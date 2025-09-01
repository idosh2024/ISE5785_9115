package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/** Infinite tube defined by an axis ray and radius (Stage 1: getNormal returns null). */
public class Tube extends RadialGeometry {
    protected final Ray axis;

    public Tube(Ray axis, double radius) {
        super(radius);
        this.axis = axis;
    }

    @Override
    public Vector getNormal(Point point) {
        return null; // Stage 1
    }
}

