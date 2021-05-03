package view;

import java.awt.BorderLayout;

import javax.swing.*;

import simulator.control.Controller;

public class MainWindow extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// ...
	Controller controller;
	public MainWindow(Controller controller) {
		super("Physics Simulator");
		this.controller = controller;
		initGUI();
	}
	private void initGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		setContentPane(mainPanel);
		// TODO complete this method to build the GUI
		// ..
		
		//Paneles
		BodiesTable bodiesTable = new BodiesTable(controller);
		ControlPanel controlPanel = new ControlPanel(controller);
		Viewer viewer = new Viewer(controller);
		StatusBar statusBar = new StatusBar(controller);
		
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel,BoxLayout.Y_AXIS));
		
		this.add(controlPanel,BorderLayout.PAGE_START);
		this.add(centerPanel,BorderLayout.CENTER);
		this.add(statusBar,BorderLayout.PAGE_END);
		
		//manejar la dimension desde ventana principal
		centerPanel.add(bodiesTable);
		centerPanel.add(controlPanel);
		
		this.pack();
		this.setVisible(true);
	}
	// other private/protected methods
	// ...
	
}
