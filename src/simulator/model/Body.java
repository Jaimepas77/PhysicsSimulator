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
	
	//Constructor
	public Body(String id, Vector2D velocity, Vector2D position, double mass) {
		this.id = id;
		this.velocity = velocity;
		resetForce();
		this.position = position;
		this.mass = mass;
	}
	
	//Getters
	public String getId() {
		return id;
	}
	public Vector2D getVelocity() {
		return new Vector2D(velocity);
	}
	public Vector2D getForce() {
		return new Vector2D(force);
	}
	public Vector2D getPosition() {
		return new Vector2D(position);
	}
	
	public double getMass() {
		return mass;
	}
	
	@Override
	public boolean equals(Object obj){
		Body other = (Body)obj;
		return this.id.equals(other.id);
	}
	
	//Metodos a implementar
	protected void addForce(Vector2D f){
		force = force.plus(f); //El metodos plus devuelve un Vector cuyo componentes son sumas de componentes de f y force.
	}
	
	protected void resetForce() {
		force = new Vector2D(0, 0);//Podemos poner tambien Vector2D() para el mismo efecto,
	}
	
	protected void move(double t) {
		Vector2D a; //Vector aceleracion
		if(mass != 0) {
			a = force.scale(1.0/mass);
		}
		else {
			a = new Vector2D(0, 0);
		}
		
		//Formulas
		position = position.plus(velocity.scale(t).plus(a.scale(0.5 * t * t)) );
		velocity = velocity.plus(a.scale(t));
	}
	
	public JSONObject getState() {
		
		JSONObject js = new JSONObject();
		js.put("id", id);
		js.put("m", mass);
		js.put("p", position.asJSONArray());
		js.put("v", velocity.asJSONArray());
		js.put("f", force.asJSONArray());
		return js;
		
	}
	
	public String toString() {
		return getState().toString();
	}

}
