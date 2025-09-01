package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/** Finite cylinder: tube with a given height (Stage 1: getNormal returns null). */
public class Cylinder extends Tube {
    private final double height;

    public Cylinder(Ray axis, double radius, double height) {
        super(axis, radius);
        this.height = height;
    }

    @Override
    public Vector getNormal(Point point) {
        return null; // Stage 1
    }
}
