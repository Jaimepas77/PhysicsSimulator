	package simulator.factories;


import org.json.JSONException;
import org.json.JSONObject;

public abstract class Builder <T>  {

	protected String type;
	protected String desc;
	
	public Builder() {};
	
	public Builder(String type, String desc) {
		this.type = type;
		this.desc = desc;
	};
	
	public T createInstance(JSONObject info) {
		if(type == null || !type.equals(info.getString("type")))
			return null;
		try{
			return (T)createTheInstance(info.getJSONObject("data"));
		}
		catch(JSONException e){
			throw new IllegalArgumentException("Datos erroneos en la creacion del objeto...");
		}
	}
	
	public JSONObject getBuilderInfo() {//Construccion de una plantilla.
		JSONObject info = new JSONObject();
		info.put("type", type);
		info.put("data", createData());
		info.put("desc", desc);
		return info;
	}
	
	protected JSONObject createData() {
		//Por defecto, se intrepreta como sin parametro para plantilla (No tiene claves). 
		return new JSONObject();
	}
	
	protected abstract T createTheInstance(JSONObject data);
	
}
