/**
 * 
 */
package stwing.calc;

import java.util.Queue;
import java.util.Stack;

/**
 * @author Duyun Chen
 *
 */
public class CalcFunction {
	private Stack<CalcOperation> operations;
	
	public CalcFunction() {
		operations = new Stack<CalcOperation>();
	}
	
	public void addOperation(CalcOperation op)
	{
		operations.add(op);
	}
	
	public double evaluate(double in) {
		double output = in;
		if (operations.size() == 0) {
			System.out.println("No operations available");
			return in;
		}
		for (CalcOperation ii : operations) {
			System.out.println("Appyling Operation " + ii);
			switch (ii)
			{
				case ADD:
					output += in;
					break;
				case SUBTRACT:
					output -= in;
					break;
				case MULTIPLY:
					output *= in;
					break;
				case DIVIDE:
					output /= in;
					break;
			}
		}
		
		return output;
	}
}
