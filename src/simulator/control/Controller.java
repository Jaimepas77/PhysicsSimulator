package simulator.control;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Body;
import simulator.model.ForceLaws;
import simulator.model.PhysicsSimulator;
import simulator.model.SimulatorObserver;

public class Controller {
	
	private PhysicsSimulator simulator;
	private Factory<Body> bodyFactory;
	private Factory<ForceLaws> forceLawsFactory;
	
	public Controller(PhysicsSimulator simulator, Factory<Body> bodyFactory, Factory<ForceLaws> forceLawsFactory) {
		this.simulator = simulator;
		this.bodyFactory = bodyFactory;
		this.forceLawsFactory = forceLawsFactory;
	}

	public void loadBodies(InputStream in) {//Iniciar los cuerpos 
		JSONObject jsin = new JSONObject(new JSONTokener(in));//Convertir a JSONObject
		
		JSONArray bodies = jsin.getJSONArray("bodies");//Se supone que lo que lleva es un JSONArrays cuyos elementos son JSONObjects de bodies.
		
		for(int i = 0; i< bodies.length(); i++) {
			simulator.addBody(bodyFactory.createInstance(bodies.getJSONObject(i)));
		}
	}
	
	public void reset() {
		simulator.reset();
	}
	
	public void setStepTime(double dt) {//setDeltaTime en el enunciado
		simulator.setStepTime(dt);
	}
	
	public void setForceLaws(JSONObject info) {
		ForceLaws law = forceLawsFactory.createInstance(info);//Se crea una nueva ley con info
		simulator.setLaw(law);
	}
	
	public void addObserver(SimulatorObserver so) {
		simulator.addObserver(so);
	}
	
	public List<JSONObject>getForceLawsInfo(){
		return forceLawsFactory.getInfo();
	}
	
	public void run(int steps, OutputStream out, InputStream expOut, StateComparator cmp) throws JSONException, NotEqualStatesException  {//Ejecuta el simulador n veces, comparando el estado con lo esperado cada vez. 
		
		new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				//No escribimos nada en la consola...
			};
		};

		JSONObject JSONCmpS = null;//Para comparar nuestro salida con los esperados
		PrintStream p = new PrintStream(out);//Salida
		
		if(expOut != null) {
			JSONCmpS = new JSONObject(new JSONTokener(expOut));//JSON con la salida esperada
		}
		
		p.println("{");
		p.println("\"states\": [");
		
		//Comparar Estado inicial
		JSONObject stateIni = simulator.getState();//Salida inicial obtenida
		p.println(stateIni);
		
		JSONObject stateCmp = new JSONObject();//Estado esperado
		
		if(JSONCmpS != null) {
			stateCmp = JSONCmpS.getJSONArray("states").getJSONObject(0);//Salida inicial esperada
			
			if(!cmp.equal(stateIni, stateCmp)) {//si la salida no es la esperada...
				//Lanzar expepcion que sirve para las pruebas
				throw new NotEqualStatesException(stateIni, stateCmp, steps); 
			}
		}
	
		//Ejecutar los estados restantes
		for(int i = 1; i <= steps ; i++) {
			simulator.advance();
			
			p.print(",");
			JSONObject stateAct = simulator.getState();//Salida actual
			p.println(stateAct);
			
			if(JSONCmpS != null) {
				stateCmp = JSONCmpS.getJSONArray("states").getJSONObject(i);//Se actualiza la salida esperada en el paso actual
				
				if(!cmp.equal(stateAct, stateCmp)) {
					throw new NotEqualStatesException(stateAct, stateCmp, steps); 
				}
			}
		}
		
		p.println("]");
		p.println("}");
	
	}

	public void run(int n) {
		//Ejecutar n pasos de la simulacion
		for(int i = 1; i <= n ; i++) {
			simulator.advance();
		}
	}
	
}
