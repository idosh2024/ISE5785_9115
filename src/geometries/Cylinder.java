package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

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
        Point p0 = axis.getP0();

        // Check if the point is the CENTER of the BOTTOM base of the cylinder.
        if (point.equals(axis.getP0()))
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
}
