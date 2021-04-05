package simulator.model;

import simulator.misc.Vector2D;

public class MassLossingBody extends Body{
	
	private double lossFactor;//Proporcion de masa de cada perdida
	private double lossFrequency; //"Periodo" en el que produce la perdida de masa
	private double c; //Contador de tiempo

	public MassLossingBody(String id, Vector2D velocity, Vector2D position, double mass, double lossFactor, double lossFrequency) {
		super(id, velocity, position, mass);
		this.lossFactor = lossFactor;
		this.lossFrequency = lossFrequency;
		c = 0.0;
	}
	
	protected void move(double t) {
		//Mover
		super.move(t);
		
		c += t;
		//Pérdida de la masa, de manera 1 por movimiento.
		if(c >= lossFrequency ) {
			mass *= (1 - lossFactor);
			c = 0.0;//Resetea el contador
		}
	}

}
