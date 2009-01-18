/**
 * 
 */
package javacalculus.evaluator;

import javacalculus.core.CALC;
import javacalculus.evaluator.extend.CalcFunctionEvaluator;
import javacalculus.exception.CalcWrongParametersException;
import javacalculus.graphing.CalcGraph;
import javacalculus.graphing.CalcGraph3D;
import javacalculus.graphing.CalcPlotter;
import javacalculus.graphing.CalcPlotter3D;
import javacalculus.struct.CalcDouble;
import javacalculus.struct.CalcFunction;
import javacalculus.struct.CalcObject;
import javacalculus.struct.CalcSymbol;

/**
 * This evaluator implements the CalcPlotter interface to use the CalcGraph utility for
 * displaying mathematical functions.
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 *  
 */
public class CalcPLOT3D implements CalcFunctionEvaluator, CalcPlotter3D {
	
	/**
	 * The function that's being graphed
	 */
	private CalcFunction function;
	
	@Override
	public CalcObject evaluate(CalcFunction input) {
		if (input.size() == 3) { //should be PLOT(function, independent variable1, independent variable2) -> 3 params
			if (!(input.get(1) instanceof CalcSymbol)) {
				throw new CalcWrongParametersException("PLOT -> 2nd parameter must be a variable.");
			}
			if (!(input.get(2) instanceof CalcSymbol)) {
				throw new CalcWrongParametersException("PLOT -> 3rd parameter must be a variable.");
			}
			else if (!isDuoVariable(input.get(0), (CalcSymbol)input.get(1))) {
				throw new CalcWrongParametersException("PLOT -> input function is not a double variable function of " + input.get(1).toString() + " and " + input.get(2).toString());
			}
			else {
				CalcObject evaled = CALC.SYM_EVAL(input.get(0));
				
				if (evaled == null) {
					function = (CalcFunction)input.get(0);
				}
				else if (evaled instanceof CalcSymbol || evaled.isNumber()) { //case f(x,y)=x, f(x,y) = y, or f(x,y) = c 
					function = new CalcFunction(CALC.ADD, input.get(0));
				}
				else function = (CalcFunction)evaled;
				
				function.removeAllVariables();
				function.addVariable((CalcSymbol)input.get(1));
				function.addVariable((CalcSymbol)input.get(2));
				
				showGraph();
				return input;
			}
		}
		else throw new CalcWrongParametersException("PLOT -> wrong number of parameters.");
	}
	
	/**
	 * Create and show the graph
	 */
	private void showGraph() {
		new CalcGraph3D(this);
	}
	
	/**
	 * Determines if the function <b>input</b> is single-variable in
	 * <b>var</b> and only contains numeric literals and defined numeric constants.
	 * Not even gonna THINK about higher dimension graphing... *shudder*.
	 * @param input
	 * @param var
	 * @return
	 */
	private boolean isDuoVariable(CalcObject input, CalcSymbol var) {
		return true; //TODO code this later.. only a foolproof measure. Not important yet.
	}
	
	@Override
	public String getFunction() {
		return function.toString();
	}

	@Override
	public double getZValue(double x, double y) {
		CalcObject result = CalcSUB.numericSubstitute(function, function.getVariable(0), new CalcDouble(x));
		result = CalcSUB.numericSubstitute(result, function.getVariable(1), new CalcDouble(y));
		//TestAPI.print(result.toString());
		
		result = CALC.SYM_EVAL(result);
		
		if (result == null) return Double.NaN;
		
		if (result.isNumber()) { //if is number, return it
			try {
				return Double.parseDouble(result.toString());
			}
			catch (NumberFormatException e) {
				return Double.NaN;
			}
		}
		else { //else return ... not a number
			return Double.NaN;
		}
	}

	@Override
	public String getIndependentXVariable() {
		return function.getVariable(0).toString();
	}
	
	@Override
	public String getIndependentYVariable() {
		return function.getVariable(1).toString();
	}
}

