package simulator.factories;

import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.ForceLaws;
import simulator.model.MovingTowardsFixedPoint;

public class MovingTowardsFixedPointBuilder  extends Builder<ForceLaws> {

	public MovingTowardsFixedPointBuilder() {
		super("mtcp",
			  "Moving a Fixed Point");
	}

	@Override
	protected ForceLaws createTheInstance(JSONObject data) {
		
		//Aqui se intrepreta que "constructor" se define el valor por defector y no clase la define.
		//Y asi bastaria un constructor de clase , que es la estandar.
		double g = data.has("g") ? data.getDouble("g") : 9.81;
		Vector2D c;
		
		if(data.has("c")){
			 c = new Vector2D(data.getJSONArray("c").getDouble(0),data.getJSONArray("c").getDouble(1));
		}
		else c = new Vector2D();
		
		return new  MovingTowardsFixedPoint(c, g);
	}

	protected JSONObject createData() {
		JSONObject data = new JSONObject();
		Vector2D c = new Vector2D(0,0);
		data.put("c", c.asJSONArray());
		data.put("g", 9.81);
		return data;
	}
}
