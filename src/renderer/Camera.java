package renderer;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.MissingResourceException;

import static primitives.Util.isZero;

/**
 * Camera that emits primary rays through a view plane.
 * Built via Builder (inner static class) and uses an orthonormal basis {vTo, vUp, vRight}.
 *
 * <p>Indices convention for pixels:
 * i = row (0..nY-1, goes DOWN), j = column (0..nX-1, goes RIGHT).
 * vUp points UP, so the vertical shift is subtracted.</p>
 *
 * <p>Required fields (set via Builder): location (P0), vTo/vUp (basis), view-plane width/height, distance.</p>
 *
 * @author Ido Shamir
 */
public class Camera implements Cloneable {

    // --- Pose ---
    private Point  p0;            // camera location
    private Vector vTo;           // forward (unit)
    private Vector vUp;           // up (unit)
    private Vector vRight;        // right (unit) = vTo × vUp

    // --- View plane geometry ---
    private double vpWidth;       // VP width  (w  > 0)
    private double vpHeight;      // VP height (h  > 0)
    private double vpDistance;    // distance  (d  > 0)

    // (Optional) resolution cached on the camera if you want; constructRay still receives nX,nY
    private int nX;
    private int nY;

    /** Default ctor — start "empty"; populate only via Builder. */
    private Camera() { }

    /** @return a fresh Builder for Camera */
    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * Construct a primary ray through the center of pixel (i,j) on an nX×nY view plane.
     * Implements the DZ “Ray Construction through a Pixel” formulas.
     *
     * @param nX number of columns (pixels in X)
     * @param nY number of rows    (pixels in Y)
     * @param j  column index [0..nX-1]
     * @param i  row    index [0..nY-1]
     * @return the ray from camera through that pixel center
     * @throws IllegalStateException if pose/VP not fully initialized
     * @throws IllegalArgumentException for bad indices or non-positive nX/nY
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        // Basic guards
        if (p0 == null || vTo == null || vUp == null || vRight == null)
            throw new IllegalStateException("Camera basis/position not initialized");
        if (!(vpWidth  > 0) || !(vpHeight > 0) || !(vpDistance > 0))
            throw new IllegalStateException("View-plane size/distance must be positive");
        if (nX <= 0 || nY <= 0) throw new IllegalArgumentException("nX and nY must be positive");
        if (j < 0 || j >= nX || i < 0 || i >= nY)
            throw new IllegalArgumentException("Pixel indices out of range");

        // Pixel size
        double rX = vpWidth  / nX;
        double rY = vpHeight / nY;

        // View-plane center: PC = P0 + d * vTo
        Point pc = p0.add(vTo.scale(vpDistance));

        // Offsets to pixel center
        double xShift = (j - (nX - 1) / 2.0) * rX;
        double yShift = (i - (nY - 1) / 2.0) * rY;

        // Pij = PC + xShift*vRight - yShift*vUp  (minus because rows go downward)
        Point pij = pc;
        if (!isZero(xShift)) pij = pij.add(vRight.scale(xShift));
        if (!isZero(yShift)) pij = pij.add(vUp.scale(-yShift));

