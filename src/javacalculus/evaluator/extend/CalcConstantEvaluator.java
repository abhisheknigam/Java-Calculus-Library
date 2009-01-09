/**
 * 
 */
package javacalculus.evaluator.extend;

import javacalculus.struct.CalcFunction;
import javacalculus.struct.CalcObject;

/**
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 *  
 *
 */
public class CalcConstantEvaluator implements CalcFunctionEvaluator {
	
	CalcObject constant;
	/**
	 * 
	 */
	public CalcConstantEvaluator(CalcObject obj) {
		constant = obj;
	}

	/* (non-Javadoc)
	 * @see javacalculus.evaluator.CalcFunctionEvaluator#evaluate(javacalculus.struct.CalcFunction)
	 */
	@Override
	public CalcObject evaluate(CalcFunction input) {
		return null;
	}
	
	public CalcObject getValue() {
		return constant;
	}

}
