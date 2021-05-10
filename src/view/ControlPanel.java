package view;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.swing.*;

import org.json.JSONException;
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

	//Selector de ficheros
	private JFileChooser fileChooser;
	
	//ToolBar
	private JToolBar toolBar;
	
	//Spinner & observador
	private JSpinner steps;//Contiene el numero de pasos a ejecutar
	
	JTextField deltaTime;//Campo de texto en el que se introduce el tiempo por paso
	
	
	ControlPanel(Controller controller){
		this.controller = controller;
		stopped = false;//true;
		initGUI();
		controller.addObserver(this);
	}

	private void initGUI() {
		
		this.setLayout(new BorderLayout());
		
		toolBar = new JToolBar();
		
		//Boton de seleccion de fichero
		initFileButton();//ToDo
		toolBar.add(fileButton);
		
		toolBar.addSeparator();
		
		//Boton de selección de la ley gravitacional
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
		steps = new JSpinner(new SpinnerNumberModel(10, 1, 100000, 1));//Al JUGAR
		steps.setMaximumSize(new Dimension(80, 40));
		steps.setPreferredSize(new Dimension(80, 40));
		JLabel stepLabel= new JLabel("Steps: ");
		toolBar.add(stepLabel);
		toolBar.add(steps);
		
		toolBar.addSeparator();
		
		//TextField del DeltaTime
		JLabel delta = new JLabel("Delta-Time: ");
		deltaTime = new JTextField();
		deltaTime.setColumns(4);
		deltaTime.setMaximumSize(deltaTime.getPreferredSize());//Para que mantenga una distacia con exitButton
		deltaTime.setText(realTime + "");
		toolBar.add(delta);
		toolBar.add(deltaTime);
		
		toolBar.add(Box.createGlue());//Pegamento para la redimension
		toolBar.addSeparator();
		
		//Boton de apagar
		initExitButton();
		toolBar.add(exitButton);
		
		this.add(toolBar);//No hace falta especificar la ubicación en el layout porque sólo se inserta un elemento (y el borderLayout sólo se usa para que redimensione bien.)
	}
	
	private void initFileButton() {
		fileButton = new JButton(new ImageIcon("resources\\icons\\open.png"));//Completar dir¡¢
		fileButton.setToolTipText("Seleccionar fichero fuente");
		
		fileChooser = new JFileChooser();//Se inicializa el fileChooser que luego se empleara cada vez que se pulse el boton
		fileChooser.setCurrentDirectory(new File("resources\\examples"));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		fileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
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
					catch (JSONException e2) {
						JOptionPane.showMessageDialog(null, "Error en el JSON: " + e2.getMessage(),  "ERROR", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}
		);
	}
	
	private void initLawConfButton() {

		lawConfButton = new JButton(new ImageIcon("resources/icons/physics.png"));//Ambas formas valdrian
		lawConfButton.setToolTipText("Seleccionar ley de gravitación");
		
		lawConfButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {//Hay que usar table.
				//controller.setForceLaws(info);

				new LawConfDialog(controller.getForceLawsInfo());
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
				//Desactivar todos los botones
				buttonEnable(false);
				
				stopped = false;
				try {
					controller.setStepTime(Double.parseDouble(deltaTime.getText()));//Se ajusta el delta time según ha especificado el usuario (stepTime = deltaTime)

					run_sim((int)steps.getValue());
				}
				catch (IllegalArgumentException error) {
					JOptionPane.showMessageDialog(null, error.getMessage());
					buttonEnable(true);
					stopped = true;
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
	
	private void initExitButton() {
		exitButton = new JButton(new ImageIcon("resources/icons/exit.png"));
		exitButton.setToolTipText("Salir");

		exitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				quit();
			}
		});
	}
	
	private void run_sim(int n) {
		if (n > 0 && !stopped) {
			try {
				controller.run(1);
			} catch (Exception e) {
				// TODO show the error in a dialog box
				JOptionPane.showMessageDialog(null, e.getMessage());

				// TODO enable all buttons
				buttonEnable(true);

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
			buttonEnable(true);
		}
	}


	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc) {
		updateDeltaTime(time);
	}

	@Override
	public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
		updateDeltaTime(time);
	}

	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
	}

	@Override
	public void onAdvance(List<Body> bodies, double time) {
	}

	@Override
	public void onDeltaTimeChanged(double dt) {
		updateDeltaTime(dt);
	}

	@Override
	public void onForceLawsChanged(String fLawsDesc) {
	}
	
	private void updateDeltaTime(double time) {//Actualiza el tiempo por paso de ejecucion (el delta-time)
		this.realTime = time;
		deltaTime.setText(realTime + "");
	}
	
	private void buttonEnable(boolean value) {
		fileButton.setEnabled(value);
		lawConfButton.setEnabled(value);
		if(value) {//El button de pausa nunca se deshabilita
			pauseButton.setEnabled(value);
		}
		runButton.setEnabled(value);
		exitButton.setEnabled(value);
		deltaTime.setEnabled(value);
		steps.setEnabled(value);
	}
	
	protected void quit() {
		int op = JOptionPane.showConfirmDialog(exitButton, "Salir", "Ventana de Confirmacion", JOptionPane.YES_NO_OPTION);
		if(op == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}
}
