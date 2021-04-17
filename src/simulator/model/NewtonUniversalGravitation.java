package simulator.model;

import java.util.List;

import simulator.misc.Vector2D;

public class NewtonUniversalGravitation implements ForceLaws {
	
	//Constante gravitatoria autodefinida
	private double G;
	
	public NewtonUniversalGravitation(double G) {
		//Se define la constante gravitaria
		this.G = G;
	}
	
	public void apply(List<Body> bodies) {
		
		for(Body bodyi : bodies) {
			for(Body bodyj : bodies)
			{
				//Sumatorio
				if(bodyi != bodyj) {//Si no es el mismo
					Vector2D f = force(bodyi,bodyj);
					bodyi.addForce(f);
				}
			}
		}
	}
	
	private Vector2D force(Body a, Body b) {
		
		Vector2D delta = b.getPosition().minus(a.getPosition());
	    double dist = delta.magnitude();
	    double magnitude = dist > 0 ? (G * a.getMass() * b.getMass()) / (dist * dist) : 0.0;
	    return delta.direction().scale(magnitude);
	}
	
	@Override
	public String toString() {
		return "Newton's Universal Gravitation with G-" + G;
	}
	
}
