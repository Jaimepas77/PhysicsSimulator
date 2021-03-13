package simulator.factories;

import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.Body;
import simulator.model.MassLossingBody;

public class MassLossingBodyBuilder extends Builder<Body> {

	
	MassLossingBodyBuilder(){
		super("mlb",
				"Mass Losing Body");
	}
	@Override
	protected Body createTheInstance(JSONObject data) {
		String id = data.getString("id");
		double m = data.getDouble("m");
		Vector2D v = new Vector2D(data.getJSONArray("v").getDouble(0),data.getJSONArray("v").getDouble(1));
		Vector2D p = new Vector2D(data.getJSONArray("p").getDouble(0),data.getJSONArray("p").getDouble(1));
		double freq = data.getDouble("freq");
		double factor = data.getDouble("factor");
		return new MassLossingBody(id,v,p,m,factor,freq);
	}
	
	protected JSONObject createData() {
		JSONObject data = new JSONObject();
		//Plantilla
		Vector2D p = new Vector2D(-3.5e10,0.0e00);
		Vector2D v = new Vector2D(0.0e00,1.4e03);
		data.put("id", "b1");
		data.put("p", p.asJSONArray());
		data.put("v", v.asJSONArray());
		data.put("m", 5.97e24);
		data.put("freq", 1e3);
		data.put("factor", 1e-3);
		return data;
		
	}

}
