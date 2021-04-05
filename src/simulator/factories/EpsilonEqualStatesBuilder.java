package simulator.factories;

import org.json.JSONObject;

import simulator.control.EpsilonEqualStates;
import simulator.control.StateComparator;

public class EpsilonEqualStatesBuilder extends Builder<StateComparator> {

	//Valor por defecto
	public static final double EPSILON = 0.0;
	
	public  EpsilonEqualStatesBuilder(){
		super("epseq",
			 "Epsilon Equal Comparator");
	}
	@Override
	protected StateComparator createTheInstance(JSONObject data) {
		if(data.has("eps")){
			return new EpsilonEqualStates(data.getDouble("eps"));
		}
		return new EpsilonEqualStates(EPSILON);
	}
	
	protected JSONObject createData() {
		JSONObject data = new JSONObject();
		data.put("eps",0.0);
		return data;
		
	}

}
