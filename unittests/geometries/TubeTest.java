package geometries;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import primitives.*;
import java.util.List;

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

    /// tests for findIntersections
    @Test
    void testFindIntersections() {
        // Tube around Z-axis, r=1
        Tube tube = new Tube(new Ray(new Point(0,0,0), new Vector(0,0,1)), 1d);

        // ============ Equivalence Partitions ============

        // EP-TC01: 2 hits (ray crosses the tube)
        var r1 = new Ray(new Point(-2, 0, 0), new Vector(1, 0, 0));
        var hit1 = tube.findIntersections(r1);
        assertNotNull(hit1, "EP-TC01: should hit twice");
        assertEquals(2, hit1.size(), "EP-TC01: wrong #points");
        assertEquals(List.of(new Point(-1,0,0), new Point(1,0,0)), hit1,
                "EP-TC01: expected points/order");

        // EP-TC02: start inside → 1 hit
        var hit2 = tube.findIntersections(new Ray(new Point(0.5, 0, 0), new Vector(1, 0, 0)));
        assertNotNull(hit2, "EP-TC02");
        assertEquals(1, hit2.size(), "EP-TC02");
        assertEquals(new Point(1,0,0), hit2.getFirst(), "EP-TC02: expected exit");

        // EP-TC03: start after / away → 0 hits
        assertNull(tube.findIntersections(new Ray(new Point(2, 0, 0), new Vector(1, 0, 0))),
                "EP-TC03: should be null");

        // =============== Boundary Values ===============

        // BVA: tangent → 0 hits
        assertNull(tube.findIntersections(new Ray(new Point(0, 1, 0), new Vector(1, 0, 0))),
                "BVA: tangent should be null");

        // BVA: direction parallel to axis → 0 hits
        assertNull(tube.findIntersections(new Ray(new Point(2, 0, 0), new Vector(0, 0, 1))),
                "BVA: parallel-to-axis should be null");

        // BVA: start on surface and go inside → 1 hit
        var hit3 = tube.findIntersections(new Ray(new Point(1, 0, 0), new Vector(-1, 0, 0)));
        assertNotNull(hit3, "BVA: on-surface inward should hit");
        assertEquals(1, hit3.size(), "BVA: on-surface inward one hit");
        assertEquals(new Point(-1,0,0), hit3.getFirst());

        // BVA: start on surface and go outward → 0 hits
        assertNull(tube.findIntersections(new Ray(new Point(1, 0, 0), new Vector(1, 0, 0))),
                "BVA: on-surface outward should be null");
    }
}