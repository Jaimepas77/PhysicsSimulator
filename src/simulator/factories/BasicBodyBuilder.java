package simulator.factories;

import org.json.JSONException;
import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.Body;

public class BasicBodyBuilder extends Builder<Body>{

	
	public BasicBodyBuilder() {
		super("basic",
			  "Default Body");
	}
	@Override
	protected Body createTheInstance(JSONObject data) {
		
		String id = data.getString("id");
		double m = data.getDouble("m");
		Vector2D v = new Vector2D(data.getJSONArray("v").getDouble(0),data.getJSONArray("v").getDouble(1));
		Vector2D p = new Vector2D(data.getJSONArray("p").getDouble(0),data.getJSONArray("p").getDouble(1));
		return new Body(id,v,p,m);
		
	}
	
	protected JSONObject createData() {
		
		JSONObject data = new JSONObject();
		//Plantilla
		Vector2D p = new Vector2D(0.0e00,0.0e00);
		Vector2D v = new Vector2D(0.5e04,0.0e00);
		
		data.put("id", "b1");
		data.put("p", p.asJSONArray());
		data.put("v", v.asJSONArray());
		data.put("m", 5.97e24);
		return data;
		
	}

}
