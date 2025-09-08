package geometries;
import java.util.List;
import primitives.Point;
import primitives.Ray;

/** Triangle – a polygon with 3 vertices. */
public class Triangle extends Polygon {
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3);
    }

    /// Finding intersections of the triangle by the ray -
    /// by using the findIntersections of the Polygon class
    @Override
    public List<Point> findIntersections(Ray ray) {
        return super.findIntersections(ray);
    }
}
