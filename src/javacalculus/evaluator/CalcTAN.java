/**
 * 
 */
package javacalculus.evaluator;

import javacalculus.core.CALC;
import javacalculus.evaluator.extend.Calc1ParamFunctionEvaluator;
import javacalculus.exception.CalcArithmeticException;
import javacalculus.struct.CalcDouble;
import javacalculus.struct.CalcFraction;
import javacalculus.struct.CalcFunction;
import javacalculus.struct.CalcInteger;
import javacalculus.struct.CalcObject;
import javacalculus.struct.CalcSymbol;

/**
 * Trigonometric Tangent function evaluator. Supports fast evaluation of special values (various multiples of PI and PI fractions).
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 *  
 *
 */
public class CalcTAN extends Calc1ParamFunctionEvaluator {
	
	@Override
	protected CalcObject evaluateObject(CalcObject input) {
		CalcDouble PI = null;
		try {
			PI = (CalcDouble)CALC.PI.evaluate();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		if (input.equals(PI)) {	//TAN(PI) = 0
			return CALC.ZERO;
		}
		if (input instanceof CalcDouble) {
			CalcDouble param = (CalcDouble)input;
			param = param.divide(PI); //retrieve coefficient of pi
			if (param.isInteger()) { //TAN(c*PI) = 0
				return CALC.ZERO;
			}
			if (param.isDivisibleBy(CALC.D_QUARTER)) { //TAN(c*PI/4) = 0
				if (param.isDivisibleBy(CALC.D_HALF)) {
					throw new CalcArithmeticException("TAN(k*PI/2) not defined");
				}
				else {
					param = param.divide(CALC.D_QUARTER);
					param = param.add(CALC.D_NEG_ONE);
					if (param.isDivisibleBy(CALC.D_FOUR) || param.equals(CALC.D_ZERO)) { //TAN(k*PI/4)=1 //TAN(5k*PI/4)=1
						return CALC.ONE;
					}
					else {	//TAN(3k*PI/4)= -1, //TAN(7k*PI/4) = -1
						return CALC.NEG_ONE;
					}
				}
			}
		}
		return null;
	}
	
	@Override
	protected CalcObject evaluateDouble(CalcDouble input) {
		return new CalcDouble(Math.tan(input.doubleValue()));
	}

	@Override
	protected CalcObject evaluateFraction(CalcFraction input) {
		return null;
	}

	@Override
	protected CalcObject evaluateFunction(CalcFunction input) {
		return CALC.TAN.createFunction(input);
	}

	@Override
	protected CalcObject evaluateInteger(CalcInteger input) {
		return new CalcDouble(Math.tan(input.bigIntegerValue().intValue()));
	}

	@Override
	protected CalcObject evaluateSymbol(CalcSymbol input) {
		//cannot evaluate symbols, so just return the original function
		return CALC.TAN.createFunction(input);
	}

}
