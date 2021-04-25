package view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import javax.swing.*;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class ControlPanel extends JPanel implements SimulatorObserver{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Controller controller;
	private boolean stopped;
	
	private double dt ;
	
	//Buttons
	private JButton fileButton;
	private JButton lawConfButton;
	private JButton pauseButton;
	private JButton runButton;
	private JButton exitButton;

	
	ControlPanel(Controller controller){
		this.controller = controller;
		stopped = false;
		initGUI();
		controller.addObserver(this);
	}
	ControlPanel(){
		initGUI();
	}

	private void initGUI() {
		// TODO Auto-generated method stub
		
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		intFileButton();
		
		initLawConfButton();//TODO
		
		initPauseButton();
		
		initRunButton();
		
		initExitButton();
		
	}
	
	private void intFileButton() {
		fileButton = new JButton(new ImageIcon("resources\\icons\\open.png"));//Completar dir¡¢
		fileButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser =  new JFileChooser(); 
				
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);//
				int selection = fileChooser.showOpenDialog(fileButton);//Abre la ventana 
				
				if(selection == JFileChooser.APPROVE_OPTION) {//Si selecciona
					File file = fileChooser.getSelectedFile();
					try {
						InputStream is = new FileInputStream(file);
						controller.reset();//ERROR......
						controller.loadBodies(is);
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
				}
				
			}
		}
		);
		this.add(fileButton);
	}
	
	private void initLawConfButton() {

		lawConfButton = new JButton(new ImageIcon("resources/icons/physics.png"));//Ambas formas vadria
		lawConfButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//controller.setForceLaws(info);
		
				JComboBox comboBox= new JComboBox(); 
				
				JPanel central = new JPanel();
				central.setLayout(new GridLayout());
				central.add(new Label("des"));
				central.add(new Label("values"));
				
				List<JSONObject> laws = controller.getForceLawsInfo();
				
				for(JSONObject o : laws) {
					comboBox.addItem(o.getString("desc"));
				}
			
			}
					
			}
		);
		this.add(lawConfButton);
	}
	
	private void initPauseButton() {
		pauseButton = new JButton(new ImageIcon("resources/icons/stop.png"));
		pauseButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				stopped = true;
			}
		}
		);
		this.add(pauseButton);
	}
	
	private void initRunButton() {
		JLabel stepLabel= new JLabel("step:");
		JSpinner step = new JSpinner();
		
		runButton  = new JButton(new ImageIcon("resources/icons/run.png"));
		runButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				fileButton.setEnabled(false);
				lawConfButton.setEnabled(false);
				exitButton.setEnabled(false);
				stopped = false;
				run_sim((int)step.getValue());
			}
			
		});
		this.add(runButton);
		
		this.add(stepLabel);
		this.add(step);
		
		JLabel delta= new JLabel("Delta-tiem:");
		JTextField deltaTime = new JTextField();
		deltaTime.setText(dt + "");
		this.add(delta);
		this.add(deltaTime);
	}
	
	private void initExitButton() {
		exitButton = new JButton(new ImageIcon("resources/icons/exit.png"));
		exitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int op = JOptionPane.showConfirmDialog(exitButton, "Salir","Ventana de Confirmacion" ,JOptionPane.YES_NO_OPTION);
				if(op ==JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
			
		});
		this.add(exitButton);
	}
	
	private void run_sim(int n) {
		if ( n>0 && ! stopped ) {
		try {
			//controller.run(1);
		} catch (Exception e) {
			// TODO show the error in a dialog box
			// TODO enable all buttons
			stopped = true;
		return;
		}
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
			run_sim(n-1);
			}
		});
		} else {
			stopped = true;
			// TODO enable all buttons
		}
	}


	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdvance(List<Body> bodies, double time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeltaTimeChanged(double dt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onForceLawsChanged(String fLawsDesc) {
		// TODO Auto-generated method stub
		
	}
	public static void main(String[] args) {
		
		JFrame j = new JFrame("Prueba");
		j.setLayout(new BorderLayout());
		ControlPanel p = new ControlPanel();//No funcionaria la parte que necesita controller
		j.add(p,BorderLayout.NORTH);
		j.setVisible(true);
	}

}
