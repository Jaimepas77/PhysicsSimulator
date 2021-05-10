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
	
	private String[] columns = {"Key", "Value", "Description"};
	private JSONObject lawData;
	private List<String> keys;
	private List<String> values;
	
	public LawTableModel(JSONObject law){
		update(law);
	}
	
	@Override
	public String getColumnName(int column) {
		return this.columns[column];
	}
	
	@Override
	public int getRowCount() {
		return lawData.length();
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
				return lawData.get(key);
		}
		return null;
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if(columnIndex == 1) {
			return true;
		}
		else {
			return false;
		}
	}
	
	@Override
	public void setValueAt(Object value, int row, int col) {
		values.set(row, (String)value);
	}
	
	public void update(JSONObject law) {
		lawData = law.getJSONObject("data");
		
		keys = new ArrayList<String>();
		values = new ArrayList<String>();
		for(String s : lawData.keySet()) {
			keys.add(s);
			values.add("");
		}
		
		fireTableStructureChanged();
	}

}
