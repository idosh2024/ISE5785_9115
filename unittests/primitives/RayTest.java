package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RayTest {

    @Test
    void testGetPoint() {
        Ray ray = new Ray(new Point(1, 0, 0), new Vector(1, 0, 0));

        // ============ Equivalence Partitions Tests ==============
        // TC01: The point is before the head of the ray (t < 0)
        assertEquals(new Point(0, 0, 0), ray.getPoint(-1),
                "ERROR: point is before the head of the ray (t < 0) - not working as expected");

        // TC02: The point is after the head of the ray (t > 0)
        assertEquals(new Point(2, 0, 0), ray.getPoint(1),
                "ERROR: point is after the head of the ray (t > 0) - not working as expected");

        // =============== Boundary Values Tests =================
        // TC03: The point is on the head of the ray (t = 0)
        assertEquals(new Point(1, 0, 0), ray.getPoint(0),
                "ERROR: point is on the head of the ray (t = 0) - not working as expected");
    }
}