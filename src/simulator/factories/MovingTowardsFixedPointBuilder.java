package simulator.factories;

import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.ForceLaws;
import simulator.model.MovingTowardsFixedPoint;

public class MovingTowardsFixedPointBuilder  extends Builder<ForceLaws> {

	//Valores por defecto
	public static final double G = 9.81;
	
	public MovingTowardsFixedPointBuilder() {
		super("mtfp", "Moving towards a Fixed Point");
	}

	@Override
	protected ForceLaws createTheInstance(JSONObject data) {
		
		double g = data.has("g") ? data.getDouble("g") : G;
		Vector2D c;
		
		if(data.has("c")){
			 c = new Vector2D(data.getJSONArray("c").getDouble(0),data.getJSONArray("c").getDouble(1));
		}
		else c = new Vector2D();
		
		return new  MovingTowardsFixedPoint(c, g);
	}

	protected JSONObject createData() {
		JSONObject data = new JSONObject();
		data.put("c", "the point towards which bodies move (a json list of 2 numbers, e.g., [100.0,50.0])");
		data.put("g", "the length of the acceleration vector (a number)");
		return data;
	}
}
