package simulator.model;

import simulator.misc.Vector2D;

public class MassLossingBody extends Body{
	
	private double lossFactor;//Proporci¨®n de masa de cada p¨¦rdida
	private double lossFrequency; //"Periodo" en el que produce la p¨¦rdida de masa
	private double c ; //Contador de tiempo

	public MassLossingBody(String id, Vector2D velocity, Vector2D position, double mass , double lossFactor , double lossFrequency) {
		super(id, velocity, position, mass);
		this.lossFactor = lossFactor;
		this.lossFrequency = lossFrequency;
		c = 0.0;
	}
	
	protected void move(double t) {
		//Mover
		super.move(t);
		
		c += t;
		//P¨¦rdida de la masa ,de manera 1 por movimiento.
		if(c >= lossFrequency ) {
			mass *= (1 - lossFactor);
			c = 0.0;//Resetea el contador
		}
		
	}

}
