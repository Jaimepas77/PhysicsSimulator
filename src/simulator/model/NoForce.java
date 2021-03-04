package simulator.model;

import java.util.List;

public class NoForce implements ForceLaws {

	public NoForce() {
		//......
	}
	public void apply(List<Body> bs) {
		//Los cuerpos se mueven con una aceleraci¨®n fija
		
		//Para entender bien las leyes de fuerzas(Ver parte de simulador y de commando):
		//En cada simulaci¨®n aplicamos una de las leyes que existe (un objeto de tipo de ForceLaws)
		//En advance de simulador hacemos : 1.ResetForce 2.Aplly 3.Move
		
		//Pero en este si force = (0,0) -> a = f/m - > a = (0,0); Ya que no hacemos nada....
		//Es decir la velocidad de los cuerpos son constantes...... Mal interpretado yo o?
		
	}
	
}
