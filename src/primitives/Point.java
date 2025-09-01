package primitives;

/** A 3D point represented by a Double3 triple. */
public class Point {
    final protected Double3 xyz;
    final public static Point ZERO = new Point(0, 0, 0);

    /** Ctor from three coordinates. */
    public Point(double x, double y, double z) {
        this.xyz = new Double3(x, y, z);
    }

    /** Ctor from a double3. */
    public Point(Double3 _xyz) {
        this.xyz = _xyz;
    }

    /** Vector from another point to this point (this - other). */
    public Vector subtract(Point other) {
        return new Vector(this.xyz.subtract(other.xyz));
    }

    /** Translate this point by a vector (this + v). */
    public Point add(Vector v) {
        return new Point(this.xyz.add(v.xyz));
    }

    /** Squared distance between two points - not using math.pow(). */
    public double distanceSquared(Point other) {
        double dx = this.xyz.d1() - other.xyz.d1();
        double dy = this.xyz.d2() - other.xyz.d2();
        double dz = this.xyz.d3() - other.xyz.d3();
        return dx * dx + dy * dy + dz * dz;
    }

    /** distance to another point. */
    public double distance(Point other) {
        return Math.sqrt(distanceSquared(other));
    }

    // Implement Object's equals() and toString().
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Point p)) return false;
        return this.xyz.equals(p.xyz);
    }

    @Override
    public String toString() {
        return "Point" + xyz.toString();
    }
}
