package simulator.model;

import java.util.List;

import simulator.misc.Vector2D;

public class MovingTowardsFixedPoint implements ForceLaws {
	
	//Valores para definir
	private Vector2D centerOfUniverse; // Refencia del centro
	private double gDef;
	
	public MovingTowardsFixedPoint(Vector2D c, double g) {
		centerOfUniverse = c;
		gDef = g;
	}

	public void apply(List<Body> bodies) {
		for(Body body : bodies){
			body.addForce(centerOfUniverse.minus(body.getPosition()).direction().scale(gDef * body.getMass()) );
		}
	}
	
	@Override
	public String toString() {
		return "Moving towards " + centerOfUniverse + " with constant acceleration " + gDef;
	}

}
