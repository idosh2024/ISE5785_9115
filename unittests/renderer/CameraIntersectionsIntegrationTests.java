package renderer;

import geometries.*;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests: Camera ray construction (3x3) + intersections with geometries.
 * We count the TOTAL number of intersection points over all 9 rays.
 */
class CameraIntersectionsIntegrationTests {

    /** Canonical camera used in all tests (P0 at origin, look to -Z, up +Y) */
    private Camera buildCam() {
        return Camera.getBuilder()
                .setLocation(Point.ZERO)
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(3, 3)    // view-plane size in world units
                .setVpDistance(1)   // distance from camera
                .build();
    }

    /**
     * Helper: cast 3x3 primary rays and sum intersections.
     */
    private int countIntersections(Camera cam, Intersectable shape, int nX, int nY) {
        int count = 0;
        for (int i = 0; i < nY; i++) {
            for (int j = 0; j < nX; j++) {
                var ray = cam.constructRay(nX, nY, j, i);
                var hits = shape.findIntersections(ray);
                if (hits != null) count += hits.size();
            }
        }
        return count;
    }

    // ---------------- S P H E R E ----------------
    @Test
    void sphere_totalHits() {
        var cam = buildCam();

        // Small sphere in front -> only center ray crosses (2 hits)
        var s1 = new Sphere(new Point(0, 0, -3), 1d);
        assertEquals(2, countIntersections(cam, s1, 3, 3),
                "Sphere r=1 at (0,0,-3) should yield 2 hits total");

        // Medium sphere -> several rays cross (2 each) -> total 10
        var s2 = new Sphere(new Point(0, 0, -2.5), 2d);
        assertEquals(10, countIntersections(cam, s2, 3, 3),
                "Sphere r=2 at (0,0,-2.5) should yield 10 hits total");

        // Big sphere that contains the camera -> each ray exits once -> 9
        var s3 = new Sphere(new Point(0, 0, -2.5), 3d);
        assertEquals(9, countIntersections(cam, s3, 3, 3),
                "Sphere r=3 at (0,0,-2.5) (camera inside) should yield 9 hits total");
    }

    // ---------------- P L A N E ----------------
    @Test
    void plane_totalHits() {
        var cam = buildCam();

        // Plane z = -2 with normal +Z. All 9 rays should hit once.
        var p = new Plane(new Point(0, 0, -2), new Vector(0, 0, 1));
        assertEquals(9, countIntersections(cam, p, 3, 3),
                "Front parallel plane should be hit once by each of the 9 rays");
    }

    // ---------------- T R I A N G L E ----------------
    @Test
    void triangle_totalHits() {
        var cam = buildCam();

        // A centered triangle on z=-2 sized so only the center ray hits.
        var t = new Triangle(
                new Point(-1,  1, -2),
                new Point( 1,  1, -2),
                new Point( 0, -1, -2)
        );
        assertEquals(1, countIntersections(cam, t, 3, 3),
                "Only the center ray should hit this triangle");
    }
}
