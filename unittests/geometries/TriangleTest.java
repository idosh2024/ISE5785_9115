package geometries;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import primitives.*;


class TriangleTest {

    @Test
    void testFindIntersections() {

        final Vector v1 = new Vector(0, 0, -1);
        final Vector v2 = new Vector(0, 0, 1);
        final Point p2 = new Point(0, 1, 0);
        final Point p3 = new Point(1, 0, 0);
        Triangle triangle = new Triangle(new Point(1, 1, 0), p3, p2);

        // ============ Equivalence Partitions Tests ==============
        // TC01: the intersection point is inside the triangle
        assertEquals(1, triangle.findIntersections(
                        new Ray(new Point(1.8, 1.8, 1), new Vector(-1, -1, -1))).size(),
                "Failed to find the intersection point when the intersection point is inside the triangle");
        // TC02: the intersection point is outside the triangle and against an edge
        assertNull(triangle.findIntersections(
                        new Ray(new Point(0.5, 2, 1), v1)),
                "Failed to find the intersection point when the intersection point is outside the triangle and against an edge");
        // TC03: the intersection point is outside the triangle and against a vertex
        assertNull(triangle.findIntersections(
                        new Ray(new Point(2, 2, 1), v1)),
                "Failed to find the intersection point when the intersection point is outside the triangle and against an edge");


        // ================= Boundary Values Tests =================
        // TC04: the intersection point is on the edge of the triangle
        assertNull(triangle.findIntersections(
                        new Ray(new Point(0.5, 1, -1), v2)),
                "Failed to find the intersection point when the intersection point is on the edge of the triangle");
        // TC05: the intersection point is on the vertex of the triangle
        assertNull(triangle.findIntersections(
                        new Ray(new Point(1, 1, 1), new Vector(0, 0, -1))),
                "Failed to find the intersection point when the intersection point is on the vertex of the triangle");
        // TC06: the intersection point is outside the triangle but in the path of the edge
        assertNull(triangle.findIntersections(
                        new Ray(new Point(2, 1, -1), v2)),
                "Failed to find the intersection point when the intersection point is outside the triangle but in the path of the edge");

    }
}