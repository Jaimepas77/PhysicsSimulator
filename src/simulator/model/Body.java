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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Body other = (Body) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
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
			a = force.scale(1.0/mass); // Multiplicamos para dividir .
		}
		else {
			a = new Vector2D(0,0);
		}
		
		//Formulas
		position = position.plus(velocity.scale(t).plus(a.scale(0.5 * t * t)) );//Interpreto que v, se refiere a Vini.
		velocity = velocity.plus(a.scale(t));
	}
	
	public JSONObject getState() {
		
		//Viendo Create Example de JSONObject
		// build a JSONObject from a string en este caso es complicado
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
