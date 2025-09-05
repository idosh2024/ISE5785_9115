package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * Abstract class RadialGeometry represents radial geometric bodies (with a radius)
 * such as Sphere, Tube, and Cylinder.
 * It extends Geometry and adds a radius field.
 * All instances are immutable.
 * @author Ido
 */
public abstract class RadialGeometry extends Geometry {

    /** Radius of the radial geometry */
    protected final double radius;

    /**
     * Constructs a RadialGeometry with a given radius(only positive radius allowed).
     * @param radius the radius of the geometry
     */
    public RadialGeometry(double radius) {
        if (radius <= 0) {
            throw new IllegalArgumentException("Radius must be greater than 0");
        }
        this.radius = radius;
    }

    @Override
    public abstract Vector getNormal(Point point);

    @Override
    public String toString() {
        return "RadialGeometry{" + "radius=" + radius + '}';
    }
}

