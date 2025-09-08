package geometries;

import primitives.Point;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;

/**
 * Abstract class Geometry is the basic class representing a geometric body
 * in Euclidean 3D space.
 * All geometric shapes in the project will inherit from this class.
 *
 * @author Ido
 */
public abstract class Geometry implements Intersectable {

    /**
     * Returns the normal vector to the geometry at a given point on its surface.
     * @param point the point on the surface
     * @return the normal vector
     */
    public abstract Vector getNormal(Point point);

    @Override
    public String toString() {
        return "Geometry{}";
    }
}
