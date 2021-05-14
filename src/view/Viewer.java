package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.TitledBorder;

import simulator.control.Controller;
import simulator.misc.Vector2D;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class Viewer extends JComponent implements SimulatorObserver {

	private static final long serialVersionUID = 1L;
	// ...
	private int _centerX;
	private int _centerY;
	private double _scale;
	private List<Body> _bodies;
	private boolean _showHelp;
	private boolean _showVectors;
	
	//
	private static final int RADIUS = 5; 
	
	private static final String HELP_MSG = "h: toggle help, v: toggle vectors, +: zoom in, -: zoom out, =: fir";
	private static final Color VEL_COLOR = new Color(0, 180, 0);//Verde oscuro (cuanto más bajo el número, más oscuro el color)
	private static final Color FORCE_COLOR = Color.RED;
	private static final Color BODY_COLOR = Color.BLUE;
	
	public Viewer(Controller ctrl) {
		initGUI();
		ctrl.addObserver(this);
	}
	
	private void initGUI() {
		//border with title
		this.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.black, 2),
				"Viewer",
				TitledBorder.LEFT, TitledBorder.TOP)
				);	

		_bodies = new ArrayList<>();
		_scale = 1.0;
		_showHelp = true;
		_showVectors = true;
		addKeyListener(new KeyListener() {
			// ...
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyChar()) {
				case '-':
					_scale = _scale * 1.1;
					repaint();
					break;
				case '+':
					_scale = Math.max(1000.0, _scale / 1.1);
					repaint();
					break;
				case '=':
					autoScale();
					repaint();
					break;
				case 'h':
					_showHelp = !_showHelp;
					repaint();
					break;
				case 'v':
					_showVectors = !_showVectors;
					repaint();
					break;
				default:
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}
			@Override
			public void keyReleased(KeyEvent e) {
			}
		});
		addMouseListener(new MouseListener() {
			// ...
			@Override
			public void mouseEntered(MouseEvent e) {
				requestFocus();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
			@Override
			public void mousePressed(MouseEvent e) {
			}
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			@Override
			public void mouseExited(MouseEvent e) {
			}
		});
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// use 'gr' to draw, not 'g' --- it gives nicer results
		Graphics2D gr = (Graphics2D) g;
		
		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		gr.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		// calculate the center
		_centerX = getWidth() / 2;
		_centerY = getHeight() / 2;
		
		// draw a cross at center
		drawCross(gr);
		
		// draw bodies (with vectors if _showVectors is true)
		drawBodies(gr);
		
		//draw help if _showHelp is true
		if(_showHelp) {
			drawHelp(gr);
		}
	}

	private void drawCross(Graphics2D gr) {
		gr.setColor(Color.red);
		gr.drawLine(_centerX - 5, _centerY, _centerX + 5, _centerY);
		gr.drawLine(_centerX, _centerY- 5, _centerX, _centerY + 5);
	}

	private void drawBodies(Graphics2D gr) {

		for (Body b : _bodies) {
			//Posición del cuerpo
			int x = _centerX + (int)(b.getPosition().getX() / _scale);
			int y = _centerY - (int)(b.getPosition().getY() / _scale);
			
			//Vector
			if(_showVectors) {
				//No usar directamente el vector, porque puede llevar un valor enorme.
				Vector2D force = b.getForce().direction().scale(20);
				Vector2D velocity = b.getVelocity().direction().scale(20);
				//Se puede ajusta un poco más
				drawLineWithArrow(gr, x, y, x + (int)force.getX(), y - (int)force.getY(), 4, 4, FORCE_COLOR, FORCE_COLOR);
				drawLineWithArrow(gr, x, y, x + (int)velocity.getX(), y - (int)velocity.getY(), 4, 4, VEL_COLOR, VEL_COLOR);
			}
			
			//Círculo del cuerpo
			gr.setColor(BODY_COLOR);
			gr.fillOval(x - RADIUS,  y - RADIUS, RADIUS * 2, RADIUS * 2);//FillOval se utiliza x, y como izq-sup pero lo queremos es en central
			
			//Nombre del cuerpo
			gr.setColor(Color.BLACK);
			gr.drawString(b.getId(), x - RADIUS,  y - RADIUS);//Habría que ajustar un poco
		}
	}
	
	private void drawHelp(Graphics2D gr) {
		gr.setColor(Color.red);
		gr.drawString(HELP_MSG, 10, 25);
		gr.drawString("Scaling ratio: " + String.valueOf(_scale), 10, 42);
	}

	private void autoScale() {
		double max = 1.0;
		for (Body b : _bodies) {
			Vector2D p = b.getPosition();
			max = Math.max(max, Math.abs(p.getX()));
			max = Math.max(max, Math.abs(p.getY()));
		}
		double size = Math.max(1.0, Math.min(getWidth(), getHeight()));
		_scale = max > size ? 4.0 * max / size : 1.0;
	}
	
	// This method draws a line from (x1, y1) to (x2, y2) with an arrow.
	// The arrow is of height h and width w.
	// The last two arguments are the colors of the arrow and the line
	private void drawLineWithArrow(Graphics g, int x1, int y1, int x2, int y2, int w, int h, Color lineColor, Color arrowColor) {
		int dx = x2 - x1, dy = y2 - y1;
		double D = Math.sqrt(dx * dx + dy * dy);
		double xm = D - w, xn = xm, ym = h, yn = -h, x;
		double sin = dy / D, cos = dx / D;
		x = xm * cos - ym * sin + x1;
		ym = xm * sin + ym * cos + y1;
		xm = x;
		x = xn * cos - yn * sin + x1;
		yn = xn * sin + yn * cos + y1;
		xn = x;
		int[] xpoints = { x2, (int) xm, (int) xn };
		int[] ypoints = { y2, (int) ym, (int) yn };
		g.setColor(lineColor);
		g.drawLine(x1, y1, x2, y2);
		g.setColor(arrowColor);
		g.fillPolygon(xpoints, ypoints, 3);
	}
	
	// SimulatorObserver methods
	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc) {
		updateBodies(bodies);
	}
	@Override
	public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
		updateBodies(bodies);
	}
	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		updateBodies(bodies);
	}
	@Override
	public void onAdvance(List<Body> bodies, double time) {
		repaint();
	}
	@Override
	public void onDeltaTimeChanged(double dt) {
		
	}
	@Override
	public void onForceLawsChanged(String fLawsDesc) {
		
	}
	
	private void updateBodies(List<Body> bodies) {
		this._bodies = bodies;
		autoScale();
		repaint();
	}
	
}