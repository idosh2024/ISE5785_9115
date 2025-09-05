package geometries;
import primitives.*;
import primitives.Util;

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
        final Point p0 = axis.getP0();
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
}

