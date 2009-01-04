/**
 * 
 */
package test;
import javacalculus.*;

/**
 * @author Seth
 *
 */
public class ExpressionTest {
	public static void main (String args[])
	{
		String e=Expression.eval("ln(2+3^-cos(1)+5.21-389/tan(Pi-1))");
		System.out.println(e);		
	}

}
