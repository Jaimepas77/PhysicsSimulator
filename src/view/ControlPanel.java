package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class ControlPanel extends JPanel implements SimulatorObserver{

	private static final long serialVersionUID = 1L;
	private Controller controller;
	private boolean stopped;
	
	private MainWindow mainWindow;
	
	//Buttons
	private JButton fileButton;
	private JButton lawConfButton;
	private JButton pauseButton;
	private JButton runButton;
	private JButton exitButton;
	
	//ToolBar
	private JToolBar toolBar;
	
	//Spinner
	private JSpinner steps;//Contiene el numero de pasos a ejecutar
	
	JTextField deltaTime;//Campo de texto en el que se introduce el tiempo por paso
	
	
	ControlPanel(Controller controller, MainWindow mainWindow){
		this.controller = controller;
		this.mainWindow = mainWindow;
		stopped = false;
		initGUI();
		controller.addObserver(this);
	}

	private void initGUI() {
		
		this.setLayout(new BorderLayout());
		
		toolBar = new JToolBar();
		
		//Botón de selección de fichero
		initFileButton();
		toolBar.add(fileButton);
		
		toolBar.addSeparator();
		
		//Botón de selección de la ley gravitacional
		initLawConfButton();
		toolBar.add(lawConfButton);
		
		toolBar.addSeparator();
		
		//Botón de reproducir (play)
		initRunButton();
		toolBar.add(runButton);
		
		//Botón de pausa
		initPauseButton();
		toolBar.add(pauseButton);
		
		//Spinner de selección del número de pasos a ejecutar
		steps = new JSpinner(new SpinnerNumberModel(10000, 1, 100000, 100));
		steps.setMaximumSize(new Dimension(80, 40));
		steps.setPreferredSize(new Dimension(80, 40));
		JLabel stepLabel= new JLabel("Steps: ");
		toolBar.add(stepLabel);
		toolBar.add(steps);
		
		toolBar.addSeparator();
		
		//TextField del DeltaTime
		JLabel deltaLabel = new JLabel("Delta-Time: ");
		deltaTime = new JTextField();
		deltaTime.setColumns(4);
		deltaTime.setMaximumSize(deltaTime.getPreferredSize());//Para que mantenga una distacia con exitButton
		toolBar.add(deltaLabel);
		toolBar.add(deltaTime);
		
		toolBar.add(Box.createGlue());//Pegamento para la redimension
		toolBar.addSeparator();
		
		//Botón de apagar
		initExitButton();
		toolBar.add(exitButton);
		
		this.add(toolBar);//No hace falta especificar la ubicación en el layout porque sólo se inserta un elemento (y el borderLayout sólo se usa para que redimensione bien.)
	}
	
	private void initFileButton() {
		fileButton = new JButton(new ImageIcon("resources\\icons\\open.png"));//Completar dir
		fileButton.setToolTipText("Seleccionar fichero fuente");
		
		fileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.chooseFile();
			}

		}
		);
	}
	
	private void initLawConfButton() {

		lawConfButton = new JButton(new ImageIcon("resources/icons/physics.png"));//Ambas formas valdrían
		lawConfButton.setToolTipText("Seleccionar ley de gravitación");
		lawConfButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.lawConfWindow();
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
				mainWindow.initRun();
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
				setStopped(true);
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
				mainWindow.quit();
			}
		});
	}
	
	void run_sim(int n) {
		if (n > 0 && !stopped) {
			try {
				controller.run(1);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e.getMessage());

				mainWindow.buttonEnable(true);

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
			mainWindow.buttonEnable(true);
		}
	}


	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc) {
		deltaTime.setText(dt + "");
	}

	@Override
	public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
		deltaTime.setText(dt + "");
	}

	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
	}

	@Override
	public void onAdvance(List<Body> bodies, double time) {
	}

	@Override
	public void onDeltaTimeChanged(double dt) {
		deltaTime.setText(dt + "");
	}

	@Override
	public void onForceLawsChanged(String fLawsDesc) {
	}
	
	protected void buttonEnable(boolean value) {
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
	
	public int getSteps() {
		return (int)steps.getValue();
	}

	public double getDeltaTime() {
		return Double.parseDouble(deltaTime.getText());
	}

	public void setStopped(boolean stopped) {
		this.stopped = stopped;
	}

}
