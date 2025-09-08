package geometries;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import primitives.*;
import java.util.List;

class PlaneTest {

    /// points for the tests + delta (accuracy) for assertEquals
    public static final double DELTA = 0.000001;
    private final Point P100 = new Point(1, 0, 0);
    private final Point P010 = new Point(0, 1, 0);
    private final Point P001 = new Point(0, 0, 1);
    private final Point P004 = new Point(0, 0, 4);
    private final Point P256 = new Point(2, 5, 6);
    private final Vector P111 = new Vector(1, 1, 1);

    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Simple constructor test
        assertDoesNotThrow(() -> new Plane(P001, P010, P100),
                "ERROR: Failed constructing a correct plane");

        // =============== Boundary Values Tests =================
        // TC02: First and second points are the same
        assertThrows(IllegalArgumentException.class, () -> new Plane(P001, P001, P100),
                "ERROR: Failed to throw an exception when the first and second points are the same");

        // TC03: First and third points are the same
        assertThrows(IllegalArgumentException.class, () -> new Plane(P001, P010, P001),
                "ERROR: Failed to throw an exception when the first and third points are the same");

        // TC04: Second and third points are the same
        assertThrows(IllegalArgumentException.class, () -> new Plane(P001, P010, P010),
                "ERROR: Failed to throw an exception when the second and third points are the same");

        // TC05: All points are the same
        assertThrows(IllegalArgumentException.class, () -> new Plane(P001, P001, P001),
                "ERROR: Failed to throw an exception when all points are the same");
        // TC12: The three points are on the same line - should throw exception
        assertThrows(IllegalArgumentException.class,
                () -> new Plane(P001, new Point(0,0,2), P004),
                "ERROR: Constructed a plane with three collinear points");
    }

    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        Point p1 = new Point(-2, 0, 0);
        Point p2 = new Point(4, 0, 0);
        Point p3 = new Point(0, 6, 0);
        Vector norm = new Plane(p1, p2, p3).getNormal(p1);

        assertEquals(0.0, norm.dotProduct(p1.subtract(p2)), DELTA,
                "Plane.getNormal: normal not orthogonal to edge p1-p2");
        assertEquals(0.0, norm.dotProduct(p1.subtract(p3)), DELTA,
                "Plane.getNormal: normal not orthogonal to edge p1-p3");


        // TC02: check if the normal really is normalized
        assertEquals(1,
                norm.length(),
                DELTA,
                "ERROR: Plane.getNormal - does not return a normalized vector");
    }

    @Test
    void testFindIntersections() {

        Plane plane = new Plane(P001, P010, P100);

        // ============ Equivalence Partitions Tests ==============
        //TC01: Ray intersects the plane, starts outside the plane,
        //neither parallel nor perpendicular to the plane (1 point)
        assertEquals(List.of(new Point(0.8, 0.2, 0)),
                plane.findIntersections(new Ray(P256, new Vector(-1, -4, -5))),
                "Error: Ray intersects the plane - does not work as expected"
        );

        //TC02: Ray not intersects the plane, starts outside the plane,
        //neither parallel nor perpendicular to the plane (0 point)
        assertNull(plane.findIntersections(new Ray(P256, P111)),
                "Error: Ray does not intersects the plane - does not work as expected");


        // =============== Boundary Values Tests ==================
        // **** Group: Ray is parallel to the plane (0 point)
        //TC10: The ray not included in the plane
        assertNull(plane.findIntersections(new Ray(P256, new Vector(1, 1, -2))),
                "ERROR: Ray is parallel to the plane, and the ray not included in the plane");

        //TC11: The ray included in the plane (0 point ~infinite points)
        assertNull(plane.findIntersections(new Ray(new Point(2, 0, -1), new Vector(-1, 1, 0))),
                "ERROR: Ray is parallel to the plane, and the ray included in the plane");

        // **** Group: Ray is orthogonal to the plane
        //TC12: according to the head point, before the plane (1 point)
        assertEquals(List.of(P001),
                plane.findIntersections(new Ray(new Point(-1, -1, 0), new Vector(2, 2, 2))),
                "ERROR: Ray is orthogonal to the plane, according to the head point, before the plane");

        //TC13: according to the head point, in the plane (0 point)
        assertNull(plane.findIntersections(new Ray(P001, P111)),
                "ERROR: Ray is orthogonal to the plane, according to the head point, in the plane");

        //TC14: according to the head point, after the plane (0 point)
        assertNull(plane.findIntersections(new Ray(new Point(1, 1, 2), P111)),
                "ERROR: Ray is orthogonal to the plane, according to the head point, after the plane");

        // **** Group: Ray is neither orthogonal nor parallel to the plane
        //TC15: Ray begins at the plane (0 point)
        assertNull(plane.findIntersections(new Ray(new Point(2, -2, 1), new Vector(-1, 3, 1))),
                "ERROR: Ray is neither orthogonal nor parallel to ray and begin at the plane");

        //TC16: Ray begins in the same point which appears as reference point in the plane (0 point)
        assertNull(plane.findIntersections(new Ray(P100, new Vector(0, 1, 2))),
                "ERROR: Ray is neither orthogonal nor parallel to ray " +
                        "and begins in the same point which appears as reference point in the plane");

    }
}