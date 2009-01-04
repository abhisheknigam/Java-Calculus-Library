/**
 * 
 */
package javacalculus.evaluator;

import javacalculus.core.CALC;
import javacalculus.exception.CalcWrongParametersException;
import javacalculus.struct.*;

/**
 * This function evaluator applies the derivative operator to a function with respect to
 * a given variable. An optional "order" parameter is built in to take multiple derivatives.
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 *  
 *
 */
public class CalcDIFF implements CalcFunctionEvaluator {

	/**
	 * 
	 */
	public CalcDIFF() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public CalcObject evaluate(CalcFunction function) {
		if (function.size() == 2) {	//case DIFF(function, variable)
			if (function.get(1) instanceof CalcSymbol) {
				return differentiate(function.get(0), (CalcSymbol)function.get(1));
			}
			else throw new CalcWrongParametersException("DIFF -> 2nd parameter syntax");
		}
		else if (function.size() == 3) { //case DIFF(function, variable, order)
			if (function.get(1) instanceof CalcSymbol) {
				if (function.get(2) instanceof CalcInteger) {
					int order = ((CalcInteger)function.get(2)).intValue();
					if (order == 0) return function.get(0);
					CalcObject returnVal = differentiate(function.get(0), (CalcSymbol)function.get(1));
					order--; //We already took one derivative. Take one away.
					for (int ii = 0; ii < order; ii++) {
						returnVal = differentiate(returnVal, (CalcSymbol)function.get(1));
					}
					return returnVal;
				}
				else throw new CalcWrongParametersException("DIFF -> 3rd parameter syntax");
			}
			else throw new CalcWrongParametersException("DIFF -> 2nd parameter syntax");
		}
		
		else throw new CalcWrongParametersException("DIFF -> wrong number of parameters");
	}
	
	public CalcObject differentiate(CalcObject obj, CalcSymbol var) {
		if (obj instanceof CalcFunction) { //input f(x..xn)
			obj = CALC.SYM_EVAL(obj); //evaluate the function before attempting differentiation
		}
		if (obj.isNumber() || (obj instanceof CalcSymbol && !((CalcSymbol)obj).equals(var))) {	//	(d/dx) c = 0
			return CALC.ZERO; 
		}
		if (obj instanceof CalcSymbol && ((CalcSymbol)obj).equals(var)) { //	(d/dx) x = 1
			return CALC.ONE;
		}
		if (obj.getHeader().equals(CALC.ADD) && ((CalcFunction)obj).size() > 1) { //	(d/dx)(y1+y2+...) = (d/dx)y1 + (d/dx)y2 + ...
			CalcFunction function = (CalcFunction)obj;
			CalcFunction functionB = new CalcFunction(CALC.ADD, function, 1, function.size());
			return CALC.ADD.createFunction(
					differentiate(function.get(0), var),
					differentiate(functionB, var));
		}
		if (obj.getHeader().equals(CALC.MULTIPLY)) {
			CalcFunction function = (CalcFunction)obj;
			CalcObject firstObj = function.get(0);
			if (firstObj.isNumber()) { //	(d/dx) c*y = c * (d/dx) y
				return CALC.MULTIPLY.createFunction(function.get(0), 
						differentiate(new CalcFunction(CALC.MULTIPLY, function, 1, function.size()), var));
			}
			else { //	(d/dx) y1*y2 = y1*(d/dx)y2 + y2*(d/dx)y1
				CalcFunction functionB = new CalcFunction(CALC.MULTIPLY, function, 1, function.size());
				CalcObject secondObj = CALC.SYM_EVAL(functionB);
				return CALC.ADD.createFunction(
						CALC.MULTIPLY.createFunction(firstObj, differentiate(secondObj, var)),
						CALC.MULTIPLY.createFunction(secondObj, differentiate(firstObj, var)));
			}
		}
		if (obj.getHeader().equals(CALC.POWER)) { //this part is probably trickiest
			CalcFunction function = (CalcFunction)obj;
			CalcObject firstObj = function.get(0);
			CalcObject secondObj = function.get(1);
			if (firstObj instanceof CalcFunction || firstObj instanceof CalcSymbol) {
				if (secondObj.isNumber() || secondObj instanceof CalcSymbol && !(secondObj.equals(var))) { //	(d/dx) f^n = n*f^(n-1)*(d/dx)f
					return CALC.MULTIPLY.createFunction(
							secondObj,
							CALC.POWER.createFunction(firstObj, CALC.ADD.createFunction(secondObj, CALC.NEG_ONE)),
							differentiate(firstObj, var));
				}
			}
		}
		if (obj.getHeader().equals(CALC.SIN)) {	//	(d/dx) sin(f(x)) = cos(f(x)) * (d/dx)f(x)
			CalcFunction function = (CalcFunction)obj;
			CalcObject firstObj = function.get(0);
			return CALC.MULTIPLY.createFunction(CALC.COS.createFunction(firstObj), differentiate(firstObj, var)); 
		}
		if (obj.getHeader().equals(CALC.COS)) {	//	(d/dx) cos(f(x)) = -sin(f(x)) * (d/dx)f(x)
			CalcFunction function = (CalcFunction)obj;
			CalcObject firstObj = function.get(0);
			return CALC.MULTIPLY.createFunction(CALC.NEG_ONE, CALC.SIN.createFunction(firstObj), differentiate(firstObj, var)); 
		}
		return CALC.DIFF.createFunction(obj, var); //don't know how to differentiate (yet). Return original expression.
	}
}
