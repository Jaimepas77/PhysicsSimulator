package view;

import java.awt.FlowLayout;
import java.util.List;

import javax.swing.*;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class StatusBar extends JPanel implements SimulatorObserver {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// ...
	private JLabel _currTime; // for current time
	private JLabel _currLaws; // for gravity laws
	private JLabel _numOfBodies; // for number of bodies
	
	//Cadenas que no cambian
	private static final String TIME = "Time: ";
	private static final String BODIES = "Bodies: ";
	private static final String LAWS = "Laws: ";
	
	StatusBar(Controller controller) {
		initGUI();
		controller.addObserver(this);
	}
	private void initGUI() {
		this.setLayout( new FlowLayout( FlowLayout.LEFT ));
		this.setBorder( BorderFactory.createBevelBorder( 1 ));
	// TODO complete the code to build the tool bar
		JToolBar toolBar = new JToolBar();
		
		//Por defecto
		_currTime = new JLabel(TIME + "0.0");
		_numOfBodies = new JLabel(BODIES + "0");
		_currLaws = new JLabel(LAWS + "Newton Gravitation with G : ....");
		toolBar.add(_currTime);
		toolBar.add(_numOfBodies);
		toolBar.add(_currLaws);
		this.add(toolBar);
	}
	// other private/protected methods
	// ...
	private void updateBodies(List<Body> bodies) {
		_numOfBodies.setText(BODIES + bodies.size());
	}
	
	private void updateTime(double time) {
		_currTime.setText(TIME + time);
	}
	
	private void updateLaws(String fLawsDesc) {
		_currLaws.setText(LAWS + fLawsDesc);
	}
	
	
	// SimulatorObserver methods
	// ...
	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc) {
		updateBodies(bodies);
		updateTime(time);
		updateLaws(fLawsDesc);
	}
	@Override
	public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
		updateBodies(bodies);
		updateTime(time);
		updateLaws(fLawsDesc);
	}
	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		updateBodies(bodies);
	}
	@Override
	public void onAdvance(List<Body> bodies, double time) {
		updateBodies(bodies);
		updateTime(time);
	}
	@Override
	public void onDeltaTimeChanged(double dt) {
		
	}
	@Override
	public void onForceLawsChanged(String fLawsDesc) {
		updateLaws(fLawsDesc);
	}

}
