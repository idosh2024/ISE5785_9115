package geometries;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import primitives.*;

class SphereTest {
    /// points and vectors for all the tests
    private final Point p1 = new Point(1, 0, 0);


    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test for a proper result
        assertDoesNotThrow(() -> new Sphere(p1,1), "Failed to create a proper sphere");

        // =============== Boundary Values Tests =================
        // TC02: Test for a sphere with a negative radius
        assertThrows(IllegalArgumentException.class, () -> new Sphere(p1,-1),
                "Failed to throw an exception when creating a sphere with a negative radius");
    }

    @Test
    void testGetNormal() {
        // ============ Equivalence partitions Tests ==============
        // TC01: check correction of regular normal calculation (direction & length)
        Sphere s1 = new Sphere(new Point(2, 1, 4),6);
        Point p1 = new Point(8, 1, 4);
        Vector normalP1 = new Vector(1, 0, 0);


        assertEquals(normalP1, s1.getNormal(p1),
                "ERROR: getNormal of Sphere doesn't work");
    }
}