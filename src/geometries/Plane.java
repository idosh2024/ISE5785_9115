package geometries;

import primitives.*;

import java.util.List;
import static java.util.List.of;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Represents a plane in three-dimensional space.
 */
public class Plane extends Geometry {

    private final Point p;
    private final Vector normal;

    /**
     * takes 3 points on the plane and set the normal vector and a point on the
     * plane - by making vectors between the points and taking the cross product normalized
     *
     * @param p1 point on the plane
     * @param p2 point on the plane
     * @param p3 point on the plane
     */
    public Plane(Point p1, Point p2, Point p3) {
        this.p = p1;
        this.normal = p2.subtract(p1).crossProduct(p3.subtract(p1)).normalize();
    }

    /**
     * Constructor to initialize a plane based on a point and a normal vector
     *
     * @param p1 point on the plane
     * @param v1 normal vector to the plane
     */
    public Plane(Point p1, Vector v1) {
        this.p = p1;
        this.normal = v1.normalize();
    }

    /**
     * Getter for the normal of the plane, as we asked
     *
     * @return the normal vector of the plane
     */
    @Override
    public Vector getNormal(Point p) {
        return normal;
    }

    // In src/geometries/Plane.java
    @Override
    public String toString() {
        return "Plane{" +
                "point=" + p +
                ", normal=" + normal +
                '}';
    }

    /// Finding intersections by the formula:
    /// t = (N · (Q0 - P0)) / (N · V
    @Override
    public List<Point> findIntersections(Ray ray) {
        var p0 = ray.getPoint(0);
        var v  = ray.getDir();

        double nv = normal.dotProduct(v);
        if (isZero(nv)) return null; // parallel (includes "ray lies in plane")

        // Safe numerator: if p0 == q0, the numerator is 0 without building a vector
        double nQMinusP0 = p0.equals(p) ? 0 : normal.dotProduct(p.subtract(p0));

        double t = alignZero(nQMinusP0 / nv);
        if (t <= 0) return null;     // don't return head or behind the head

        return of(ray.getPoint(t));
    }

}