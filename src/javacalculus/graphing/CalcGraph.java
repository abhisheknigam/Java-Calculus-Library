/**
 * 
 */
package javacalculus.graphing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
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
public class CalcGraph extends JComponent implements Runnable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4609667081987450156L;

	protected JFrame frame;
	
	private static final int DEFAULT_WIDTH = 400;
	private static final int DEFAULT_HEIGHT = 400;
	//private static final int DEFAULT_IMAGETYPE = BufferedImage.TYPE_INT_ARGB;
	
	private double 
	min_x = -10,
	max_x = 10,
	min_y = -10,
	max_y = 10,
	resolution = 0.01;

	private int 	width = DEFAULT_WIDTH,
					height = DEFAULT_HEIGHT;
	
	private Color axis_color = Color.BLACK;
	private Color function_color = Color.BLUE;
	private Color background_color = Color.WHITE;
	
	//private BufferedImage graph = new BufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_IMAGETYPE);
	private CalcPlotter plotter;
	private ArrayList<Point> points = new ArrayList<Point>();
	
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
	}
	
	/**
	 * Calculate all the unique points on the graph using boundary and resolution
	 * information. This is where the custom plotter is used.
	 */
	private void populatePoints() {
		double currentX = min_x, currentY;
		
		while (currentX < max_x) {
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
		for (Point p : points) {
			double oldx = p.getX();
			double oldy = p.getY();
			double newx = width * (oldx - min_x)/(max_x - min_x);
			double newy = height * (max_y - oldy)/(max_y - min_y);
			p.setX(newx);
			p.setY(newy);
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		//fill background color
		g.setColor(background_color);
		g.fillRect(0, 0, width, height);
		
		drawAxis(g);
		
		Point currentPoint = points.get(0);

		g.setColor(function_color);
		for (Point p : points) {
			g.drawLine((int)currentPoint.getX(), (int)currentPoint.getY(), (int)p.getX(), (int)p.getY());
			currentPoint = p;
		}
	}

	/**
	 * Draw the axis onto the graph (Graphics g). As of now, only prosaic straight lines.
	 * Will make more elaborate (labels) later.
	 * @param g
	 */
	private void drawAxis(Graphics g) {
		g.setColor(axis_color);
		
		//determine location of the origin using same scaling math used in transformPoints
		int originX = (int)(width * (-min_x)/(max_x - min_x));
		int originY = (int)(height * (max_y)/(max_y - min_y));
		
		//draw x axis
		g.drawLine(0, originY, width, originY);
		//draw y axis
		g.drawLine(originX, 0, originX, height);
	}
	
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
