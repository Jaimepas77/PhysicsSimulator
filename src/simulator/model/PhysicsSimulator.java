package simulator.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class PhysicsSimulator {
	private double stepTime; //Tiempo real por paso
	private ForceLaws law; //Ley gravitacional a aplicar
	private List<Body> bodies; //Lista de cuerpos existentes en la simulacion
	private double actualTime; //Momento de la simulacion en el que nos encontramos
	
	//Constructor
	public PhysicsSimulator(double realTime, ForceLaws law)
	{
		setStepTime(realTime); //Reutilizando el codigo
		
		setLaw(law);
		
		actualTime = 0.0;
		
		bodies = new ArrayList<>();
	}
	
	//Metodos
	public void advance()
	{
		for(Body x : bodies)
			x.resetForce();
		
		law.apply(bodies);
		
		for(Body x : bodies)
			x.move(stepTime);
		
		actualTime += stepTime;
	}
	
	public void addBody(Body b)
	{
		if(bodies.contains(b))
				throw new IllegalArgumentException("El cuerpo ya existe.");
		bodies.add(b);
	}
	
	public JSONObject getState()
	{
		JSONObject simulator = new JSONObject();//Objeto JSON con el estado del simulador
		
		JSONArray bodies = new JSONArray();//Crear e inicializar una lista con los JSON de cada body
		for(Body x : this.bodies)
			bodies.put(x.getState());
		
		simulator.put("time", actualTime);
		simulator.put("bodies", bodies);
		
		return simulator;
	}
	
	public String toString()
	{
		return getState().toString();
	}
	
	public void reset() {
		actualTime = 0.0;
		bodies.clear();//Vaciar la lista
	}
	
	public void setStepTime(double dt) {//Cambia el tiempo real por paso
		if(dt > 0.0)
			this.stepTime = dt;
		else
			throw new IllegalArgumentException("Tiempo no valido.");
	}
	
	public void setLaw(ForceLaws forceLaws) {//Cambia la ley gravitacional
		if(forceLaws != null)
			this.law = forceLaws;
		else
			throw new IllegalArgumentException("Ley de fuerza no valida.");
	}
}


