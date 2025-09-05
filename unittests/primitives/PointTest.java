package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointTest {

    /// points for all the tests
    private final Point P123 = new Point(1, 2, 3);
    private final Point P234 = new Point(2, 3, 4);
    private final Vector V234 = new Vector(2, 3, 4);

    @Test
    void testSubtract() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Simple subtract test
        assertEquals(new Vector(-1, -1, -1),
                P123.subtract(P234),
                "ERROR: (point2 - point1) does not work correctly");

        // ================= Boundary Values Tests =================
        // TC10: subtract point from itself - should throw exception
        assertThrows(IllegalArgumentException.class,
                () -> V234.subtract(V234),
                "ERROR: (point - itself) does not throw an exception");

    }

    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Simple add test
        assertEquals(new Point(3, 5, 7),
                P123.add(V234),
                "ERROR: the add method does not work currently, check Again!");

        // ================= Boundary Values Tests =================
        // TC10: add opposite vector to point - should throw exception
        assertEquals(Point.ZERO, P123.add(new Vector(-1, -2, -3)),
                "ERROR: (point + vector) = cannot add zero vector");
    }

    @Test
    void testDistanceSquared() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Simple distance squared test
        assertEquals(3, P123.distanceSquared(P234),
                "ERROR: squared distance between points is wrong");

        // ================= Boundary Values Tests =================
        // TC10: distance squared between point and itself
        assertEquals(0, P123.distanceSquared(P123),
                "ERROR: point squared distance to itself is not zero");
    }

    @Test
    void testDistance() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Simple distance test
        assertEquals(Math.sqrt(3), P123.distance(P234),
                "ERROR: the distance method doesn't work properly");

        // ================= Boundary Values Tests =================
        // TC10: distance between point and itself
        assertEquals(0, P234.distance(P234),
                "ERROR: point distance to itself is not zero");
    }
}