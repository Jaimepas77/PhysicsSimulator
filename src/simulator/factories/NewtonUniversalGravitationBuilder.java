package simulator.factories;

import org.json.JSONObject;

import simulator.model.ForceLaws;
import simulator.model.NewtonUniversalGravitation;

public class NewtonUniversalGravitationBuilder extends Builder<ForceLaws> {

	//Constante gravitatoria 
	public static final double GRAVITATIONAL_CONSTANT = 6.67e-11;
	
	public NewtonUniversalGravitationBuilder() {
		super("nlug", "Newton's law of universal gravitation");
	}
	@Override
	protected ForceLaws createTheInstance(JSONObject data) {

		if(data.has("G")){
			double G = data.getDouble("G");
			return new NewtonUniversalGravitation(G);	
		}
		return new NewtonUniversalGravitation(GRAVITATIONAL_CONSTANT);
	}
	
	protected JSONObject createData() {
		JSONObject data = new JSONObject();
		data.put("G", 6.67e-11);
		return data;
	}

}
