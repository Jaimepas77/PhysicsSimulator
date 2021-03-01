package simulator.model;

import org.json.JSONObject;

import simulator.misc.Vector2D;

public class Body {
	
	//Atributos
	
	protected String id; //Identificador de cuerpo
	protected Vector2D velocity;//Vector de Velocidad
	protected Vector2D force; //Vector de Fuerza
	protected Vector2D position; //Vector de posicion
	protected double mass; //Masa del cuerpo
	
	public Body(String id, Vector2D velocity, Vector2D position, double mass) {
		this.id = id;
		this.velocity = velocity;
		this.position = position;
		this.mass = mass;
	}
	//Getters
	public String getId() {
		return id;
	}
	public Vector2D getVelocity() {
		return velocity;
	}
	public Vector2D getForce() {
		return force;
	}
	public Vector2D getPosition() {
		return position;
	}
	
	public double getMass() {
		return mass;
	}
	
	//Metodos a implementar
	
	protected void addForce(Vector2D f){
		force = force.plus(f); //El metodos plus devuelve un Vector cuyo componentes son sumas de componentes de f y force.
	}
	
	protected void resetForce() {
		force = new Vector2D(0,0);//Podemos poner tambien Vector2D() para el mismo efecto,
	}
	
	protected void move(double t) { //Importante a ser revisado
		Vector2D a; //Vector aceleracion
		if(mass != 0) {
			 a = force.scale(1/mass); // Multiplicamos para dividir .
		}
		else {
			a = new Vector2D(0,0);
		}
		
		//Formulas
		position = position.plus(velocity.scale(t)).plus(a.scale(t * t * (1/2))) ;//Interpreto que v ,se refiere a Vini.
		velocity = velocity.plus(a.scale(t));
	}
	
	public JSONObject getState() {
		
		//Viendo Create Example de JSONObject
		// build a JSONObject from a string en este caso es complicado
		JSONObject js = new JSONObject();
		js.put("id", id);
		js.put("m",mass);
		js.put("p",position);
		js.put("v",velocity);
		js.put("f", force);
		return js;
		
	}
	
	public String toString() {
		return getState().toString();
		
	}
	
	
	
	
}
