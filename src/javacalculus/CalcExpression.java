/**
 * 
 */
package javacalculus;

/**
 * @author Duyun Chen
 *
 */
public class CalcExpression 
{
	private String expression;
	private CalcFunction function;
	
	public CalcExpression(String exp) {
		expression = exp;
		function = parseExpressionString(expression);
	}
	
	private CalcFunction parseExpressionString(String in) {
		CalcFunction out = new CalcFunction();
		
		for (int ii = 0; ii < in.length(); ii++) {
			for (CalcOperation op : CalcOperation.values()) {
				if (op.getName().charAt(0) == (in.charAt(ii))) {
					out.addOperation(op);
				}
			}
		}
		
		return out;
	}
	
	public CalcFunction getFunction()
	{return function;}
}
