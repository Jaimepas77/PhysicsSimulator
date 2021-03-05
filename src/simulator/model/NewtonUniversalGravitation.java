package simulator.model;

import java.util.List;

import simulator.misc.Vector2D;

public class NewtonUniversalGravitation implements ForceLaws {
	
	//Constante gravitatoria 
	public static final double GravitationalConstant = 6.67e-11;
	
	//Constante gravitatoria autodefinada
	private double G;
	
	
	public NewtonUniversalGravitation(double G) {
		//Se define la constante gravitaria en lugar de constante definido por defecto
		this.G = G;
		
	}
	
	//Contructor por defecto , posiblemente se usa cuando implementa las factorias o no.
	public NewtonUniversalGravitation() {
		this(GravitationalConstant);
	}
	
	//
	public void apply(List<Body> bs) {
		
		for(Body bodyi : bs) {//Cuerpo Bi, quien "aplicado" la fuerca
			for(Body bodyj : bs)//Cuerpos Bj , quienes "aplican" la fuerza
			{
				//Sumatorio a lo bruto
				//Habria que ver como comparar los cuerpos
				if(bodyi != bodyj) {
				double force = G * bodyi.getMass() * bodyj.getMass() / Math.pow(bodyj.getPosition().distanceTo(bodyi.getPosition()),2); //Fuerza
				Vector2D dir = bodyj.getPosition().direction().minus(bodyi.getPosition().direction()) ;//Direccion pj - pi
				Vector2D f = dir.scale(force);//Vector de fuerza
				bodyi.addForce(f);
				
				//Creo que se puede implementar mejor sabiendo que f se actua uno a otro variando la dirrecion.
				//el primer for(int i = 0; .........) 
				//el segundo for (int i = j + 1; i < body.size(); i++)
				//......
				//Bodyj.addForce(f.scale(-1))
				}
			}
		}
		
	}
	
	public String toSring() { //Actual desconoce el uso
		return null;
	}
	
}
