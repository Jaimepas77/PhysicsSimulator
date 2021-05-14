package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.swing.*;

import org.json.JSONException;
import org.json.JSONObject;

import simulator.control.Controller;

public class MainWindow extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private MyMenu myMenu;
	private ControlPanel controlPanel;
	private BodiesTable bodiesTable;
	private Viewer viewer;
	private StatusBar statusBar;
	
	private JPanel centerPanel;

	//Selector de ficheros
	private JFileChooser fileChooser;
	
	//Selector de fuerza gravitacional
	LawConfDialog lawDialog;

	Controller controller;
	public MainWindow(Controller controller) {
		super("Physics Simulator");
		this.controller = controller;
		initGUI();
	}
	
	private void initGUI() {
		//Layout
		JPanel mainPanel = new JPanel(new BorderLayout());
		setContentPane(mainPanel);
		
		//Inicialización de paneles y componentes que se insertarán en el borderLayout
		initComponents();

		this.setJMenuBar(myMenu);
		//Añadir en el border layout
		this.add(controlPanel, BorderLayout.PAGE_START);//Arriba (toolbar: botones de control)
		this.add(centerPanel, BorderLayout.CENTER);//En medio (simulación)
		this.add(statusBar, BorderLayout.PAGE_END);//Abajo (barra de info)
		
		//Controlar el cierre de la ventana (pedir mensaje de confirmación)
		setCloseDialog();
		
		//Ajustar las dimensiones
		bodiesTable.setPreferredSize(new Dimension(600, 300));
		viewer.setPreferredSize(new Dimension(600, 400));
		setMinimumSize(new Dimension(650, 500));
		
		pack();
		setVisible(true);
	}
	
	private void initComponents() {
		
		//JMenuBar
		myMenu = new MyMenu(this);

		controlPanel = new ControlPanel(controller, this);//Control
		
		//Componentes de en medio (simulación)
		bodiesTable = new BodiesTable(controller);
		viewer = new Viewer(controller);
		
		centerPanel = new JPanel();//Simulación
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		centerPanel.add(bodiesTable);
		centerPanel.add(viewer);
		
		statusBar = new StatusBar(controller);//Estado
	}
	
	private void setCloseDialog() {

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowListener() {

			@Override
			public void windowClosing(WindowEvent arg0) {
				quit();
			}
			
			@Override
			public void windowActivated(WindowEvent arg0) {				
			}
			@Override
			public void windowClosed(WindowEvent arg0) {
			}
			@Override
			public void windowDeactivated(WindowEvent arg0) {
			}
			@Override
			public void windowDeiconified(WindowEvent arg0) {
			}
			@Override
			public void windowIconified(WindowEvent arg0) {
			}
			@Override
			public void windowOpened(WindowEvent arg0) {
			}
		});
	}
	
	protected void chooseFile() {
		if(fileChooser == null) {
			fileChooser = new JFileChooser();//Se inicializa el fileChooser que luego se empleara cada vez que se pulse el boton
			fileChooser.setCurrentDirectory(new File("resources\\examples"));
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		}
		
		int selection = fileChooser.showOpenDialog(this);//Abre la ventana 
		
		if(selection == JFileChooser.APPROVE_OPTION) {//Si selecciona
			File file = fileChooser.getSelectedFile();
			try {
				InputStream is = new FileInputStream(file);
				controller.reset();
				controller.loadBodies(is);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			catch (JSONException e2) {
				JOptionPane.showMessageDialog(null, "Error en el JSON: " + e2.getMessage(),  "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	protected void lawConfWindow() {
		if(lawDialog == null) {//Crear si no se ha creado aún
			lawDialog = new LawConfDialog(null, controller.getForceLawsInfo());
		}
		
		int i = lawDialog.getStatus();//Obtener la info del usuario
		if(i == 1) {
			try {
				JSONObject info = lawDialog.getJSON();
				controller.setForceLaws(info);
			}
			catch(Exception error) {
				JOptionPane.showMessageDialog(null, error.getMessage());
			}
		}
	}
	
	protected void initRun() {
		//Desactivar todos los botones
		buttonEnable(false);
		
		controlPanel.setStopped(false);
		try {
			controller.setStepTime(controlPanel.getDeltaTime());//Se ajusta el delta time según ha especificado el usuario (stepTime = deltaTime)

			controlPanel.run_sim(controlPanel.getSteps());
		}
		catch (IllegalArgumentException error) {
			JOptionPane.showMessageDialog(null, error.getMessage());
			buttonEnable(true);
			controlPanel.setStopped(true);
		}
	}
	
	protected void pause() {
		controlPanel.setStopped(true);
	}
	
	protected void quit() {
		int op = JOptionPane.showConfirmDialog(this, "¿Seguro que quieres salir?", "Ventana de confirmación", JOptionPane.YES_NO_OPTION);
		if(op == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}
	
	protected void buttonEnable(boolean enable) {
		controlPanel.buttonEnable(enable);
		myMenu.buttonEnable(enable);
	}
}
