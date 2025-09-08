package geometries;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import primitives.*;
import java.util.List;

class CylinderTest {

    @Test
    void testGetNormal() {
        // ============ Equivalence partitions Tests ==============
        Vector v1 = new Vector(0, 0, 1);
        Ray r = new Ray(new Point(0, 0, 1), v1);
        Cylinder cyl = new Cylinder(r, 5, 10);

        // TC01: check correction of normal on cylinder's side
        assertEquals(new Vector(0.6, 0.8, 0), cyl.getNormal(new Point(3, 4, 2)),
                "ERROR: Cylinder.getNormal - incorrect tube vector for side");

        //TC02: check correction of normal on cylinder's close-to-axis base
        assertEquals(v1.scale(-1), cyl.getNormal(new Point(2, 3, 1)),
                "ERROR: Cylinder.getNormal - incorrect tube vector for close-to-axis base");

        //TC03: check correction of normal on cylinder's far-from-axis base
        assertEquals(v1, cyl.getNormal(new Point(2, 2, 11)),
                "ERROR: Cylinder.getNormal - incorrect tube vector for far-from-axis base");

        // =============== Boundary Values Tests ==================
        // TC10: normal for the center of cylinder's close-to-axis base
        assertEquals(v1.scale(-1), cyl.getNormal(new Point(0, 0, 1)),
                "ERROR: Cylinder.getNormal - incorrect tube vector for far-from-axis base's center");

        // TC11: normal for the center of cylinder's far-from-axis base
        assertEquals(v1, cyl.getNormal(new Point(0, 0, 11)),
                "ERROR: Cylinder.getNormal - incorrect tube vector for far-from-axis base's center");

        // TC12: normal for a point on the EDGE of the close-to-axis (bottom) base
        assertEquals(v1.scale(-1), cyl.getNormal(new Point(5, 0, 1)),
                "ERROR: Cylinder.getNormal - incorrect normal on bottom edge");

        // TC13: normal for a point on the EDGE of the far-from-axis (top) base
        assertEquals(v1, cyl.getNormal(new Point(5, 0, 11)),
                "ERROR: Cylinder.getNormal - incorrect normal on top edge");
    }

    @Test
    void testFindIntersections() {
        // Cylinder: Z-axis, r=1, h=2 ; bases at z=0 and z=2
        Cylinder cyl = new Cylinder(new Ray(new Point(0,0,0), new Vector(0,0,1)), 1d, 2d);

        // ============ Equivalence Partitions ============

        // EP-TC01: Cross mantle inside height (two hits)
        var r1 = new Ray(new Point(-2, 0, 1), new Vector(1, 0, 0));
        var h1 = cyl.findIntersections(r1);
        assertNotNull(h1, "EP-TC01");
        assertEquals(2, h1.size(), "EP-TC01");
        assertEquals(List.of(new Point(-1,0,1), new Point(1,0,1)), h1,
                "EP-TC01: order/points");

        // EP-TC02: Same line but outside height (z=3) → 0 hits
        assertNull(cyl.findIntersections(new Ray(new Point(-2, 0, 3), new Vector(1, 0, 0))),
                "EP-TC02: outside height");

        // EP-TC03: Through both caps, along axis (two cap hits)
        var h3 = cyl.findIntersections(new Ray(new Point(0, 0, -1), new Vector(0, 0, 1)));
        assertNotNull(h3, "EP-TC03");
        assertEquals(2, h3.size(), "EP-TC03");
        assertEquals(List.of(new Point(0,0,0), new Point(0,0,2)), h3,
                "EP-TC03: cap points");

        // =============== Boundary Values ===============

        var h4 = cyl.findIntersections(new Ray(new Point(0,0,3), new Vector(0,0,-1)));
        assertNotNull(h4, "BVA top");
        assertEquals(2, h4.size(), "BVA top should hit both caps");
        assertEquals(List.of(new Point(0,0,2), new Point(0,0,0)), h4);


        // BVA: Start on bottom cap and go inside → only top cap
        var h5 = cyl.findIntersections(new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)));
        assertNotNull(h5, "BVA bottom->in");
        assertEquals(1, h5.size(), "BVA bottom->in");
        assertEquals(new Point(0,0,2), h5.getFirst());

        // BVA: Start on bottom cap and go outward → 0
        assertNull(cyl.findIntersections(new Ray(new Point(0, 0, 0), new Vector(0, 0, -1))),
                "BVA bottom->out");

        // BVA: Start on mantle and go inward → 1 mantle hit
        var h6 = cyl.findIntersections(new Ray(new Point(1, 0, 1), new Vector(-1, 0, 0)));
        assertNotNull(h6, "BVA mantle->in");
        assertEquals(1, h6.size(), "BVA mantle->in");
        assertEquals(new Point(-1,0,1), h6.getFirst());

        // BVA: Start on mantle and go outward → 0
        assertNull(cyl.findIntersections(new Ray(new Point(1, 0, 1), new Vector(1, 0, 0))),
                "BVA mantle->out");
    }

}