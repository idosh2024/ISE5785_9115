package geometries;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import primitives.*;

class TubeTest {

    @Test
    void testGetNormal() {
        // ============ Equivalence partitions Tests ==============
        // TC01: check correction of normal calculation (direction & length)
        Ray ray = new Ray(new Point(0, 0, 1), new Vector(1, 1, 0));
        Tube tube = new Tube(ray, 1);

        assertEquals(new Vector(0, 0, 1), tube.getNormal(new Point(4, 4, 2)),
                "ERROR: tube.getNormal - incorrect tube vector");

        // =============== Boundary Values Tests ==================
        // TC10: normal is from a point orthogonal to tube's ray
        assertEquals(new Vector(0, 0, 1), tube.getNormal(new Point(0, 0, 2)),
                "ERROR: tube.getNormal - incorrect tube vector for a point orthogonal to tube's ray");
    }
}