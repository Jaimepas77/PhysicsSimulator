package view;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
	
	JTextField textTime;
	
	
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
		
		//Boton de seleccion de fichero
		initFileButton();//ToDo
		toolBar.add(fileButton);
		
		toolBar.addSeparator();
		
		//Boton de selecci�n de la ley gravitacional
		initLawConfButton();//ToDo
		toolBar.add(lawConfButton);
		
		toolBar.addSeparator();
		
		//Boton de reproducir (play)
		initRunButton();//ToDo
		toolBar.add(runButton);
		
		//Boton de pausa
		initPauseButton();
		toolBar.add(pauseButton);
		
		//Spinner de seleccion del numero de pasos a ejecutar
		step = new JSpinner(new SpinnerNumberModel(10, 1, 100000, 1));//Al JUGAR
		step.setMaximumSize(new Dimension(80, 40));
		step.setPreferredSize(new Dimension(80, 40));
		JLabel stepLabel= new JLabel("Steps: ");
		toolBar.add(stepLabel);
		toolBar.add(step);
		
		toolBar.addSeparator();
		
		//TextField del DeltaTime
		JLabel delta = new JLabel("Delta-Time: ");
		textTime = new JTextField();
		textTime.setColumns(4);
		textTime.setMaximumSize(textTime.getPreferredSize());//Para que mantenga una distacia con exitButton
		textTime.setText(realTime + "");
		toolBar.add(delta);
		toolBar.add(textTime);
		
		toolBar.add(Box.createGlue());//Pegamento para la redimension
		toolBar.addSeparator();
		
		//Boton de apagar
		initExitButton();
		toolBar.add(exitButton);
		
		this.add(toolBar);//No hace falta especificar la ubicaci�n en el layout porque s�lo se inserta un elemento (y el borderLayout s�lo se usa para que redimensione bien.)
	}
	
	private void initFileButton() {
		fileButton = new JButton(new ImageIcon("resources\\icons\\open.png"));//Completar dir��
		fileButton.setToolTipText("Seleccionar fichero fuente");
		
		fileButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser(); 
				
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

		lawConfButton = new JButton(new ImageIcon("resources/icons/physics.png"));//Ambas formas valdrian
		lawConfButton.setToolTipText("Seleccionar ley de gravitaci�n");
		
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
		pauseButton.setToolTipText("Pausar");
		
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
		runButton.setToolTipText("Ejecutar");
		
		runButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				fileButton.setEnabled(false);//Desactivar todos los botones
				lawConfButton.setEnabled(false);
				runButton.setEnabled(false);
				exitButton.setEnabled(false);
				textTime.setEnabled(false);
				step.setEnabled(false);
				
				stopped = false;
				run_sim((int)step.getValue());
			}
		}
		);		
	}
	
	private void initExitButton() {
		exitButton = new JButton(new ImageIcon("resources/icons/exit.png"));
		exitButton.setToolTipText("Salir");

		exitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int op = JOptionPane.showConfirmDialog(exitButton, "Salir", "Ventana de Confirmacion", JOptionPane.YES_NO_OPTION);
				if(op == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});
	}
	
	private void run_sim(int n) {
		if (n > 0 && !stopped) {
			try {
				//controller.run(1);
			} catch (Exception e) {
				// TODO show the error in a dialog box

				// TODO enable all buttons
				allButtonEnable();

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
		textTime.setText(realTime + "");
	}
	
	private void allButtonEnable() {
		// TODO enable all buttons
		fileButton.setEnabled(true);
		lawConfButton.setEnabled(true);
		pauseButton.setEnabled(true);
		runButton.setEnabled(true);
		exitButton.setEnabled(true);
		textTime.setEnabled(true);
		step.setEnabled(true);
	}
	
	public static void main(String[] args) {
		
		JFrame j = new JFrame("Prueba");
		j.setLayout(new BorderLayout());
		ControlPanel p = new ControlPanel();//No funcionaria la parte que necesita controller
		j.add(p,BorderLayout.PAGE_START);
		j.pack();
		j.setVisible(true);
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
