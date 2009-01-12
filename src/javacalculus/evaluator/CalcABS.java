/**
 * 
 */
package javacalculus.evaluator;

import javacalculus.core.CALC;
import javacalculus.evaluator.extend.Calc1ParamFunctionEvaluator;
import javacalculus.evaluator.extend.CalcOperatorEvaluator;
import javacalculus.struct.CalcDouble;
import javacalculus.struct.CalcFraction;
import javacalculus.struct.CalcFunction;
import javacalculus.struct.CalcInteger;
import javacalculus.struct.CalcObject;
import javacalculus.struct.CalcSymbol;

/**
 * Takes the absolute value of an object. Parsing token is the paired '|' or the 
 * function literal ABS();
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 *  
 *
 */
public class CalcABS extends Calc1ParamFunctionEvaluator implements CalcOperatorEvaluator {

	/**
	 * 
	 */
	public CalcABS() {}
	
	@Override
	public CalcObject evaluateObject(CalcObject obj) {
		return null;
	}

	@Override
	protected CalcObject evaluateDouble(CalcDouble input) {
		if (input.isNegative()) return input.negate();
		return input;
	}

	@Override
	protected CalcObject evaluateFraction(CalcFraction input) {
		if (input.isNegative()) input.negate();
		return input;
	}

	@Override
	protected CalcObject evaluateFunction(CalcFunction input) {
		return CALC.ABS.createFunction(input);
	}

	@Override
	protected CalcObject evaluateInteger(CalcInteger input) {
		if (input.isNegative()) return input.negate();
		else return input;
	}

	@Override
	protected CalcObject evaluateSymbol(CalcSymbol input) {
		return CALC.ABS.createFunction(input);
	}

	@Override
	public int getPrecedence() {
		return 700;
	}

	@Override
	public String toOperatorString(CalcFunction function) {
		StringBuffer buffer = new StringBuffer();
		char operatorChar = '|';
		CalcObject temp = function.get(0);
		
//    	if (temp.getPrecedence() < getPrecedence()) {
//    		buffer.append('(');
//    	}

    	buffer.append(operatorChar + temp.toString() + operatorChar);

//    	if (temp.getPrecedence() < getPrecedence()) {
//    		buffer.append(')');
//    	}
//    	
    	return buffer.toString();		
	}
}
