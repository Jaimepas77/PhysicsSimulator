package view;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import simulator.control.Controller;

public class BodiesTable extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	BodiesTable(Controller controller) {
		//Configuracion de panel
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder(
				 BorderFactory.createLineBorder(Color.black, 2),
				"Bodies",
				TitledBorder.LEFT, TitledBorder.TOP)
		);
		//Table
		// TODO complete
		BodiesTableModel bodiesTableModel = new BodiesTableModel(controller);//Model a seguir(orientado por Jtable(example))
		JTable bodiestable = new JTable(bodiesTableModel);
		this.add(new JScrollPane(bodiestable,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
	}
	
	public static void main(String[] args) {
		
		JFrame j = new JFrame("Prueba");
		j.setLayout(new BorderLayout());
		BodiesTable b = new BodiesTable(null);//No funcionaria la parte que necesita controller
		ControlPanel p = new ControlPanel();
		j.add(p,BorderLayout.NORTH);
		j.add(b,BorderLayout.CENTER);
		j.pack();
		j.setVisible(true);
	}

}
