package simulator.factories;


import org.json.JSONObject;

public abstract class Builder <T>  {

	protected String type;//tipo definido para "contructor" , razonando que es mejor saber el propio fabrica el tipo que fabrica
	
	public Builder() {};
	
	public Builder(String t) {type = t;};//Si usamos atributo.....y iniciliamos luego en los contructores de "contructores".
	
	public T createInstance(JSONObject info) {
		JSONObject template = getBuilderInfo();
		//Try catch de Jsonexception 
		if(!info.has("Type") || !info.has("data") ||info.getString("type") != this.type)//Si no hay claves  o type no corresponde al "contructor"
		{
			return null;
		}
		for(String key : template.getJSONObject("data").keySet())//Si falta algun parametro en date
		{
			if(!info.has(key))
			{
				return null;
			}
		}
		return createTheInstance(info);//Se crea la instancia con info
		
	}
	
	public JSONObject getBuilderInfo() {//Construccion de una plantilla.
		JSONObject info = new JSONObject();
		info.put("type", type);
		info.put("data", createData());
		return info;
		
	}
	protected JSONObject createData() {
		JSONObject data = new JSONObject();//Por defecto ,se intrepreta como sin parametro para plantilla.. !!No tiene claves 
		return data;
	}
	
	protected abstract T createTheInstance(JSONObject info) ;//Debe lazar JSONEXception?
	

}
