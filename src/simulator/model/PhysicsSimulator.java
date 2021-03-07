package simulator.model;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class PhysicsSimulator {
	double stepTime; //Tiempo real por paso
	ForceLaws law; //Ley gravitacional a aplicar
	List<Body> bodies; //Lista de cuerpos existentes en la simulaci�n
	double actualTime; //Momento de la simulaci�n en el que nos encontramos
	
	//Constructor
	public PhysicsSimulator(double realTime, ForceLaws law)
	{
		if(realTime > 0.0)
			this.stepTime = realTime;
		else
			throw new IllegalArgumentException("Tiempo no v�lido");
		
		if(law != null)
			this.law = law;
		else
			throw new IllegalArgumentException("Ley de fuerza no v�lida");
		
		
		actualTime = 0.0;
	}
	
	//M�todos
	public void advance()
	{
		for(Body x : bodies)
			x.resetForce();
		
		law.apply(bodies);
		
		for(Body x: bodies)
			x.move(stepTime);
		
		actualTime += stepTime;
	}
	
	public void addBody(Body b)
	{
		for(Body x : bodies)
			if(x == b)
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
}

