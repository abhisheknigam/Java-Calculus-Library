/**
 * 
 */
package test;
import java.io.IOException;
import java.util.Scanner;

import javacalculus.*;

/**
 * @author Seth
 *
 *
 */

public class ExpressionTest {
		public static void main (String args[])
		{
			Scanner in = new Scanner(System.in);
			//String e=Expression.eval("ln(x+3^-cos(1)+5.21-394/tan(Pi-1))");
			//String f=Expression.diff("3*x^2","x");
			//String g=Expression.eval("2^-33+5*x/(5-4)","x=2");
			while (true) {
				System.out.println("Math Shitz? ");
				String input = in.next();
				System.out.println(ExpressionTools.simplify(input));
			}
			
		}
}