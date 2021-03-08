package simulator.model;

import java.util.List;

public class NoForce implements ForceLaws {

	public NoForce() {}
	
	public void apply(List<Body> bs) {
		//Los cuerpos se mueven con una aceleraci�n fija
	}
	
	public String toSring() { //Actual desconoce el uso
		return null;
	}
}
