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
	private  Factory<Body> BodyFactory;
	Controller(PhysicsSimulator p , Factory<Body> f) {
		simulator = p;
		BodyFactory = f;
	}

	public void loadBies(InputStream in) {//Iniciar los cuerpos 
		JSONObject jsin = new JSONObject(new JSONTokener(in));//Convertir a JSONObject
		
		JSONArray bodies = jsin.getJSONArray("Bodies");//Se supone lo que lleva es un JSONArryas cuyo elementos son JSONObject de bodys
		
		for(int i = 0 ; i< bodies.length();i++) {
			simulator.addBody(createBody(bodies.getJSONObject(i)));
		}
		
	}
	
	public void run(int n ,OutputStream out , InputStream expOut ,StateComparator cmp) {//Ejecuta el simulador n veces , comparando el estado con lo esperado cada vez. 
		
		JSONObject JSONCmpS = null;//Para comparar nuestro salida con los esperados
		PrintStream p = new PrintStream(out);//Salida
		
		if(expOut != null) {
			JSONCmpS =new JSONObject(new JSONTokener(expOut));
		}
		
		p.println("{");
		p.println("\"states\":[");
		//Comparar Estado inicial
		JSONObject stateini = simulator.getState();
		p.println(stateini);
		JSONObject JSONcmp = JSONCmpS.getJSONArray("states").getJSONObject(0);
		if(JSONCmpS != null) {
			if(cmp.equal(stateini, JSONcmp)) {
				//throw new NotEqualStatesException(state,statecmp,0) hay que crear
			}
		}
		//Comparar los estados restantes
		for(int i = 1; i <= n ; i++) {
			simulator.advance();
			JSONObject state = simulator.getState();
			p.println(state);
			JSONObject statecmp = JSONCmpS.getJSONArray("states").getJSONObject(i);
			if(JSONCmpS != null) {
				if(cmp.equal(state, statecmp)) {
					//throw new NotEqualStatesException(state,statecmp,n) hay que crear
				}
			}
		}
		p.println("]");
		p.println("}");
	
	}
	
	private Body createBody(JSONObject info){
		
		return BodyFactory.createInstance(info);
		
	}
	
	
}
