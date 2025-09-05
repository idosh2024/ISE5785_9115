package primitives;

/**
 * Class Ray represents a ray in 3D Cartesian coordinate system.
 * A ray is defined by a starting point (p0) and a normalized direction vector (dir).
 * All instances are immutable.
 * @author Ido
 */
public class Ray {
    /** Starting point of the ray */
    private final Point p0;

    /** Normalized direction vector */
    private final Vector dir;

    /**
     * Constructs a Ray given a point and a direction vector.
     * The vector is normalized before being stored.
     * @param p0 starting point of the ray
     * @param dir direction vector (will be normalized internally)
     */
    public Ray(Point p0, Vector dir) {
        this.p0 = p0;
        this.dir = dir.normalize(); // must be unit vector
    }

    /**
     * Returns the head point of the ray. (had to give it a getter for tube+cylinder)
     */
    public Point getP0() {
        return p0;
    }

    /**
     * Returns the direction of the ray. (had to give it a getter for tube+cylinder)
     */
    public Vector getDir() {
        return dir;
    }


    /// implementation of equals and toString
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Ray other)
                && this.p0.equals(other.p0)
                && this.dir.equals(other.dir);
    }

    @Override
    public String toString() {
        return "Ray{" + "p0=" + p0 + ", dir=" + dir + '}';
    }
}
