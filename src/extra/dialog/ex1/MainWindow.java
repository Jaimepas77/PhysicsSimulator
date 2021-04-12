package extra.dialog.ex1;

import javax.swing.*;

import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {

	DishSelectionDialog _dialog;

	public MainWindow() {
		super("Custom Dialog Example");
		initGUI();
	}

	private void initGUI() {

		JPanel mainPanel = new JPanel();
		this.setContentPane(mainPanel);

		mainPanel.add(new JLabel("Click "));
		JButton here = new JButton("HERE");
		here.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				select_food();
			}
		});
		mainPanel.add(here);
		mainPanel.add(new JLabel(" to select your food"));

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);

	}

	protected void select_food() {

		if (_dialog == null) {

			// if you're in a JPanel class, you can use the following
			//
			//   (Frame) SwingUtilities.getWindowAncestor(this)
			//
			// in order to get the parent JFrame. Then pass it to the
			// constructor of MyDialogWindow instead of 'this'
			//
			_dialog = new DishSelectionDialog(this);
		}
		
		List<Dish> dishes = new ArrayList<Dish>();
		for (int i = 0; i < 10; i++) {
			dishes.add(new Dish("Yum Yum " + i));
		}

		int status = _dialog.open(dishes);

		if (status == 0) {
			System.out.println("Canceled");
		} else {
			System.out.println("Your favorite dish is: " + _dialog.getDish());
		}
	}

	public static void main(String[] args) throws InvocationTargetException, InterruptedException {
		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
				new MainWindow();
			}
		});
	}
}