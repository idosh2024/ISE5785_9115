package geometries;

import static java.lang.Double.*;
import java.util.List;
import static primitives.Util.*;
import primitives.*;

/**
 * Polygon class represents two-dimensional polygon in 3D Cartesian coordinate
 * system
 * @author Dan
 */
public class Polygon extends Geometry {
   /** List of polygon's vertices */
   protected final List<Point> vertices;
   /** Associated plane in which the polygon lays */
   protected final Plane       plane;
   /** The size of the polygon - the amount of the vertices in the polygon */
   private final int           size;

   /**
    * Polygon constructor based on vertices list. The list must be ordered by edge
    * path. The polygon must be convex.
    * @param  vertices                 list of vertices according to their order by
    *                                  edge path
    * @throws IllegalArgumentException in any case of illegal combination of
    *                                  vertices:
    *                                  <ul>
    *                                  <li>Less than 3 vertices</li>
    *                                  <li>Consequent vertices are in the same
    *                                  point
    *                                  <li>The vertices are not in the same
    *                                  plane</li>
    *                                  <li>The order of vertices is not according
    *                                  to edge path</li>
    *                                  <li>Three consequent vertices lay in the
    *                                  same line (180&#176; angle between two
    *                                  consequent edges)
    *                                  <li>The polygon is concave (not convex)</li>
    *                                  </ul>
    */
   public Polygon(Point... vertices) {
      if (vertices.length < 3)
         throw new IllegalArgumentException("A polygon can't have less than 3 vertices");
      this.vertices = List.of(vertices);
      size          = vertices.length;

      // Generate the plane according t
       // o the first three vertices and associate the
      // polygon with this plane.
      // The plane holds the invariant normal (orthogonal unit) vector to the polygon
      plane         = new Plane(vertices[0], vertices[1], vertices[2]);
      if (size == 3) return; // no need for more tests for a Triangle

      Vector  n        = plane.getNormal(vertices[0]);
      // Subtracting any subsequent points will throw an IllegalArgumentException
      // because of Zero Vector if they are in the same point
      Vector  edge1    = vertices[size - 1].subtract(vertices[size - 2]);
      Vector  edge2    = vertices[0].subtract(vertices[size - 1]);

      // Cross Product of any subsequent edges will throw an IllegalArgumentException
      // because of Zero Vector if they connect three vertices that lay in the same
      // line.
      // Generate the direction of the polygon according to the angle between last and
      // first edge being less than 180deg. It is hold by the sign of its dot product
      // with the normal. If all the rest consequent edges will generate the same sign
      // - the polygon is convex ("kamur" in Hebrew).
      boolean positive = edge1.crossProduct(edge2).dotProduct(n) > 0;
      for (var i = 1; i < size; ++i) {
         // Test that the point is in the same plane as calculated originally
         if (!isZero(vertices[i].subtract(vertices[0]).dotProduct(n)))
            throw new IllegalArgumentException("All vertices of a polygon must lay in the same plane");
         // Test the consequent edges have
         edge1 = edge2;
         edge2 = vertices[i].subtract(vertices[i - 1]);
         if (positive != (edge1.crossProduct(edge2).dotProduct(n) > 0))
            throw new IllegalArgumentException("All vertices must be ordered and the polygon must be convex");
      }
   }

   @Override
   public Vector getNormal(Point point) { return plane.getNormal(point); }

    /// Finding intersections of a polygon
    @Override
    public List<Point> findIntersections(Ray ray) {
        // 1) Intersect ray with the hosting plane (already enforces t > 0, parallel cases, etc.)
        List<Point> planeHits = plane.findIntersections(ray);
        if (planeHits == null) return null;         // no plane hit → no polygon hit

        Point  P = planeHits.getFirst();                // plane gives at most one point
        Vector n = plane.getNormal(P);              // polygon/plane normal
        int    nVerts = vertices.size();

        // 2) Point-in-convex-polygon test using consistent orientation of edge-cross-point vectors
        // First edge (to establish the required sign)
        double s0;
        try {
            Vector edge0 = vertices.get(1).subtract(vertices.get(0));
            Vector toP0  = P.subtract(vertices.get(0));   // if P equals vertex → zero → caught below
            s0 = alignZero(n.dotProduct(edge0.crossProduct(toP0)));
        } catch (IllegalArgumentException ex) {
            // Cross product (or subtract) produced a zero vector → P on vertex/edge → exclude
            return null;
        }
        if (isZero(s0)) return null;               // on edge/vertex → exclude
        boolean positive = s0 > 0;

        // Remaining edges
        for (int i = 1; i < nVerts; i++) {
            Point  vi = vertices.get(i);
            Point  vj = vertices.get((i + 1) % nVerts);

            // If P coincides with a vertex → exclude
            if (P.equals(vi)) return null;

            double s;
            try {
                Vector edge = vj.subtract(vi);
                Vector toP  = P.subtract(vi);
                s = alignZero(n.dotProduct(edge.crossProduct(toP)));
            } catch (IllegalArgumentException ex) {
                // zero vector → P colinear with edge (on edge) or equals a vertex
                return null;
            }

            if (isZero(s)) return null;            // on edge/vertex → exclude
            if ((s > 0) != positive) return null;  // sign changed → outside polygon
        }

        // Inside → return the same single-point list we got from the plane
        return planeHits;
    }



}
