package simulator.control;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector2D;

public class EpsilonEqualStates implements StateComparator {
	
	private double eps;

	public EpsilonEqualStates(){
		this.eps = 0;
	}
	
	public EpsilonEqualStates(double eps){
		this.eps = eps; 
	}

	public boolean equal(JSONObject s1, JSONObject s2) {
		
		if(s1.getDouble("time") != s2.getDouble("time"))
			return false;
		
		JSONArray ja1 = s1.getJSONArray("bodies");
		JSONArray ja2 = s2.getJSONArray("bodies");
		if(ja1.length() != ja2.length())//Revisar que tienen el mismo numerode cuerpos
			return false;

		for (int i = 0; i < ja1.length(); i++) {//Revisar cada cuerpo con su correspondiente
			if(!ja1.getJSONObject(i).getString("id").equals(ja2.getJSONObject(i).getString("id"))) 
				return false;
			
			double m1 = ja1.getJSONObject(i).getDouble("m"), m2 = ja2.getJSONObject(i).getDouble("m");
			Vector2D p1 = new Vector2D(ja1.getJSONObject(i).getJSONArray("p").getDouble(0), ja1.getJSONObject(i).getJSONArray("p").getDouble(1));
			Vector2D v1 = new Vector2D(ja1.getJSONObject(i).getJSONArray("v").getDouble(0), ja1.getJSONObject(i).getJSONArray("v").getDouble(1));
			Vector2D f1 = new Vector2D(ja1.getJSONObject(i).getJSONArray("f").getDouble(0), ja1.getJSONObject(i).getJSONArray("f").getDouble(1));
			Vector2D p2 = new Vector2D(ja2.getJSONObject(i).getJSONArray("p").getDouble(0), ja2.getJSONObject(i).getJSONArray("p").getDouble(1));
			Vector2D v2 = new Vector2D(ja2.getJSONObject(i).getJSONArray("v").getDouble(0), ja2.getJSONObject(i).getJSONArray("v").getDouble(1));
			Vector2D f2 = new Vector2D(ja2.getJSONObject(i).getJSONArray("f").getDouble(0), ja2.getJSONObject(i).getJSONArray("f").getDouble(1));
			
			if(!(Math.abs(m1-m2) <= eps && p1.distanceTo(p2) <= eps && v1.distanceTo(v2) <= eps && f1.distanceTo(f2) <= eps ))
				return false;
			
		}
		
		return true;
	}
}
