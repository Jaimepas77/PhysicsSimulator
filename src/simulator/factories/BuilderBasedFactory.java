package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class BuilderBasedFactory<T> implements Factory<T> {

	private List<Builder<T>> listBuilder;
	private List<JSONObject> factoryElem;//Lista de JSONObject (plantillas con valores por defectos)
	
	public BuilderBasedFactory(List <Builder<T>> builders) {
		listBuilder = new ArrayList<Builder<T>>(builders);
		
		factoryElem = new ArrayList<JSONObject>();
		for(Builder<T> b : listBuilder) {
			factoryElem.add(b.getBuilderInfo());
		}
	}
	@Override
	public T createInstance(JSONObject info) {
		if(info == null) {
			throw new IllegalArgumentException("Valor invalido para createInstance");
		}
		
		for(Builder<T> b : listBuilder) {
			if(b.createInstance(info) != null) {
				return b.createInstance(info);
			}
		}

		throw new IllegalArgumentException("Formato JSON incorrecto (no es compatible con ninguno de los tipos)");
	}

	@Override
	public List<JSONObject> getInfo() {
		return factoryElem; //Se devuelve la lista con todas las informaciones(JSONObject) de Builders
	}

}
