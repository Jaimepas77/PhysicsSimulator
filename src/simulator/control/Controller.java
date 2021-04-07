package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Body;
import simulator.model.PhysicsSimulator;

public class Controller {
	
	private PhysicsSimulator simulator;
	private Factory<Body> bodyFactory;
	
	public Controller(PhysicsSimulator simulator, Factory<Body> bodyFactory) {
		this.simulator = simulator;
		this.bodyFactory = bodyFactory;
	}

	public void loadBodies(InputStream in) {//Iniciar los cuerpos 
		JSONObject jsin = new JSONObject(new JSONTokener(in));//Convertir a JSONObject
		
		JSONArray bodies = jsin.getJSONArray("bodies");//Se supone lo que lleva es un JSONArryas cuyo elementos son JSONObject de bodies
		
		for(int i = 0; i< bodies.length(); i++) {
			simulator.addBody(bodyFactory.createInstance(bodies.getJSONObject(i)));
		}
		
	}
	
	public void run(int steps, OutputStream out, InputStream expOut, StateComparator cmp) throws JSONException, NotEqualStatesException  {//Ejecuta el simulador n veces, comparando el estado con lo esperado cada vez. 
		
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
				//Lanzar expecion que sirve para las pruebas
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
	
}
