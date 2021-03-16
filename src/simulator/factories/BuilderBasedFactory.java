package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class BuilderBasedFactory<T> implements Factory<T> {

	private List<Builder<T>> listBuilder;
	private List<JSONObject> factoryElem;//Lista de JSONObject (plantillas con valores por defectos)
	
	public BuilderBasedFactory(List <Builder<T>> builders) {
		listBuilder = new ArrayList<>(builders); //Invoca construtor por copia
		
		for(Builder<T> b : listBuilder) {//Inicializa otra lista
			factoryElem.add(b.getBuilderInfo());
		}
	}
	@Override
	public T createInstance(JSONObject info) {
		if(info == null) {
			throw new IllegalArgumentException("Valor inv¨¢lido para createInstance");
		}
		for(Builder<T> b : listBuilder) {
			if(b.createInstance(info) != null) {
				return b.createInstance(info);
			}
		}
		return null;
	}

	@Override
	public List<JSONObject> getInfo() {
		return factoryElem; //Se devuelve la lista con todas las informaciones(JSONObject) de Builders
	}

}
