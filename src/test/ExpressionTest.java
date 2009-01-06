/**
 * 
 */
package test;
import javacalculus.*;

/**
 * @author Seth
 *
 *
 */
public class ExpressionTest {
		public static void main (String args[])
		{

			//String e=Expression.eval("ln(x+3^-cos(1)+5.21-394/tan(Pi-1))");
			//String f=Expression.diff("3*x^2","x");
			String g=Expression.eval("2^-33+5*x/(5-4)","x=2");
			String h=ExpressionTools.simplify("2+(x-2)");
			//System.out.println(e);
			//System.out.println(f);	
			//System.out.println(g);
			System.out.println(h);
		}
}