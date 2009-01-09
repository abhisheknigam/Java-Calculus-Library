package javacalculus.evaluator.extend;

import javacalculus.struct.CalcFunction;
import javacalculus.struct.CalcObject;

/**
 * Standard interface for any symbolic or numeric evaluator of functions.
 * Every CalcSymbol that represents a function must have a function evaluator.
 * 
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 *  
 *
 */
public interface CalcFunctionEvaluator {
	/*
	 * Evaluate the given function if possible. Otherwise should return null.
	 */
	public CalcObject evaluate(CalcFunction input);
}
