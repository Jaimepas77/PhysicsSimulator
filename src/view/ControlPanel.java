package view;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Container;
import java.awt.Dimension;
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
	
	//Observador
	private double realTime;
	
	//Buttons
	private JButton fileButton;
	private JButton lawConfButton;
	private JButton pauseButton;
	private JButton runButton;
	private JButton exitButton;
	
	//ToolBar
	private JToolBar toolBar;
	
	//Spinner & observador
	private JSpinner step;
	
	
	JTextField text_time;

	
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
		
		this.setLayout(new BorderLayout());
		
		toolBar = new JToolBar();
		//
		intFileButton();//ToDo
		toolBar.add(fileButton);
		toolBar.addSeparator();
		//
		initLawConfButton();//ToDo
		toolBar.add(lawConfButton);
		//
		initPauseButton();
		toolBar.add(pauseButton);
		toolBar.addSeparator();
		//Spinner
		step = new JSpinner(new SpinnerNumberModel(10, 1, 100000, 1));//Al JUGAR
		step.setMaximumSize(new Dimension(80, 40));
		step.setPreferredSize(new Dimension(80, 40));
		//
		initRunButton();//ToDo
		toolBar.add(runButton);
		JLabel stepLabel= new JLabel("step:");
		toolBar.add(stepLabel);
		toolBar.add(step);
		
		toolBar.addSeparator();
		//
		JLabel delta= new JLabel("Delta-tiem:");
		text_time = new JTextField();
		text_time.setColumns(4);
		text_time.setMaximumSize(text_time.getPreferredSize());//Para que mantenga una distacia con exitButton
		text_time.setText(realTime + "");
		toolBar.add(delta);
		toolBar.add(text_time);
		//
		initExitButton();
		toolBar.add(Box.createGlue());//Pegamento
		toolBar.addSeparator();
		toolBar.add(exitButton);
		
		this.add(toolBar,BorderLayout.PAGE_START);
		
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
						controller.reset();//ERROR si el archivo no es deseado no?
						controller.loadBodies(is);
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
				}
				
			}
		}
		);
	}
	
	private void initLawConfButton() {

		lawConfButton = new JButton(new ImageIcon("resources/icons/physics.png"));//Ambas formas vadria
		lawConfButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {//Hay que usar table.
				//controller.setForceLaws(info);
		
				JComboBox comboBox= new JComboBox(); 
				List<JSONObject> laws = controller.getForceLawsInfo();
				
				for(JSONObject o : laws) {
					comboBox.addItem(o.getString("desc"));
				}
			
			}
					
			}
		);
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
	}
	
	private void initRunButton() {
		runButton  = new JButton(new ImageIcon("resources/icons/run.png"));
		runButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				fileButton.setEnabled(false);//Desactivar todas los botones
				lawConfButton.setEnabled(false);
				exitButton.setEnabled(false);
				stopped = false;
				run_sim((int)step.getValue());
			}
			
		});
		
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
		
	}
	
	private void run_sim(int n) {
		if ( n>0 && ! stopped ) {
		try {
			//controller.run(1);
		} catch (Exception e) {
			// TODO show the error in a dialog box
			// TODO enable all buttons
			allButtonEnable();
			stopped = true;
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
			allButtonEnable();
		}
	}


	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc) {
		updateTime(time);
		updateStep(dt);
	}

	@Override
	public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
		updateTime(time);
		updateStep(dt);
	}

	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
	}

	@Override
	public void onAdvance(List<Body> bodies, double time) {
		updateTime(time);
	}

	@Override
	public void onDeltaTimeChanged(double dt) {
		updateStep(dt);
	}

	@Override
	public void onForceLawsChanged(String fLawsDesc) {
	}
	
	private void updateStep(double dt) {
		step.setValue(dt);
	}
	
	private void updateTime(double time) {
		this.realTime = time;
		text_time.setText(realTime + "");
	}
	
	private void allButtonEnable() {
		// TODO enable all buttons
		fileButton.setEnabled(true);
		lawConfButton.setEnabled(true);
		pauseButton.setEnabled(true);
		runButton.setEnabled(true);
		exitButton.setEnabled(true);
	}
	
	public static void main(String[] args) {
		
		JFrame j = new JFrame("Prueba");
		j.setLayout(new BorderLayout());
		ControlPanel p = new ControlPanel();//No funcionaria la parte que necesita controller
		j.add(p,BorderLayout.NORTH);
		j.pack();
		j.setVisible(true);
	}

}
