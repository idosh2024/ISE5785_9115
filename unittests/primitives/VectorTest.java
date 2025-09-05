package primitives;

import static org.junit.jupiter.api.Assertions.*;

class VectorTest {

    @org.junit.jupiter.api.Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Simple add test
        assertEquals(new Vector(1, 2, 3),
                new Vector(1, 1, 1).add(new Vector(0, 1, 2)),
                "ERROR: Vector + Vector does not work correctly");

        // TC12: add vector to its opposite and equals in length - should throw exception
        assertThrows(IllegalArgumentException.class,
                () -> new Vector(1, 2, 3).add(new Vector(-1, -2, -3)),
                "ERROR: Vector + (-Vector) does not throw an exception");
    }

    @org.junit.jupiter.api.Test
    void testScale() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Simple scale test
        assertEquals(new Vector(2, 4, 6),
                new Vector(1, 2, 3).scale(2),
                "ERROR: Vector * Scalar does not work correctly");

        // =============== Boundary Values Tests =================
        // TC11: scale by zero - should throw exception (zero vector)
        assertThrows(IllegalArgumentException.class,
                () -> new Vector(1, 2, 3).scale(0),
                "ERROR: scaling by 0 does not throw an exception");
    }

    @org.junit.jupiter.api.Test
    void testDotProduct() {

        // ============ Equivalence Partitions Tests ==============
        // TC01: Simple dot product test - any two vectors
        assertEquals(10,
                new Vector(1, 2, 3).dotProduct(new Vector(3, 2, 1)),
                "ERROR: dotProduct for orthogonal vectors does not work correctly");

        // TC12: Simple dot product test - orthogonal vectors - always 0
        assertEquals(0,
                new Vector(1, 0, 0).dotProduct(new Vector(0, 1, 0)),
                "ERROR: dotProduct for orthogonal vectors does not work correctly");

        // TC13: Simple dot product test - one vector length is 1
        assertEquals(3,
                new Vector(1, 2, 3).dotProduct(new Vector(0, 0, 1)),
                "ERROR: dotProduct for one vector length is 1 does not work correctly");

    }

    @org.junit.jupiter.api.Test
    void testCrossProduct() {

        // ============ Equivalence Partitions Tests ==============
        // TC01: Simple cross product test - any two vectors
        assertEquals(new Vector(-4, 8, -4),
                new Vector(1, 2, 3).crossProduct(new Vector(3, 2, 1)),
                "ERROR: crossProduct for orthogonal vectors does not work correctly");

        // TC12: Simple cross product test - parallel vectors - should throw exception
        assertThrows(IllegalArgumentException.class,
                () -> new Vector(1, 2, 3).crossProduct(new Vector(-2, -4, -6)),
                "ERROR: crossProduct for opposite direction vectors does not throw an exception");
    }

    @org.junit.jupiter.api.Test
    void testLengthSquared() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Simple length squared test
        assertEquals(14,
                new Vector(1, 2, 3).lengthSquared(),
                "ERROR: lengthSquared does not work correctly");
    }

    @org.junit.jupiter.api.Test
    void testLength() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Simple length test
        assertEquals(5,
                new Vector(0, 3, 4).length(),
                "ERROR: length does not work correctly");

    }

    @org.junit.jupiter.api.Test
    void testNormalize() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Simple normalize test
        assertEquals(new Vector(0, 0.6, 0.8),
                new Vector(0, 3, 4).normalize(),
                "ERROR: normalize does not work correctly");

        // =============== Boundary Values Tests =================
        // TC12: normalize vector with length 1 - already normalized
        assertEquals(new Vector(0, 1, 0),
                new Vector(0, 1, 0).normalize(),
                "ERROR: normalize does not work correctly");
    }
}