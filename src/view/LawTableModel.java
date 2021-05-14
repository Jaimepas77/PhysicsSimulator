package view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.json.JSONObject;

public class LawTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	
	private String[] columns = {"Key", "Value", "Description"};
	private List<LawRowInfo> attributes;
	
	private JSONObject law;
	
	public LawTableModel(JSONObject law){
		attributes = new ArrayList<LawRowInfo>();
		update(law);
	}
	
	public void update(JSONObject law) {
		
		if( attributes.isEmpty() || !this.law.getString("type").equals(law.getString("type"))) {
			this.law = new JSONObject(law.toString());//Una copia de law
			attributes.clear();
			for(String key : law.getJSONObject("data").keySet()) {
				attributes.add(new LawRowInfo(key, new String(), law.getJSONObject("data").getString(key)));
			}
		}
		
		fireTableStructureChanged();
		
	}
	
	@Override
	public String getColumnName(int column) {
		return this.columns[column];
	}
	
	@Override
	public int getRowCount() {
		return attributes.size();
	}

	@Override
	public int getColumnCount() {
		return columns.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		LawRowInfo attribute = attributes.get(rowIndex);
		switch(columnIndex) {
			case 0 :
				return attribute.getKey();
			case 1:
				return attribute.getValue(); 
			case 2:
				return attribute.getDesc();
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
		attributes.get(row).setValue((String) value);
		
	}
	
	public JSONObject getJSONObject() {//Orientado por dialogExample
		StringBuilder s = new StringBuilder();
		s.append('{');
		for(int i = 0; i < attributes.size() ; i++) {
			if(!attributes.get(i).getValue().isEmpty()) {
				s.append('"');
				s.append(attributes.get(i).getKey());
				s.append('"');
				s.append(':');
				s.append(attributes.get(i).getValue());
				s.append(',');
			}
		}
		if (s.length() > 1)
			s.deleteCharAt(s.length() - 1);
		s.append('}');
		JSONObject data = new JSONObject(s.toString());
		law.put("data", data);
		
		return law;
	}

}
