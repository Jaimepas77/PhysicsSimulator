package simulator.model;

import java.util.List;

import simulator.misc.Vector2D;

public class MovingTowardsFixedPoint implements ForceLaws {
	
	//Valores por defecto
	public static final double g = 9.81;
	
	//Valores para definir
	private Vector2D centerOfUniverse; // is not used!!!
	private double  gDef;
	
	public MovingTowardsFixedPoint(Vector2D c, double g) {
		centerOfUniverse = c;
		gDef = g;
	}
	
	//Constructores por defectos posiblemente se usa cuando implementa las factorias o no.
	public MovingTowardsFixedPoint() {
		this(new Vector2D(),g);
	}

	public MovingTowardsFixedPoint(double g) {
		this(new Vector2D (0,0) , g);
	}
	
	public MovingTowardsFixedPoint(Vector2D c) {
		this(c,g);
	}
	
	
	public void apply(List<Body> bs) {
		for(Body body : bs){
			//F = m * a -----> F = m * -g * di;
			body.addForce(body.position.direction().scale(body.getMass()* gDef * (-1)));
		}
	}

}
