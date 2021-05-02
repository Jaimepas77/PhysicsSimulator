package simulator.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class PhysicsSimulator {
	private double stepTime; //Tiempo real por paso (delta time)
	private ForceLaws law; //Ley gravitacional a aplicar
	private List<Body> bodies; //Lista de cuerpos existentes en la simulacion
	private double actualTime; //Momento de la simulacion en el que nos encontramos
	private List<SimulatorObserver> observers;//Lista de observadores
	
	//Constructor
	public PhysicsSimulator(double realTime, ForceLaws law)
	{
		bodies = new ArrayList<>();
		
		observers = new ArrayList<>();
		setStepTime(realTime); //Reutilizando el codigo
		
		setLaw(law);
		
		actualTime = 0.0;
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
		
		for(SimulatorObserver so : observers) //Se envia a todos los observadores
		{
			so.onAdvance(bodies, actualTime);;
		}
	}
	
	public void addBody(Body b)
	{
		if(bodies.contains(b))
				throw new IllegalArgumentException("El cuerpo ya existe.");
		bodies.add(b);
		
		for(SimulatorObserver so : observers) //Se envia a todos los observadores
		{
			so.onBodyAdded(bodies, b);
		}
	}
	
	public void addObserver(SimulatorObserver so)
	{
		if(observers.contains(so))
		{
			throw new IllegalArgumentException("El observador ya existe.");
		}
		else
		{
			observers.add(so);
			//observers.get(observers.size()-1).onRegister(bodies, actualTime, stepTime, law.toString());//Se envia al observador que acabamos de aniadir
			so.onRegister(bodies, actualTime, stepTime, law.toString());//¿Esto no sería equivalente y más legible que la opción anterior?
		}
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
	
	public void reset() 
	{
		actualTime = 0.0;
		bodies.clear();//Vaciar la lista
		
		for(SimulatorObserver so : observers) //Se envia a todos los observadores
		{
			so.onReset(bodies, actualTime, stepTime, law.toString());
		}
	}
	
	public void setStepTime(double dt)//Cambia el tiempo real por paso (delta time)
	{
		if(dt > 0.0)
		{
			this.stepTime = dt;
			for (SimulatorObserver so : observers) // Se envia a todos los observadores
			{
				so.onDeltaTimeChanged(stepTime);
			}
		} 
		else 
		{
			throw new IllegalArgumentException("Tiempo no valido.");
		}
	}
	
	public void setLaw(ForceLaws forceLaws)//Cambia la ley gravitacional
	{
		if(forceLaws != null) 
		{
			this.law = forceLaws;
			for(SimulatorObserver so : observers) //Se envia a todos los observadores
			{
				so.onForceLawsChanged(law.toString());
			}
		}
		else
		{
			throw new IllegalArgumentException("Ley de fuerza no valida.");
		}
	}
}


