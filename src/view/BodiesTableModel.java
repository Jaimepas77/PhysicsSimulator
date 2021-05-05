package view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class BodiesTableModel extends AbstractTableModel implements SimulatorObserver{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String[] col = {"Id","Mass","Position","Velocity","Force"};
	private List<Body> bodies;
	//Contructores
	public BodiesTableModel(Controller controller) {
		this.bodies = new ArrayList<>();
		controller.addObserver(this); //Para prueba
	}
	
	//Metodos a ser un TableModel
	@Override
	public int getRowCount() {
		return bodies == null ? 0 : bodies.size();
	}

	@Override
	public int getColumnCount() {
		return col.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		switch (columnIndex) {
		case 0:
			s = bodies.get(rowIndex).getId();
			break;
		case 1:
			s = bodies.get(rowIndex).getMass();
			break;
		case 2:
			s = bodies.get(rowIndex).getPosition();
			break;
		case 3:
			s = bodies.get(rowIndex).getVelocity();
			break;
		case 4:
			s = bodies.get(rowIndex).getForce();
			break;
		}
		return s;
	}
	
	@Override
	public String getColumnName(int col) {
		return this.col[col];
	}
	
	//Metodos por ser un observador
	public void setBodies(List<Body> bodies) {
		this.bodies = bodies;
		fireTableDataChanged();//Notificar el cambio
	}
	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc) {
		setBodies(bodies);	
	}
	
	@Override
	public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
		setBodies(bodies);
	}
	
	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		setBodies(bodies);
	}
	
	@Override
	public void onAdvance(List<Body> bodies, double time) {
		setBodies(bodies);
	}
	
	@Override
	public void onDeltaTimeChanged(double dt) {
	
	}
	
	@Override
	public void onForceLawsChanged(String fLawsDesc) {
		
	}

}
