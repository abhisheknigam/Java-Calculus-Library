package javacalculus.evaluator;

import javacalculus.struct.CalcFunction;

/**
 * Evaluates a special case of functions: operators
 * 
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 *  
 *
 */
public interface CalcOperatorEvaluator extends CalcFunctionEvaluator {
	
	/**
	 * Converts a function into a special operator notation
	 * @param function input function
	 * @return operator notation String
	 */
	public String toOperatorString(CalcFunction function);
	
	/**
	 * 
	 * @return the precedence of the operator
	 */
	public int getPrecedence();
}
