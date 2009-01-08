/**
 * 
 */
package javacalculus.evaluator;

import test.TestAPI;
import javacalculus.core.CALC;
import javacalculus.exception.CalcWrongParametersException;
import javacalculus.struct.CalcDouble;
import javacalculus.struct.CalcFunction;
import javacalculus.struct.CalcObject;
import javacalculus.struct.CalcSymbol;

/**
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 *  
 *
 */
public class CalcSUB implements CalcFunctionEvaluator {

	/**
	 * Constructor
	 */
	public CalcSUB() {}

	/* (non-Javadoc)
	 * @see javacalculus.evaluator.CalcFunctionEvaluator#evaluate(javacalculus.struct.CalcFunction)
	 */
	@Override
	public CalcObject evaluate(CalcFunction input) {
		CalcObject functionDefinition = CALC.getDefinedVariable(input.getHeader());
		
		if (functionDefinition == null) return null;
		
		if (functionDefinition.isNumber()) {
			return functionDefinition; //function is a constant. No substitution needed.
		}
		else if (functionDefinition instanceof CalcFunction){
			CalcFunction function = (CalcFunction) functionDefinition;
			int numberOfVars = CALC.getDefinedVariableKey(function).getNumberOfVariables();
			if (input.size() != numberOfVars) 
				throw new CalcWrongParametersException("SUB -> wrong number of variables");
			else return traverseAndSubstitute(function, input);
		}
		else return functionDefinition;	
	}
	
	public CalcObject traverseAndSubstitute(CalcFunction definition, CalcFunction substitutions) {
		CalcFunction returnVal = new CalcFunction(definition.getHeader());
		
		for (int ii = 0; ii < definition.size(); ii++) {
			CalcObject currentTerm = definition.get(ii);
			if (currentTerm.isNumber()) {
				returnVal.add(currentTerm);
			}
			else if (currentTerm instanceof CalcSymbol) {
				int index = CALC.getDefinedVariableKey(definition).getVariableIndex((CalcSymbol)currentTerm);
				//make the substitution if the symbol is one the substitution variables
				if (index != -1) returnVal.add(substitutions.get(index));
				else returnVal.add(currentTerm);
			}
			else if (currentTerm instanceof CalcFunction) {
				returnVal.add(traverseAndSubstitute((CalcFunction)currentTerm, substitutions));
			}
			else returnVal.add(currentTerm);
		}
		
		return returnVal;
	}

	public static CalcFunction numericSubstitute(CalcFunction input, CalcSymbol variable, CalcDouble number) {
		CalcFunction result = new CalcFunction(input.getHeader());
		for (CalcObject obj : input) {
			if (obj.equals(variable)) {
				result.add(number);
			}
			else if (obj instanceof CalcFunction) {
				result.add(numericSubstitute((CalcFunction)obj, variable, number));
			}
			else result.add(obj);
		}
		return result;
	}
}
