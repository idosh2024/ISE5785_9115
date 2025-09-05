package geometries;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import primitives.Point;
import primitives.Vector;

class PlaneTest {

    /// points for the tests + delta for assertEquals
    public static final double DELTA = 0.000001;
    private final Point P100 = new Point(1, 0, 0);
    private final Point P010 = new Point(0, 1, 0);
    private final Point P001 = new Point(0, 0, 1);
    private final Point P004 = new Point(0, 0, 4);

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
}