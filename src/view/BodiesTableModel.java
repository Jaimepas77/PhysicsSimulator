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
	
	private String[] columns = {"Id", "Mass", "Position", "Velocity", "Force"};
	private List<Body> bodies;
	
	//Contructores
	public BodiesTableModel(Controller controller) {
		bodies = new ArrayList<>();
		controller.addObserver(this); //Para prueba
	}
	
	//Metodos a ser un TableModel
	@Override
	public int getRowCount() {
		return bodies == null ? 0 : bodies.size();
	}

	@Override
	public int getColumnCount() {
		return columns.length;
	}
	
	@Override
	public String getColumnName(int column) {
		return this.columns[column];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object value = null;
		Body b = bodies.get(rowIndex);
		
		switch (columnIndex) {
		case 0:
			value = b.getId();
			break;
		case 1:
			value = b.getMass();
			break;
		case 2:
			value = b.getPosition();
			break;
		case 3:
			value = b.getVelocity();
			break;
		case 4:
			value = b.getForce();
			break;
		}
		return value;
	}
	
	//Metodos por ser un observador
	public void setBodies(List<Body> bodies) {
		this.bodies = bodies;
		fireTableStructureChanged();//Notificar los cambios realizados
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
