package view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import org.json.JSONObject;

import simulator.model.ForceLaws;

public class LawConfDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String TITLE = "Law configure ";
	private static final String INFO = "....";
	
	private int status ;//TODO
	//ComboBox
	private JComboBox<String> lawsComboBox;
	private DefaultComboBoxModel<String> lawModel;
	//Para table
	private List<JSONObject> laws;
	
	public LawConfDialog(List<JSONObject> laws) {
		
		this.laws = new ArrayList<JSONObject>();
		this.laws = laws;
		
		lawModel = new DefaultComboBoxModel<>();
		for(JSONObject o : this.laws) {
			lawModel.addElement(o.getString("desc"));
		}
		
		initGUI();

	}
	
	private void initGUI() {
		setTitle(TITLE);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
		this.setContentPane(mainPanel);
		
		//Mensaje de ayuda
		JLabel info = new JLabel(INFO);
		info.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(info);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		
		//Tabla de Configuracion
		LawTableModel lawTableModel = new LawTableModel(laws.get(1));
		JTable lawTable = new JTable(lawTableModel);
		mainPanel.add(new JScrollPane(lawTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		//ComboBox
		lawsComboBox = new JComboBox<>(lawModel);
		lawsComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(lawsComboBox.getSelectedItem().equals("")) {
					//Modificar tabla
				}
				else if(lawsComboBox.getSelectedItem().equals("")) {
					//Modificar tabla
				}
				else if(lawsComboBox.getSelectedItem().equals("")) {
					//Modificar tabla
				}
				
			}
		}
		);
		mainPanel.add(lawsComboBox);
		setVisible(true);
	}
}
