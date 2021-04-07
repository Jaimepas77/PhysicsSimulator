package simulator.control;

import org.json.JSONObject;

public class NotEqualStatesException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public NotEqualStatesException (JSONObject out, JSONObject expectedOut, int step) {
		super(
			  "step: " + step + System.lineSeparator() +
			  "Actual out: " + out + System.lineSeparator() +
			  "Expected out: " + expectedOut + System.lineSeparator()
			   );
	}
	
}
