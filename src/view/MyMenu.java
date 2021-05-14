package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MyMenu extends JMenuBar {
	
	private static final long serialVersionUID = 1L;

	private MainWindow mainWindow;
	
	private JMenuItem openFile;
	private JMenuItem selectForce;
	private JMenuItem play;
	private JMenuItem pause;
	private JMenuItem exit;

	public MyMenu(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		
		initMenu();
	}

	private void initMenu() {
		JMenu file = new JMenu("Archivo");
		openFile = new JMenuItem("Abrir archivo");
		openFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mainWindow.chooseFile();
			}
		});
		file.add(openFile);
		
		JMenu force = new JMenu("Fuerza");
		selectForce = new JMenuItem("Seleccionar fuerza gravitacional");
		selectForce.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mainWindow.lawConfWindow();
			}
		});
		force.add(selectForce);
		
		JMenu execution = new JMenu("Ejecución");
		play = new JMenuItem("Ejecutar simulación");
		play.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mainWindow.initRun();
			}
		});
		execution.add(play);
		
		pause = new JMenuItem("Pausar simulación");
		pause.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mainWindow.pause();
			}
		});
		execution.add(pause);
		
		JMenu window = new JMenu("Ventana");
		exit = new JMenuItem("Salir");
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mainWindow.quit();
			}
		});
		window.add(exit);
		
		this.add(file);
		this.add(force);
		this.add(execution);
		this.add(window);
	}

	public void buttonEnable(boolean enable) {
		openFile.setEnabled(enable);
		selectForce.setEnabled(enable);
		play.setEnabled(enable);
		if(enable) {
			pause.setEnabled(enable);
		}
		exit.setEnabled(enable);
	}
}
