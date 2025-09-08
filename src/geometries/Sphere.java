package geometries;

import primitives.*;

import java.util.List;
import static java.util.List.of;
import static primitives.Util.alignZero;

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


    /// Finding intersections by formula found in the internet
    /// www.scratchapixel.com/lessons/3d-basic-rendering
    @Override
    public List<Point> findIntersections(Ray ray) {
        var p0 = ray.getPoint(0);
        var v  = ray.getDir();

        // special case: ray starts at the center -> one exit point at distance r
        if (p0.equals(center)) return of(ray.getPoint(radius));

        // general case
        var    u  = center.subtract(p0);              // vector from ray head to sphere center
        double tm = v.dotProduct(u);                  // projection of u onto v
        double d2 = alignZero(u.lengthSquared() - tm * tm);
        double r2 = radius * radius;

        // no hit if outside or tangent (tangent must be 0 hits by the spec)
        if (d2 >= r2) return null;

        double th = Math.sqrt(r2 - d2);               // half-chord length along the ray
        double t1 = alignZero(tm - th);
        double t2 = alignZero(tm + th);

        // do not include the ray's head (t == 0) - took care of it int he case above
        boolean hit1 = t1 > 0;
        boolean hit2 = t2 > 0;

        // calling return of(...) only when 2 intersections
        if (hit1 && hit2) return of(ray.getPoint(t1), ray.getPoint(t2));
        // one intersection
        if (hit1)         return of(ray.getPoint(t1));
        if (hit2)         return of(ray.getPoint(t2));
        return null;
    }
}