        // Ray direction
        Vector dir = pij.subtract(p0); // your Vector(Point) ctor guarantees non-zero; Ray will normalize
        return new Ray(p0, dir);
    }

    /** Shallow clone is fine (Point/Vector are immutable in your project). */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    // -------------------- BUILDER --------------------

    /**
     * Builder for {@link Camera}.
     * Validates inputs, computes an orthonormal basis, and returns a cloned Camera on build().
     */
    public static class Builder {
        private final Camera camera = new Camera();

        // Temporary “hints” to support flexible setter ordering
        private Point  targetHint;     // if user sets lookAt point
        private Vector upHint;         // when lookAt used, an approximate "up"

        // Optional cached resolution
        private Integer nX, nY;

        /** Set camera location (P0). */
        public Builder setLocation(Point p0) {
            if (p0 == null) throw new IllegalArgumentException("Location cannot be null");
            camera.p0 = p0;
            return this;
        }

        /**
         * Set exact camera directions (forward and up). Will be normalized and orthogonalized.
         * @param to forward vector
         * @param up upward vector (not parallel to 'to')
         */
        public Builder setDirection(Vector to, Vector up) {
            if (to == null || up == null) throw new IllegalArgumentException("Direction vectors cannot be null");
            Vector vToRaw = to;
            Vector vUpRaw = up;

            if (isZero(vToRaw.lengthSquared()) || isZero(vUpRaw.lengthSquared()))
                throw new IllegalArgumentException("Direction vectors cannot be zero");

            // Enforce orthonormal basis
            Vector vTo = vToRaw.normalize();
            Vector vRight = vTo.crossProduct(vUpRaw);
            if (isZero(vRight.lengthSquared()))
                throw new IllegalArgumentException("vUp must not be parallel to vTo");

            vRight = vRight.normalize();
            Vector vUp = vRight.crossProduct(vTo).normalize();

            camera.vTo = vTo;
            camera.vRight = vRight;
            camera.vUp = vUp;
            // clear hints (explicit beats hints)
            targetHint = null; upHint = null;
            return this;
        }

        /**
         * Look-at variant: set direction by target point and an approximate up.
         * Computes vTo = normalize(target - p0); vRight = vTo × upHint; vUp = vRight × vTo.
         * Works in any order: if p0 not set yet, values are finalized in build().
         */
        public Builder setDirection(Point target, Vector upApprox) {
            if (target == null || upApprox == null)
                throw new IllegalArgumentException("Target and upApprox cannot be null");
            Vector vTo = target.subtract(camera.p0).normalize();

            // Calculate initial vRight using upApprox × vTo
            Vector vRight = upApprox.crossProduct(vTo).normalize();

            // Calculate corrected vUp to ensure orthogonality: vTo × vRight
            Vector vUpCorrected = vTo.crossProduct(vRight).normalize();

            // Recalculate vRight with the corrected vUp for consistency: vTo × vUp
            vRight = vTo.crossProduct(vUpCorrected).normalize();

            camera.vTo = vTo;
            camera.vRight = vRight;
            camera.vUp = vUpCorrected;

            return this;
        }

        /**
         * Look-at with default up≈Y axis: setDirection(target, new Vector(0,1,0)).
         */
        public Builder setDirection(Point target) {
            return setDirection(target, new Vector(0, 1, 0));
        }

        /** Set view-plane size (width, height) — must be positive. */
        public Builder setVpSize(double width, double height) {
            if (!(width > 0) || !(height > 0))
                throw new IllegalArgumentException("View-plane size must be positive");
            camera.vpWidth = width;
            camera.vpHeight = height;
            return this;
        }

        /** Set view-plane distance (d > 0). */
        public Builder setVpDistance(double distance) {
            if (!(distance > 0))
                throw new IllegalArgumentException("View-plane distance must be positive");
            camera.vpDistance = distance;
            return this;
        }

        /** Optional: store intended resolution on the camera (constructRay still receives nX,nY). */
        public Builder setResolution(int nX, int nY) {
            if (nX <= 0 || nY <= 0)
                throw new IllegalArgumentException("Resolution must be positive");
            this.nX = nX; this.nY = nY;
            return this;
        }

        /**
         * Validate all required fields, compute any missing basis (from hints), then return a clone.
         * @return a new Camera with finalized, normalized basis
         * @throws MissingResourceException if mandatory fields are missing
         */
        public Camera build() {
            final String MISSING = "Missing field: ";
            String cameraClass = "Camera class - ";

            if (camera.p0 == null)
                throw new MissingResourceException(MISSING, cameraClass, "missing location point");
            if (camera.vTo == null)
                throw new MissingResourceException(MISSING, cameraClass,
                        "missing forward direction vector");
            if (camera.vUp == null)
                throw new MissingResourceException(MISSING, cameraClass,
                        "missing upward direction vector");
            if (camera.vpWidth == 0.0)
                throw new MissingResourceException(MISSING, cameraClass, "missing view plane width");
            if (camera.vpHeight == 0.0)
                throw new MissingResourceException(MISSING, cameraClass, "missing view plane height");
            if (camera.vpDistance == 0.0)
                throw new MissingResourceException(MISSING, cameraClass,
                        "missing view plane distance from camera");

            // adding computed resources to the exception checks
            // Fixed: Use consistent cross product order (vTo × vUp) for right-handed
            // coordinate system
            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();

            if (!(camera.vpWidth > 0))    throw new MissingResourceException(MISSING, cameraClass, "vpWidth");
            if (!(camera.vpHeight > 0))   throw new MissingResourceException(MISSING, cameraClass, "vpHeight");
            if (!(camera.vpDistance > 0)) throw new MissingResourceException(MISSING,cameraClass, "vpDistance");

            // Apply optional resolution cache
            if (nX != null) camera.nX = nX;
            if (nY != null) camera.nY = nY;

            try {
                return (Camera) camera.clone();
            } catch (CloneNotSupportedException e) {
                // Should not happen (Camera implements Cloneable; Object.clone exists)
                throw new AssertionError("Clone failed unexpectedly", e);
            }
        }

        /** Compute orthonormal basis from (p0, targetHint, upHint). */
        private void finalizeBasisFromHints() {
            Vector to = targetHint.subtract(camera.p0);
            if (isZero(to.lengthSquared()))
                throw new IllegalArgumentException("Target cannot coincide with camera location");
            Vector vTo = to.normalize();

            Vector vRight = vTo.crossProduct(upHint);
            if (isZero(vRight.lengthSquared()))
                throw new IllegalArgumentException("upApprox must not be parallel to (target - p0)");
            vRight = vRight.normalize();

            Vector vUp = vRight.crossProduct(vTo).normalize();

            camera.vTo = vTo;
            camera.vRight = vRight;
            camera.vUp = vUp;
        }
    }
}
