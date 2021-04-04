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
		
		JSONArray bodies = jsin.getJSONArray("bodies");//Se supone lo que lleva es un JSONArryas cuyo elementos son JSONObject de bodys
		
		for(int i = 0; i< bodies.length(); i++) {
			simulator.addBody(createBody(bodies.getJSONObject(i)));
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
		p.print(",");//Coma raro de la salida esperada
		
		JSONObject stateCmp = new JSONObject();//Estado esperdo
		
		if(JSONCmpS != null) {
			stateCmp = JSONCmpS.getJSONArray("states").getJSONObject(0);//Salida inicial esperada
			
			if(!cmp.equal(stateIni, stateCmp)) {//si la salida no es la esperada...
				//Se obtiene la posicion de primer body que muestra la desigualdad con la esperada.Nos podria facilitar con esa informacion(opcional-recomentada por profe)
				int bodyDiference = positionFirstBodyDiference(stateIni ,stateCmp , cmp);
				//Lanzar expecion que sirve para las pruebas
				throw new NotEqualStatesException(stateIni, stateCmp,stateIni.getJSONArray("bodies").getJSONObject(bodyDiference),stateCmp.getJSONArray("bodies").getJSONObject(bodyDiference), steps); 
			}
		}
	
		//Ejecutar los estados restantes
		for(int i = 1; i <= steps ; i++) {
			simulator.advance();
			
			JSONObject stateAct = simulator.getState();//Salida actual
			p.println(stateAct);
			
			if(i != steps) {//Coma raro de la salida esperada
				p.print(",");
			}
			
			if(JSONCmpS != null) {
				stateCmp = JSONCmpS.getJSONArray("states").getJSONObject(i);//Se actualiza la salida esperada en el paso actual
				
				if(!cmp.equal(stateAct, stateCmp)) {
					int bodyDiference = positionFirstBodyDiference(stateAct ,stateCmp , cmp);
					
					throw new NotEqualStatesException(stateAct, stateCmp,stateAct.getJSONArray("bodies").getJSONObject(bodyDiference),stateCmp.getJSONArray("bodies").getJSONObject(bodyDiference), steps); 
				}
			}
		}
		
		p.println("]");
		p.println("}");
	
	}
	
	private Body createBody(JSONObject info){
		return bodyFactory.createInstance(info);
	}
	
	private int positionFirstBodyDiference(JSONObject stateAct ,JSONObject stateCmp , StateComparator cmp) {//Revisar
		int j = 0;
		while(j < stateAct.getJSONArray("bodies").length()) {//Ambos debe tener el mismo numero de cuerpos
			//Body que tenemos (un)
			JSONArray actBodies = new JSONArray();
			actBodies.put(stateAct.getJSONArray("bodies").get(j));
			
			//Body esperado (un)
			JSONArray expectedBodies = new JSONArray();
			expectedBodies.put(stateCmp.getJSONArray("bodies").get(j));
			
			//Pasamos a JSONObject para poder usar comparatores
			JSONObject stateActAux = new JSONObject();
			stateActAux.put("bodies", actBodies);
			stateActAux.put("time" ,stateAct.getDouble("time"));
			
			JSONObject stateCmpAux = new JSONObject();
			stateCmpAux.put("bodies", expectedBodies);
			stateCmpAux.put("time" ,stateCmp.getDouble("time"));
			
			if(!cmp.equal(stateActAux, stateCmpAux))//Se compara los cuerpos uno a uno
				return j;
			else {
				j++;
			}
		}
		return 0;//No siempre la desigualdad se debe a los cuerpos
		
	}
	
	
}
