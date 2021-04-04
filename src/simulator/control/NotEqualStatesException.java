package simulator.control;

import org.json.JSONObject;

public class NotEqualStatesException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public NotEqualStatesException (JSONObject out , JSONObject expectedOut ,JSONObject actBody , JSONObject expectedBody ,int step) {//Complementaria
		super(
			  "step: " + step + System.lineSeparator() +
			  "Actual out: " + out + System.lineSeparator() +
			  "Expected out: " + expectedOut + System.lineSeparator()+
			  "Actual body: " + actBody + System.lineSeparator() + //Primer cuerpo donde se muestra una diferencia con lo esperado.
			  "Expected body: " + expectedBody + System.lineSeparator() 
			   );
	}
	
	public NotEqualStatesException (JSONObject out , JSONObject expectedOut  ,int step) {//Basico
		super(
			  "step: " + step + System.lineSeparator() +
			  "Actual out: " + out + System.lineSeparator() +
			  "Expected out: " + expectedOut + System.lineSeparator()
			   );
	}
	
	

}
