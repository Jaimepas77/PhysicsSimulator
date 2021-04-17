package simulator.model;

import java.util.List;

public class NoForce implements ForceLaws {

	public NoForce() {}
	
	public void apply(List<Body> bs) {
		//Los cuerpos se mueven con una aceleracion fija
	}
	
	@Override
	public String toString() {
		return "No Force";
	}
}
