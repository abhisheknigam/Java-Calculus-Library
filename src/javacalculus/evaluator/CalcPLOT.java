/**
 * 
 */
package javacalculus.evaluator;

import javacalculus.core.CALC;
import javacalculus.exception.CalcWrongParametersException;
import javacalculus.graphing.CalcGraph;
import javacalculus.graphing.CalcPlotter;
import javacalculus.struct.CalcDouble;
import javacalculus.struct.CalcFunction;
import javacalculus.struct.CalcObject;
import javacalculus.struct.CalcSymbol;
import test.TestAPI;

/**
 * This evaluator implements the CalcPlotter interface to use the CalcGraph utility for
 * displaying mathematical functions.
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 *  
 */
public class CalcPLOT implements CalcFunctionEvaluator, CalcPlotter {
	
	/**
	 * The function that's being graphed
	 */
	private CalcFunction function;
	
	@Override
	public CalcObject evaluate(CalcFunction input) {
		if (input.size() == 2) { //should be PLOT(function, independent variable) -> 2 params
			if (!(input.get(1) instanceof CalcSymbol)) {
				throw new CalcWrongParametersException("PLOT -> 2nd parameter must be a variable.");
			}
			else if (!isMonoVariable(input.get(0), (CalcSymbol)input.get(1))) {
				throw new CalcWrongParametersException("PLOT -> input function is not a single variable function of " + input.get(1).toString());
			}
			else {
				if (input.get(0) instanceof CalcSymbol) { //case f(x)=x
					function = new CalcFunction(CALC.ADD, input.get(0));
				}
				else function = (CalcFunction)input.get(0);
				
				function.addVariable((CalcSymbol)input.get(1));
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
		new CalcGraph(this);
	}
	
	/**
	 * Determines if the function <b>input</b> is single-variable in
	 * <b>var</b> and only contains numeric literals and defined numeric constants.
	 * Not even gonna THINK about higher dimension graphing... *shudder*.
	 * @param input
	 * @param var
	 * @return
	 */
	private boolean isMonoVariable(CalcObject input, CalcSymbol var) {
		return true; //TODO code this later.. only a foolproof measure. Not important yet.
	}
	
	@Override
	public String getFunction() {
		return function.toString();
	}

	@Override
	public double getYValue(double x) {
		CalcObject result = CalcSUB.numericSubstitute(function, function.getVariable(0), new CalcDouble(x));
		//TestAPI.print(result.toString());
		try {
			result = result.evaluate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (result.isNumber()) { //if is number, return it
			return Double.parseDouble(result.toString());
		}
		else { //else return ... not a number
			return Double.NaN;
		}
	}

	@Override
	public String getIndependentVariable() {
		return function.getVariable(0).toString();
	}
}

