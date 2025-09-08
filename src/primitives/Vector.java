package primitives;

public class Vector extends Point {

    public static Vector AXIS_X = new Vector(1, 0, 0);
    public static Vector AXIS_Y = new Vector(0, 1, 0);
    public static Vector AXIS_Z = new Vector(0, 0, 1);

    /** Ctor from three coordinates. Throws if zero vector. */
    public Vector(double x, double y, double z) {
        super(x, y, z); // calling the point ctor
        if (xyz.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("Zero vector is not allowed");
        }
    }

    /** Ctor from Double3. Throws if zero vector. */
    public Vector(Double3 xyz) {
        super(xyz); // calling the point ctor
        if (xyz.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("Zero vector is not allowed");
        }
    }

    /** Vector + Vector -> Vector (component-wise). */
    public Vector add(Vector v) {
        return new Vector(this.xyz.add(v.xyz));
    }

    /** k * Vector -> Vector (scalar multiplication). */
    public Vector scale(double k) {
        // If k == 0 this will create a zero vector and the ctor will throw, as required.
        return new Vector(this.xyz.scale(k));
    }

    /** Dot product (scalar) between this and other. */
    public double dotProduct(Vector other) {
        return this.xyz.d1() * other.xyz.d1()
                + this.xyz.d2() * other.xyz.d2()
                + this.xyz.d3() * other.xyz.d3();
    }

    /** Cross product (vector) between this and other. */
    public Vector crossProduct(Vector other) {
        double x1 = this.xyz.d1(), y1 = this.xyz.d2(), z1 = this.xyz.d3();
        double x2 = other.xyz.d1(), y2 = other.xyz.d2(), z2 = other.xyz.d3();
        return new Vector(
                y1 * z2 - z1 * y2,
                z1 * x2 - x1 * z2,
                x1 * y2 - y1 * x2
        );
    }

    /// |v|^2
    public double lengthSquared() {
        return (xyz.d1() * xyz.d1()) + (xyz.d2() * xyz.d2()) + (xyz.d3() * xyz.d3());
    }

    /// |v|
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /** Returns a new unit vector in the same direction. */
    public Vector normalize() {
        double len = length();
        // len can't be 0 (zero vectors are forbidden), so just scale by 1/len
        return scale(1.0 / len);
    }

    /// Implement Object's equals() and toString().
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Vector other)
                && this.xyz.equals(other.xyz);
    }
    @Override
    public String toString() {
        return "Vector" + xyz.toString();
    }

}
