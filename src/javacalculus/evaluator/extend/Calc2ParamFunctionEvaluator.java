package javacalculus.evaluator.extend;

import javacalculus.core.CALC;
import javacalculus.struct.*;

/**
 * Abstract definition of a function evaluator that takes in exactly two parameters.
 * 
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 *  
 *
 */
public abstract class Calc2ParamFunctionEvaluator implements CalcFunctionEvaluator {

	@Override
	public CalcObject evaluate(CalcFunction input) {
		if (input.size() == 2) {
			CalcObject returnVal = evaluateBinary(input.get(0), input.get(1));
			return (returnVal == null)? input:returnVal;
		}
		else return null;
	}
	
	protected CalcObject evaluateBinary(CalcObject parameter1, CalcObject parameter2) {
			
		CalcObject returnVal = evaluateObject(parameter1, parameter2);
		
		if (returnVal != null) return returnVal;
		
		else if (parameter1 instanceof CalcInteger) {
			if (parameter2 instanceof CalcInteger) {
				return evaluateInteger((CalcInteger)parameter1, (CalcInteger)parameter2);
			}
			else if (parameter2 instanceof CalcFraction) {
				return evaluateFraction(new CalcFraction((CalcInteger)parameter1, CALC.ONE), (CalcFraction)parameter2);
			}
			else if (parameter2 instanceof CalcDouble) {
				return evaluateDouble(new CalcDouble((CalcInteger)parameter1), (CalcDouble)parameter2);
			}
			else return null;
		}
		else if (parameter1 instanceof CalcFraction) {
			if (parameter2 instanceof CalcInteger) {
				return evaluateFraction((CalcFraction)parameter1, new CalcFraction((CalcInteger)parameter2, CALC.ONE));
			}
			else if (parameter2 instanceof CalcFraction) {
				return evaluateFraction(new CalcFraction((CalcInteger)parameter1, CALC.ONE), (CalcFraction)parameter2);
			}
			else return null;
		}
		else if (parameter1 instanceof CalcDouble) {
			if (parameter2 instanceof CalcDouble) {
				return evaluateDouble((CalcDouble)parameter1, (CalcDouble)parameter2);
			}
			else if (parameter2 instanceof CalcInteger) {
				return evaluateDouble((CalcDouble)parameter1, new CalcDouble((CalcInteger)parameter2));
			}
			else return null;
		}
		else if (parameter1 instanceof CalcFunction) {
			if (parameter2 instanceof CalcFunction) {
				return evaluateFunction((CalcFunction)parameter1, (CalcFunction)parameter2);
			}
			else if (parameter2 instanceof CalcInteger) {
				return evaluateFunctionAndInteger((CalcFunction)parameter1, (CalcInteger)parameter2);
			}
			else return null;
		}
		else if (parameter1 instanceof CalcSymbol && parameter2 instanceof CalcSymbol) {
			return evaluateSymbol((CalcSymbol)parameter1, (CalcSymbol)parameter2);
		}
		else return null;
	}
	
	protected abstract CalcObject evaluateObject(CalcObject input1, CalcObject input2);
	protected abstract CalcObject evaluateInteger(CalcInteger input1, CalcInteger input2);
	protected abstract CalcObject evaluateDouble(CalcDouble input1, CalcDouble input2);
	protected abstract CalcObject evaluateFraction(CalcFraction input1, CalcFraction input2);
	protected abstract CalcObject evaluateSymbol(CalcSymbol input1, CalcSymbol input2);
	protected abstract CalcObject evaluateFunction(CalcFunction input1, CalcFunction input2);
	protected abstract CalcObject evaluateFunctionAndInteger(CalcFunction input1, CalcInteger input2);

}
