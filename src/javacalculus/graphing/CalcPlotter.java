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
public interface CalcPlotter {
	/**
	 * Returns a Y value given a X value. How the Y value
	 * is obtained is implementation dependent.
	 * @param x (double precision)
	 * @return y coordinate (double precision)
	 */
	public double getYValue(double x);
	
	/**
	 * Returns a string version of the function
	 * that it is currently plotting
	 * @return the function string
	 */
	public String getFunction();
}
