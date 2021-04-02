package simulator.model;

import java.util.List;

import simulator.misc.Vector2D;

public class NewtonUniversalGravitation implements ForceLaws {
	
	//Constante gravitatoria 
	public static final double GRAVITATIONAL_CONSTANT = 6.67e-11;
	
	//Constante gravitatoria autodefinida
	private double G;
	
	
	public NewtonUniversalGravitation(double G) {
		//Se define la constante gravitaria en lugar de la constante definida por defecto
		this.G = G;
		
	}
	
	//Contructor por defecto, posiblemente se usa cuando implementa las factorias o no.
	public NewtonUniversalGravitation() {
		this(GRAVITATIONAL_CONSTANT);
	}
	
	//
	public void apply(List<Body> bs) {
		
		for(Body bodyi : bs) {//Cuerpo Bi, quien "aplicado" la fuerca
			for(Body bodyj : bs)//Cuerpos Bj , quienes "aplican" la fuerza
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
		/*double force = G * a.getMass() * b.getMass() / Math.pow(b.getPosition().distanceTo(a.getPosition()), 2); //Fuerza
		Vector2D dir = b.getPosition().minus(a.getPosition());//calcular la direcci�n de (pj - pi) 
		dir = dir.direction();
		Vector2D f = dir.scale(force);//Vector de fuerza
		return f;*/
		Vector2D delta = b.getPosition().minus(a.getPosition());
	    double dist = delta.magnitude();
	    double magnitude = dist>0 ? (G * a.getMass() * b.getMass()) / (dist * dist) : 0.0;
	    return delta.direction().scale(magnitude);
	}
	
	public String toSring() { //Actual desconoce el uso
		return null;
	}
	
}
