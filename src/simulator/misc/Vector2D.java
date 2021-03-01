package simulator.misc;

import org.json.JSONArray;

public class Vector2D {

	double _x;
	double _y;

	// create the zero vector
	public Vector2D() {
		_x = _y = 0.0;
	}

	// copy constructor
	public Vector2D(Vector2D v) {
		_x = v._x;
		_y = v._y;
	}

	// create a vector from an array
	public Vector2D(double x, double y) {
		_x = x;
		_y = y;
	}

	// return the inner product of this Vector a and b
	public double dot(Vector2D that) {
		return _x * that._x + _y * that._y;
	}

	// return the length of the vector
	public double magnitude() {
		return Math.sqrt(dot(this));
	}

	// return the distance between this and that
	public double distanceTo(Vector2D that) {
		return minus(that).magnitude();
	}

	// create and return a new object whose value is (this + that)
	public Vector2D plus(Vector2D that) {
		return new Vector2D(_x + that._x, _y + that._y);
	}

	// create and return a new object whose value is (this - that)
	public Vector2D minus(Vector2D that) {
		return new Vector2D(_x - that._x, _y - that._y);
	}

	// return the corresponding coordinate
	public double getX() {
		return _x;
	}

	public double getY() {
		return _y;
	}

	// create and return a new object whose value is (this * factor)
	public Vector2D scale(double factor) {
		return new Vector2D(_x * factor, _y * factor);
	}

	// return the corresponding unit vector
	public Vector2D direction() {
		if (magnitude() > 0.0)
			return scale(1.0 / magnitude());
		else
			return new Vector2D(this);
	}

	public JSONArray asJSONArray() {
		JSONArray a = new JSONArray();
		a.put(_x);
		a.put(_y);
		return a;
	}

	// return a string representation of the vector
	public String toString() {
		return "[" + _x + "," + _y + "]";
	}

}
