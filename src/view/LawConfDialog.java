package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;

import org.json.JSONObject;

public class LawConfDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	
	private static final String TITLE = "Force Laws Selection";
	private static final String INFO = "<html><p>Select a force law and provide values for the parametes in the <b>Value column</b> (default values are used for parametes with no value).</p></html>";
	
	private int index; 
	private int status;//Estado de JDialog (0: cancel, 1: ok)
	//ComboBox
	private JComboBox<String> lawsComboBox;
	private DefaultComboBoxModel<String> lawModel;

	//Label
	private JLabel info;
	
	//Datos para tabla
	private List<JSONObject> laws;
	
	//Tabla
	private LawTableModel lawTableModel;
	private JTable lawTable;
	
	//MainPanel
	private JPanel mainPanel;
	
	//ButtonPanel
	private JPanel buttonPanel;
	
	public LawConfDialog(JFrame frame, List<JSONObject> laws) {
		
		super(frame, true);
		this.laws = laws;
		
		lawModel = new DefaultComboBoxModel<>();
		for(JSONObject o : this.laws) {
			lawModel.addElement(o.getString("desc"));
		}
		
		initGUI();
	}
	
	private void initGUI() {
		setTitle(TITLE);
		status = 0;
		index = 0;
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		this.setContentPane(mainPanel);
		
		//Mensaje de ayuda
		initInfoLabel();
		mainPanel.add(info);
	
		initLawTable();
		mainPanel.add(new JScrollPane(lawTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		
		//ComboBox
		initLawComboBox();
		mainPanel.add(lawsComboBox);
		
		//ButtonPanel
		initButtonPanel();
		mainPanel.add(buttonPanel);
		
		setVisible(false);
	}
	
	private void initInfoLabel() {
		info = new JLabel(INFO);
		info.setAlignmentX(CENTER_ALIGNMENT);
	}
	
	private void initLawTable() {
		//Tabla de Configuracion
		lawTableModel = new LawTableModel(laws.get(index));
		lawTable = new JTable(lawTableModel);
	}
	
	private void initLawComboBox() {
		lawsComboBox = new JComboBox<>(lawModel);
		lawsComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				index = lawsComboBox.getSelectedIndex();
				lawTableModel.update(laws.get(index));
			}
		}
		);
	}
	
	private void initButtonPanel() {
		buttonPanel = new JPanel();
		buttonPanel.setAlignmentX(CENTER_ALIGNMENT);
		
		JButton okButton = new JButton("ok");
		okButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				status = 1;
				LawConfDialog.this.setVisible(false);
			}
		});
		
		JButton cancelButton = new JButton("cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				status = 0;
				LawConfDialog.this.setVisible(false);
			}
		});

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
	}
	
	public JSONObject getJSON() {
		return lawTableModel.getJSONObject();
	}
	
	public int getStatus() {
		pack();
		setVisible(true);//Mostrar al usuario para que elija
		return status;
	}
}
