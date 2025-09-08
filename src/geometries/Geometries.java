package geometries;

import java.util.LinkedList;
import java.util.List;
import primitives.Point;
import primitives.Ray;

/**
 * A composite of multiple {@link Intersectable} geometries (Composite pattern).
 * @author Ido
 */

public class Geometries implements  Intersectable {
    /** immutable reference to the internal list (but list contents are mutable) */
    private final List<Intersectable> geometries = new LinkedList<>();

    /** Default constructor: empty aggregate. */
    public Geometries() { }

    /**
     * Varargs constructor to populate the aggregate.
     * @param geometries zero or more geometries to add
     */
    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    /**
     * Adds geometries into this aggregate (no removal API by design).
     * @param geometries zero or more geometries to add
     */
    public void add(Intersectable... geometries) {
        if (geometries == null) return;
        for (Intersectable g : geometries) {
            if (g != null) this.geometries.add(g);
        }
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        if(geometries.isEmpty()) /// no geometries
            return null;

        List<Point> intersectionPoints = new LinkedList<>();
            for (Intersectable geometry : geometries) {
                List<Point> points = geometry.findIntersections(ray);
                if (points != null) {
                    intersectionPoints.addAll(points);
                }
            }
            if (!intersectionPoints.isEmpty()) {
                return intersectionPoints;
            }
            return null; /// no intersections
    }

}
