package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * Sphere is a 3D geometry defined by a center point and a radius.
 * It extends RadialGeometry.
 * All instances are immutable.
 * @author Ido
 */
public class Sphere extends RadialGeometry {

    /** The center point of the sphere */
    private final Point center;

    /**
     * Constructs a Sphere with a given center and radius.
     * @param center the center point of the sphere
     * @param radius the radius of the sphere
     */
    public Sphere(Point center, double radius) {
        super(radius);
        this.center = center;
    }

    /**
     * Calculates the normal to the sphere at a given point on its surface.
     * @param point a point on the sphere surface - ready for stage 2
     * @return the normalized normal vector
     */
    @Override
    public Vector getNormal(Point point) {
        return point.subtract(center).normalize();
    }

    // In src/geometries/Sphere.java
    @Override
    public String toString() {
        return "Sphere{" +
                "center=" + center +
                ", radius=" + radius +
                '}';
    }

}
