package view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.json.JSONObject;

public class LawTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String[] columns = {"Key","Value","Description"};
	private JSONObject law;
	private List<String> keys;
	private List<String> values;
	
	public LawTableModel(JSONObject law){
		this.law = law;
		
		keys = new ArrayList<String>();
		values = new ArrayList<String>();
		for(String s : law.keySet()) {
			keys.add(s);
			values.add("");
		}
		
	}
	
	@Override
	public String getColumnName(int column) {
		return this.columns[column];
	}
	
	@Override
	public int getRowCount() {
		return law.length();
	}

	@Override
	public int getColumnCount() {
		return columns.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {//Clave - valor
		String key = keys.get(rowIndex);
		switch(columnIndex) {
			case 0 :
				return key;
			case 1:
				return values.get(rowIndex); 
			case 2:
				return law.get(key);
		}
		return null;
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if(rowIndex == 1) {
			return true;
		}
		return false;
	}
	

}
