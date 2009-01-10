/**
 * 
 */
package javacalculus.graphing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/** 
 * Creates and displays a 2D graph of a function. This is made modular to accommodate
 * different parser implementations. To use this graphing tool, simply have a parser 
 * implement the CalcPlotter interface and call the CalcGraph constructor with the
 * CalcPlotter class as the parameter.
 * @see javacalculus.evaluator.CalcPLOT
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 *  
 *
 */
public class CalcGraph extends JComponent implements Runnable, MouseListener, MouseMotionListener, MouseWheelListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4609667081987450156L;

	protected JFrame frame;
	
	private static final int DEFAULT_WIDTH = 400;
	private static final int DEFAULT_HEIGHT = 400;
	//private static final int DEFAULT_IMAGETYPE = BufferedImage.TYPE_INT_ARGB;
	
	private double 
	min_x = -10, min_x_lowest = min_x,
	max_x = 10, max_x_highest = max_x,
	min_y = -10,
	max_y = 10,
	resolution = 0.01,	//distance between each x-value sample
	zoom_factor = 1;

	private int 	width = DEFAULT_WIDTH,
					height = DEFAULT_HEIGHT;
	
	private Color axis_color = Color.BLACK;
	private Color function_color = Color.BLUE;
	private Color background_color = Color.WHITE;
	private Color text_color = Color.RED;
	private Color zoom_rectangle_color = Color.ORANGE;
	
	private Font labelFont = new Font("Dialog", Font.BOLD, 10);
	
	//private BufferedImage graph = new BufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_IMAGETYPE);
	private CalcPlotter plotter;
	private ArrayList<Point> points = new ArrayList<Point>();
	private ArrayList<Point> transformedPoints = new ArrayList<Point>();
	private Rectangle2D Zoom_Rectangle;
	
	/**
	 * Constructor
	 * @param plotterIn the plotter that calculates the points on this graph
	 */
	public CalcGraph(CalcPlotter plotterIn) {
		plotter = plotterIn;
		SwingUtilities.invokeLater(this);
	}
	
	@Override
	public void run() {
		setPreferredSize(new Dimension(width, height));
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		populatePoints();
		transformPoints();
		repaint();
		frame = new JFrame("PLOT -> " + plotter.getFunction());
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setResizable(false);
		frame.setFocusable(true);
		frame.setContentPane(this);
		frame.setVisible(true);
		frame.pack();
		centerWindow();
		frame.setAlwaysOnTop(true);
		requestFocus();
	}
	
	/**
	 * Calculate all the unique points on the graph using boundary and resolution
	 * information. This is where the custom plotter is used.
	 */
	private void populatePoints() {
		points = new ArrayList<Point>();
		
		double currentX = min_x_lowest, currentY;
		
		while (currentX < max_x_highest) {
			currentY = plotter.getYValue(currentX);
			if (Double.isNaN(currentY) || Double.isInfinite(currentY)) {
				currentX += resolution;
				continue; //we don't want THESE points ^ on the graph...
			}
			points.add(new Point(currentX, currentY));
			currentX += resolution;
		}
	}
	
	/**
	 * Transforms all points in ArrayList <b>points</b> so that they can
	 * be drawn on a graphics2D with (0,0) set at top left corner and 
	 * dimensions specified by double precision <b>width</b> and <b>height</b>. 
	 */
	private void transformPoints() {
		transformedPoints = new ArrayList<Point>();
		
		if (max_x > max_x_highest || min_x < min_x_lowest) { //if zoom or pan goes beyond the current amount of information, repopulate points
			if (max_x > max_x_highest) max_x_highest = max_x;	//TODO OPTIMIZE THIS SHIT!
			if (min_x < min_x_lowest) min_x_lowest = min_x;
			populatePoints();
		}
		
		for (int ii = 0; ii < points.size(); ii++) {
			Point p = points.get(ii);
			double oldx = p.getX();
			double oldy = p.getY();
			double newx = width * (oldx - min_x)/(max_x - min_x);
			double newy = height * (max_y - oldy)/(max_y - min_y);
			transformedPoints.add(new Point(newx, newy));
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		
		//cast to Graphics2D cuz Graphics2D is so much better (more methods)
		Graphics2D g2d = (Graphics2D)g;
		
		//Antialiasing! Hellz yeah!
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
		
		//fill background color
		g2d.setColor(background_color);
		g2d.fillRect(0, 0, width, height);
		
		//draw the axis and labels
		paintAxis(g2d);
		paintLabels(g2d);
		
		if (Zoom_Rectangle != null) {
			g.setColor(zoom_rectangle_color);
			g.drawRect(	(int)Zoom_Rectangle.getX(),
						(int)Zoom_Rectangle.getY(), 
						(int)Zoom_Rectangle.getWidth(), 
						(int)Zoom_Rectangle.getHeight());
		}
		
		//draw the curve (a concatenation of small linear lines actually)
		Point currentPoint = transformedPoints.get(0);
		g2d.setColor(function_color);
		for (Point p : transformedPoints) {
			if ((int)p.getY() >= 0 && (int)p.getY() <= height)
			g2d.drawLine((int)currentPoint.getX(), (int)currentPoint.getY(), (int)p.getX(), (int)p.getY());
			//System.out.println("CP:" + currentPoint.getX() + " " + currentPoint.getY());
			//System.out.println("P: " + p.getX() + " " + p.getY());
			currentPoint = p;
		}
	}

	/**
	 * Draw the axis onto the graph (Graphics g). As of now, only prosaic straight lines.
	 * Will make more elaborate (labels) later.
	 * @param g
	 */
	private void paintAxis(Graphics g) {
		g.setColor(axis_color);
		
		//determine location of the origin using same scaling math used in transformPoints
		int originX = (int)(width * (-min_x)/(max_x - min_x));
		int originY = (int)(height * (max_y)/(max_y - min_y));
		
		//draw x axis
		g.drawLine(0, originY, width, originY);
		//draw y axis
		g.drawLine(originX, 0, originX, height);
	}
	
	private void paintLabels(Graphics g) {
		final int padding = 5; //the distance between the text and relevant edges
		
		g.setColor(text_color);
		g.setFont(labelFont);
		
		FontMetrics metrics = g.getFontMetrics(labelFont); //this helps measure the font
		
		//determine location of the origin using same scaling math used in transformPoints
		int originX = (int)(width * (-min_x)/(max_x - min_x));
		int originY = (int)(height * (max_y)/(max_y - min_y));
		
		//draw x axis label
		String variable = plotter.getIndependentVariable();
		int variableWidth = (int)metrics.getStringBounds(variable, g).getWidth();
		int variableHeight = (int)metrics.getStringBounds(variable, g).getHeight();
		g.drawString(variable, width - variableWidth - padding, originY + variableHeight);
		
		//draw y axis label
		String function = "f(" + plotter.getIndependentVariable() + ")=" + plotter.getFunction();
		//int functionWidth = (int)metrics.getStringBounds(function, g).getWidth();
		int functionHeight = (int)metrics.getStringBounds(function, g).getHeight();
		g.drawString(function, originX + padding, functionHeight);
		
		//draw boundary values
		String xmin = Double.toString(min_x);
		String xmax = Double.toString(max_x);
		String ymin = Double.toString(min_y);
		String ymax = Double.toString(max_y);
		
		g.drawString(xmin, padding, originY - padding);
		
		int xmaxWidth = (int)metrics.getStringBounds(xmax, g).getWidth();
		g.drawString(xmax, width - xmaxWidth - padding, originY - padding);
		
		int ymaxWidth = (int)metrics.getStringBounds(ymax, g).getWidth();
		int ymaxHeight = (int)metrics.getStringBounds(ymax, g).getHeight();
		g.drawString(ymax, originX - ymaxWidth - padding, padding + ymaxHeight);
		
		int yminWidth = (int)metrics.getStringBounds(ymin, g).getWidth();
		g.drawString(ymin, originX - yminWidth - padding, height - padding);
	}

	/**
	 * Centers the JFrame on the screen. Stole this code from my java guitar hero
	 * thing.
	 */
	private void centerWindow() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getSize();
		if (frameSize.height > screenSize.height)
			frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width)
			frameSize.width = screenSize.width;
		frame.setLocation((screenSize.width - frameSize.width)/2, 
				(screenSize.height - frameSize.height)/2);
	}
	
	/**
	 * Truncates a double to 2 decimal places
	 * @param x
	 * @return x to 2 decimal places
	 */
	private static double truncate(double x) {
		if (x > 0)
			return Math.floor(x * 100)/100;
		else
			return Math.ceil(x * 100)/100;
	}
	
	private int oldMouseX = Integer.MIN_VALUE, oldMouseY = Integer.MIN_VALUE, newMouseX, newMouseY;
	
	@Override
	public void mouseReleased(MouseEvent e) {
		//System.out.println(InputEvent.getModifiersExText(e.getModifiersEx()));
		oldMouseX = Integer.MIN_VALUE;
		oldMouseY = Integer.MIN_VALUE;
		
		//calculate and draw new boundary points from the zoom rectangle upon mouse release
		if (Zoom_Rectangle != null) {			
			double min_x_rec = Zoom_Rectangle.getMinX();
			double max_x_rec = Zoom_Rectangle.getMaxX();
			double min_y_rec = Zoom_Rectangle.getMinY();
			double max_y_rec = Zoom_Rectangle.getMaxY();
			double new_min_x = min_x + (max_x - min_x)*(min_x_rec/width);
			double new_max_x = min_x + (max_x - min_x)*(max_x_rec/width);
			double new_max_y = max_y - (max_x - min_y)*(min_y_rec/height);
			double new_min_y = max_y - (max_y - min_y)*(max_y_rec/height);
			min_x = truncate(new_min_x);
			max_x = truncate(new_max_x);
			min_y = truncate(new_min_y);
			max_y = truncate(new_max_y);
			Zoom_Rectangle = null;
			transformPoints();
			repaint();
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {	//Handle PAN and RECTANGLE ZOOM
		//System.out.println(InputEvent.getModifiersExText(e.getModifiersEx()));
		if (oldMouseX == Integer.MIN_VALUE || oldMouseY == Integer.MIN_VALUE) {	//initial mouse position
			oldMouseX = e.getX(); oldMouseY = e.getY();
			return;
		}
		
		newMouseX = e.getX(); newMouseY = e.getY();
		int deltaMouseX = newMouseX - oldMouseX;
		int deltaMouseY = newMouseY - oldMouseY;
		
		//if the left mouse button (button1) is held, execute code for PAN
		if (e.getModifiersEx() == MouseEvent.BUTTON1_DOWN_MASK) {

			double xOffset = -((max_x - min_x)*deltaMouseX/width);
			double yOffset = ((max_y - min_y)*deltaMouseY/height);
			max_x = truncate(max_x + xOffset);
			min_x = truncate(min_x + xOffset);
			max_y = truncate(max_y + yOffset);
			min_y = truncate(min_y + yOffset);
			oldMouseX = newMouseX;
			oldMouseY = newMouseY;
				
			transformPoints();
			repaint();
		}
		//if the right mouse button is held down, execute code for RECTANGLE ZOOM
		else if (e.getModifiersEx() == MouseEvent.BUTTON3_DOWN_MASK) {
			if (Zoom_Rectangle == null) {
				Zoom_Rectangle = new Rectangle2D.Double(oldMouseX, oldMouseY, 0.0D, 0.0D);
				return;
			}
			Zoom_Rectangle.setFrameFromDiagonal(oldMouseX, oldMouseY, newMouseX, newMouseY);
			repaint();
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {	//Handle ZOOM
		
		//System.out.println(e);
		if (e.getWheelRotation() > 0) { //zoom out
			max_x = truncate(max_x + zoom_factor);
			min_x = truncate(min_x - zoom_factor);
			max_y = truncate(max_y + zoom_factor);
			min_y = truncate(min_y - zoom_factor);
		}
		else {	//zoom in
			max_x = truncate(max_x - zoom_factor);
			min_x = truncate(min_x + zoom_factor);
			max_y = truncate(max_y - zoom_factor);
			min_y = truncate(min_y + zoom_factor);
		}
		if (max_x <= min_x) { //make sure the zoom-in has a limit
			max_x = truncate(max_x + zoom_factor);
			min_x = truncate(min_x - zoom_factor);
			return;
		}
		if (max_y <= min_y) { //make sure the zoom-in has a limit
			max_y = truncate(max_y + zoom_factor);
			min_y = truncate(min_y - zoom_factor);
			return;
		}
		transformPoints();
		repaint();
	}
	
	//unused but nevertheless required implements
	@Override
	public void mouseMoved(MouseEvent e) {}
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}

	/**
	 * @return the min_x
	 */
	public double getMin_x() {
		return min_x;
	}

	/**
	 * @param min_x the min_x to set
	 */
	public void setMin_x(double min_x) {
		this.min_x = min_x;
	}

	/**
	 * @return the max_x
	 */
	public double getMax_x() {
		return max_x;
	}

	/**
	 * @param max_x the max_x to set
	 */
	public void setMax_x(double max_x) {
		this.max_x = max_x;
	}

	/**
	 * @return the min_y
	 */
	public double getMin_y() {
		return min_y;
	}

	/**
	 * @param min_y the min_y to set
	 */
	public void setMin_y(double min_y) {
		this.min_y = min_y;
	}

	/**
	 * @return the max_y
	 */
	public double getMax_y() {
		return max_y;
	}

	/**
	 * @param max_y the max_y to set
	 */
	public void setMax_y(double max_y) {
		this.max_y = max_y;
	}
	
    /**
     * Bare-bones custom point class used with javaCalc 2D graphing package.
     * 
     * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
     * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
     *  
     *
     */
	protected class Point {
		private double x, y;
		public Point(double xIn, double yIn) {
			x = xIn;
			y = yIn;
		}
		public double getX() {return x;}
		public double getY() {return y;}
		public void setX(double xIn) {
			x = xIn;
		}
		public void setY(double yIn) {
			y = yIn;
		}
	}
}
