	package simulator.factories;


import org.json.JSONException;
import org.json.JSONObject;

public abstract class Builder <T>  {

	protected String type;//tipo definido para "contructor"
	protected String desc; //Descripci¨®n
	
	public Builder() {};
	
	public Builder(String t ,String d) {//Para luego iniciliar los atributos en los contructores de "contructores".
		type = t;
		desc = d;
	};
	
	public T createInstance(JSONObject info) {//Revisar 
		/*JSONObject template = getBuilderInfo();
		//Try catch de Jsonexception 
		if(!info.has("Type") || !info.has("data") ||info.getString("type") != this.type)//Si no hay claves  o type no corresponde al "contructor"
		{
			return null;
		}
		for(String key : template.getJSONObject("data").keySet())//Si falta algun parametro en date
		{
			if(!info.has(key))
			{
				//return null;
				throw new IllegalArgumentException("Datos erroneos en la creacion del objeto...");
			}
		}*/
		if(type == null || !type.equals(info.getString("type")))//Basado en las diapositivas
			return null;
		try{
			return (T)createTheInstance(info.getJSONObject("data"));//Se crea la instancia con data.Ya que hemos reconocido el tipo
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
	
	protected abstract T createTheInstance(JSONObject data);//Debe lazar JSONEXception.
	
}
