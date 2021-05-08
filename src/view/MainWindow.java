package view;

import java.awt.BorderLayout;
import java.awt.Dimension;

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
		
		
		centerPanel.add(bodiesTable);
		centerPanel.add(viewer);
		//manejar las dimensiones desde la ventana principal
		bodiesTable.setPreferredSize(new Dimension(800,300));
		viewer.setPreferredSize(new Dimension(800,600));
		
		this.pack();
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		new MainWindow(null);
	}
	
	// other private/protected methods
	// ...
	
}
