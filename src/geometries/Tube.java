package geometries;
import primitives.*;
import primitives.Util;

import java.util.List;
import static java.util.List.of;
import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/** Infinite tube defined by an axis ray and radius (Stage 1: getNormal returns null). */
public class Tube extends RadialGeometry {
    protected final Ray axis;

    public Tube(Ray axis, double radius) {
        super(radius);
        this.axis = axis;
    }

    @Override
    public Vector getNormal(Point p) {
        // We are not using any Ray getters - instruction of stage 2:
        // p0  = axis point at t=0
        final Point p0 = axis.getPoint(0);
        // v   = unit direction - ray's direction
        final Vector v = axis.getDir();

        // Project (p - p0) onto v: t = v · (p - p0)
        final double t = v.dotProduct(p.subtract(p0));

        final Point o;
        // o = projection of p onto the axis line (p0 + t*v)
        // check if t is zero (p0) to avoid a throw
        if (Util.isZero(t)) {
            o = p0;
        }
        else {
            o = p0.add(v.scale(t));
        }

        // normal = p - o (must be non-zero on a valid tube surface)
        return p.subtract(o).normalize();
    }

    @Override
    public String toString() {
        return "Tube{" +
                "axis=" + axis +
                ", radius=" + radius +
                '}';
    }

    /// Finding intersections by the formula found in the internet
    /// www.scratchapixel.com/lessons/3d-basic-rendering
    @Override
    public List<Point> findIntersections(Ray ray) {
        var p0 = ray.getPoint(0);
        var v  = ray.getDir();

        var a  = axis.getDir(); // axis (unit)
        var o  = axis.getPoint(0);  // axis origin

        // A = |v_perp|^2 = 1 - (v·a)^2  (Ray dir is normalized)
        double dv = v.dotProduct(a);
        double A  = alignZero(1 - dv * dv);
        if (isZero(A)) {
            // Ray parallel to axis → no intersections with lateral surface
            return null;
        }

        // Δ = p0 - o, computed safely (avoid zero vector when equal)
        double vDotDelta, da, deltaLen2;
        if (p0.equals(o)) {
            vDotDelta = 0;
            da        = 0;
            deltaLen2 = 0;
        } else {
            var delta  = p0.subtract(o);
            vDotDelta  = v.dotProduct(delta);
            da         = delta.dotProduct(a);
            deltaLen2  = delta.lengthSquared();
        }

        // Quadratic coefficients with B' (not 2B)
        double Bp = vDotDelta - dv * da;
        double C  = deltaLen2 - da * da - radius * radius;

        double D = alignZero(Bp * Bp - A * C);
        if (D <= 0) return null; // miss or tangent → 0 hits

        double sqrtD = Math.sqrt(D);
        double t1 = alignZero((-Bp - sqrtD) / A);
        double t2 = alignZero((-Bp + sqrtD) / A);

        boolean h1 = t1 > 0;
        boolean h2 = t2 > 0;

        if (h1 && h2) return of(ray.getPoint(t1), ray.getPoint(t2)); // t1 < t2
        if (h1)       return of(ray.getPoint(t1));
        if (h2)       return of(ray.getPoint(t2));
        return null;
    }

}

