package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.json.JSONArray;
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
		
		JSONArray bodies = jsin.getJSONArray("bodies");//Se supone lo que lleva es un JSONArryas cuyo elementos son JSONObject de bodys
		
		for(int i = 0; i< bodies.length(); i++) {
			simulator.addBody(createBody(bodies.getJSONObject(i)));
		}
		
	}
	
	public void run(int steps, OutputStream out, InputStream expOut, StateComparator cmp) {//Ejecuta el simulador n veces, comparando el estado con lo esperado cada vez. 
		
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
		
		JSONObject stateCmp = JSONCmpS.getJSONArray("states").getJSONObject(0);//Salida inicial esperada
		
		if(JSONCmpS != null && !cmp.equal(stateIni, stateCmp)) {//si la salida no es la esperada...
				//throw new NotEqualStatesException(stateIni, stateCmp, 0) hay que crear
		}
		
		//Ejecutar los estados restantes
		for(int i = 1; i <= steps ; i++) {
			simulator.advance();
			
			JSONObject stateAct = simulator.getState();//Salida actual
			p.println(stateAct);
			
			stateCmp = JSONCmpS.getJSONArray("states").getJSONObject(i);//Se actualiza la salida esperada en el paso actual
			
			if(JSONCmpS != null && !cmp.equal(stateAct, stateCmp)) {//si la salida no es la esperada...
				//throw new NotEqualStatesException(stateAct, stateCmp, i) hay que crear
			}
		}
		
		p.println("]");
		p.println("}");
	
	}
	
	private Body createBody(JSONObject info){
		return bodyFactory.createInstance(info);
	}
	
	
}
