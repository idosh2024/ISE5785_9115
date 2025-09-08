package geometries;

import primitives.*;

import java.util.List;
import static java.util.List.of;
import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/** Finite cylinder: tube with a given height */
public class Cylinder extends Tube {
    private final double height;

    public Cylinder(Ray axis, double radius, double height) {
        super(axis, radius);
        if (height <= 0) {throw new IllegalArgumentException("Height must be positive");}
        this.height = height;
    }

    @Override
    public Vector getNormal(Point point) {

        Vector rayAxis = axis.getDir();
        Point p0 = axis.getPoint(0);

        // Check if the point is the CENTER of the BOTTOM base of the cylinder.
        if (point.equals(axis.getPoint(0)))
            // The normal for the bottom base is the opposite of the axis direction.
            return rayAxis.scale(-1);

        // Calculate the CENTER of the TOP base of the cylinder - important for the next possibilities.
        Point topCenter = p0.add(rayAxis.scale(height));

        // Check if the point is the CENTER of the TOP base.
        if (point.equals(topCenter))
            // Return the normal pointing outward from the top base (in the same direction
            // as the axis).
            return rayAxis;

        // Check if the point lies on the BOTTOM base plane.
        // Compute the vector from the bottom base center to the point and check if it
        // is perpendicular to the axis direction.
        if (p0.subtract(point).dotProduct(rayAxis) == 0)
            // The normal for the bottom base is the opposite of the axis direction.
            return rayAxis.scale(-1);

        // Check if the point lies on the TOP base plane.
        // Compute the vector from the top base center to the point and check if it is
        // perpendicular to the axis direction.
        if (topCenter.subtract(point).dotProduct(rayAxis) == 0)
            // The normal for the top base is in the same direction as the axis.
            return rayAxis;

        // If none of the above conditions apply, the point is on the lateral surface of
        // the cylinder.
        // In this case, delegate the normal calculation to the superclass (from Tube class).
        return super.getNormal(point);
    }

    @Override
    public String toString() {
        return "Cylinder{" +
                "Ray=" + axis +
                ", radius=" + radius +
                ", height=" + height +
                '}';
    }

    /// Finding intersections by combining Tube intersections and capping planes
    /// methods found in Internet sources
    @Override
    public List<Point> findIntersections(Ray ray) {
        var v  = ray.getDir();
        var p0 = ray.getPoint(0);      // head
        var a  = axis.getDir();        // axis (unit)
        var o  = axis.getPoint(0);     // bottom-cap center

        List<Point> result = null;

        // 1) Lateral surface (Tube) — keep only 0 < s < height
        List<Point> tubeHits = super.findIntersections(ray);
        if (tubeHits != null) {
            for (Point P : tubeHits) {
                double s = P.equals(o) ? 0 : alignZero(a.dotProduct(P.subtract(o)));
                if (s > 0 && s < height) {
                    if (result == null) result = new java.util.LinkedList<>();
                    result.add(P);
                }
            }
        }

        // 2) Cap planes — compute both t's safely, then add in ascending t
        double nv = a.dotProduct(v);
        if (!isZero(nv)) {
            // bottom cap @ o  (safe numerator: 0 if p0 == o)
            double numBottom = p0.equals(o) ? 0 : a.dotProduct(o.subtract(p0));
            double tBottom   = alignZero(numBottom / nv);

            // top cap @ oTop = o + a*height  (safe numerator: 0 if p0 == oTop)
            Point  oTop    = o.add(a.scale(height));
            double numTop  = p0.equals(oTop) ? 0 : a.dotProduct(oTop.subtract(p0));
            double tTop    = alignZero(numTop / nv);

            Point pBottom = null, pTop = null;

            if (tBottom > 0) {
                Point cand = ray.getPoint(tBottom);
                if (isInsideDisk(cand, o, a, radius)) pBottom = cand;
            }
            if (tTop > 0) {
                Point cand = ray.getPoint(tTop);
                if (isInsideDisk(cand, oTop, a, radius)) pTop = cand;
            }

            if (pBottom != null && pTop != null) {
                if (result == null) result = new java.util.LinkedList<>();
                if (tTop < tBottom) { result.add(pTop); result.add(pBottom); }
                else                { result.add(pBottom); result.add(pTop); }
            } else if (pBottom != null) {
                if (result == null) result = new java.util.LinkedList<>();
                result.add(pBottom);
            } else if (pTop != null) {
                if (result == null) result = new java.util.LinkedList<>();
                result.add(pTop);
            }
        }

        if (result == null) return null;

        // Optional: globally order by distance from ray head (near→far)
        result.sort(java.util.Comparator.comparingDouble(P -> v.dotProduct(P.subtract(p0))));

        return result.size() == 1 ? java.util.List.of(result.getFirst()) : result;
    }

    /** true iff p lies inside (or on) the circular cap disk centered at 'center' */
    private static boolean isInsideDisk(Point p, Point center, Vector axisDir, double radius) {
        if (p.equals(center)) return true;                      // exact center
        Vector d = p.subtract(center);                          // safe after equals check
        double ax = d.dotProduct(axisDir);                      // axial component
        double perp2 = alignZero(d.lengthSquared() - ax * ax);  // squared radial distance
        return perp2 <= radius * radius + 1e-10;                // include seam (tiny eps)
    }

}
