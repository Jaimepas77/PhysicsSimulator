package simulator.control;

import org.json.JSONArray;
import org.json.JSONObject;

public class MassEqualStates implements StateComparator{

    public MassEqualStates(){};
	public boolean equal(JSONObject s1, JSONObject s2){
		if(s1.getDouble("time") != s2.getDouble("time"))
			return false;

		JSONArray ja1 = s1.getJSONArray("bodies");
		JSONArray ja2 = s2.getJSONArray("bodies");
		if(ja1.length() != ja2.length())
			return false;

		for (int i = 0; i < ja1.length(); i++) {
			if(ja1.getJSONObject(i).getString("id") != ja2.getJSONObject(i).getString("id"))
				return false;
			if(ja1.getJSONObject(i).getDouble("m") != ja2.getJSONObject(i).getDouble("m"))
				return false;
		}

		return true;
	}
}