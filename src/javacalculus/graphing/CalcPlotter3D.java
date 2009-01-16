/**
 * 
 */
package javacalculus.graphing;

/**
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 *  
 *
 */
public interface CalcPlotter3D {
	/**
	 * Returns a Z value given a X and Y value. How the Z value
	 * is obtained is implementation dependent.
	 * @param x (double precision)
	 * @return y coordinate (double precision)
	 */
	public double getZValue(double x, double y);
	
	/**
	 * Returns a string version of the function
	 * that it is currently plotting
	 * @return the function string
	 */
	public String getFunction();
	
	/**
	 * Returns the independent X variable in the function
	 * @return independent variable as a String
	 */
	public String getIndependentXVariable();
	
	/**
	 * Returns the independent Y variable in the function
	 * @return independent variable as a String
	 */
	public String getIndependentYVariable();
}
