package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.*;

import simulator.control.Controller;

public class MainWindow extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private ControlPanel controlPanel;
	private BodiesTable bodiesTable;
	private Viewer viewer;
	private StatusBar statusBar;
	
	private JPanel centerPanel;

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

		controlPanel = new ControlPanel(controller);//Control
		
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
				controlPanel.quit();
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
}
