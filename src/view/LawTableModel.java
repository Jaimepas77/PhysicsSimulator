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
	private List<String> keys;
	private List<String> values;
	
	private JSONObject law;
	
	public LawTableModel(JSONObject law){
		update(law);
	}
	
	@Override
	public String getColumnName(int column) {
		return this.columns[column];
	}
	
	@Override
	public int getRowCount() {
		return law.getJSONObject("data").length();
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
				return law.getJSONObject("data").get(key);
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
		this.law = new JSONObject(law.toString());//Una copia de law
		
		keys = new ArrayList<String>();
		values = new ArrayList<String>();
		for(String s : law.getJSONObject("data").keySet()) {
			keys.add(s);
			values.add(new String());
		}
		
		fireTableStructureChanged();
	}
	
	public JSONObject getJSONObject() {//Orientado por dialogExample
		StringBuilder s = new StringBuilder();
		s.append('{');
		for(int i = 0; i< values.size() ; i++) {
			if(!values.get(i).isEmpty()) {
				s.append('"');
				s.append(keys.get(i));
				s.append('"');
				s.append(':');
				s.append(values.get(i));
				s.append(',');
			}
		}
		if (s.length() > 1)
			s.deleteCharAt(s.length() - 1);
		s.append('}');
		
		JSONObject data = new JSONObject(s.toString());
		law.put("data", data);//Se devuelve un JSON con todos las informaciones (tipo y data)data actualizado segun la tabla
		return law;
	}

}
