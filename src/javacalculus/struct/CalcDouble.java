package javacalculus.struct;

import java.math.BigDecimal;

import javacalculus.core.CALC;
import javacalculus.exception.CalcUnsupportedException;

/**
 * 
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 * @see java.math.BigDecimal 
 *
 */
public class CalcDouble implements CalcObject {
	private BigDecimal value;
	private boolean isPositiveInfinity = false;
	private boolean isNegativeInfinity = false;
	private boolean isNaN = false;
	
	public CalcDouble() {
		value = new BigDecimal(0.0d);
	}
	
	public CalcDouble(BigDecimal bigDecimalIn) {
		value = bigDecimalIn;
	}
	
	public CalcDouble(double doubleIn) {
		if (!Double.isNaN(doubleIn) && !Double.isInfinite(doubleIn)) {
			value = new BigDecimal(doubleIn, CALC.mathcontext);
		}	
		else { //mathcontext does not apply for infinitesimal values
			value = null;
			if (doubleIn == Double.POSITIVE_INFINITY) {
				isPositiveInfinity = true;
			}
			if (doubleIn == Double.NEGATIVE_INFINITY) {
				isNegativeInfinity = true;
			}
			if (Double.isNaN(doubleIn)) {
				isNaN = true;
			}
		}
	}
	
	public CalcDouble(String stringIn) {
		value = new BigDecimal(stringIn, CALC.mathcontext);
	}
	
	public CalcDouble(CalcInteger calcIntegerIn) {
		value = new BigDecimal(calcIntegerIn.bigIntegerValue());
	}
	
	public double doubleValue() {
		if (isPositiveInfinity) {
			return Double.POSITIVE_INFINITY;
		}
		else if (isNegativeInfinity) {
			return Double.NEGATIVE_INFINITY;
		}
		else if (isNaN) {
			return Double.NaN;
		}
		else return value.doubleValue();
	}
	
	public BigDecimal bigDecimalValue() {
		return value;
	}
	
	@Override
	public CalcObject evaluate() {
		return this;
	}
	
	@Override
	public StringBuffer toStringBuffer() {
		if (isPositiveInfinity) {
			return new StringBuffer("INFINITY");
		}
		else if (isNegativeInfinity) {
			return new StringBuffer("-INFINITY");
		}
		else if (isNaN) {
			return new StringBuffer("NaN");
		}
		else return new StringBuffer(value.toString());
	}
	
	@Override
	public String toString() {
		if (isPositiveInfinity) {
			return "INFINITY";
		}
		else if (isNegativeInfinity) {
			return "-INFINITY";
		}
		else if (isNaN) {
			return "NaN";
		}
		else return value.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CalcDouble) {
			if (isPositiveInfinity) {
				return ((CalcDouble)obj).isPositiveInfinity();
			}
			if (isNegativeInfinity) {
				return ((CalcDouble)obj).isNegativeInfinity();
			}
			if (isNaN) {
				return ((CalcDouble)obj).isNaN();
			}
			return value.doubleValue() == (((CalcDouble)obj).doubleValue());
		}
		else if (obj instanceof CalcInteger) {
			if (value == null) return false;
			else return value.doubleValue() == (double)((CalcInteger)obj).intValue();
		}
		else return false;
	}

	@Override
	public boolean isNumber() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public boolean isEven() {
		return mod(CALC.D_TWO).equals(CALC.D_ZERO);
	}
	
	public boolean isPositiveInfinity() {
		return isPositiveInfinity;
	}
	
	public boolean isNegativeInfinity() {
		return isNegativeInfinity;
	}
	
	public boolean isNaN() {
		return isNaN;
	}

	@Override
	public CalcSymbol getHeader() {
		return CALC.DOUBLE;
	}

	public CalcDouble add(CalcDouble input) {
		return new CalcDouble(value.add(input.bigDecimalValue()));
	}

	public CalcDouble multiply(CalcDouble input) {
		if (isNaN || input.isNaN()) return new CalcDouble(Double.NaN);
		return new CalcDouble(value.multiply(input.bigDecimalValue()));
	}
	
	public CalcDouble divide(CalcDouble input) {
		if (isNaN || input.isNaN()) return new CalcDouble(Double.NaN);
		return new CalcDouble(value.divide(input.bigDecimalValue(), CALC.mathcontext));
	}
	
	public CalcDouble power(CalcDouble input) {
		if (isNaN || input.isNaN()) return new CalcDouble(Double.NaN);
		return new CalcDouble(Math.pow(doubleValue(), input.doubleValue()));
	}
	
	public CalcDouble mod(CalcDouble input) {
		return new CalcDouble(value.remainder(input.bigDecimalValue()));
	}
	
	public boolean isDivisibleBy(CalcDouble input) {
		return mod(input).equals(CALC.D_ZERO);
	}
	
	public void negate() {
		if (isPositiveInfinity) {
			isPositiveInfinity = false;
			isNegativeInfinity = true;
		}
		else if (isNegativeInfinity) {
			isNegativeInfinity = false;
			isPositiveInfinity = true;
		}
		else if (isNaN) {
			return;
		}
		else value = value.negate();
	}
	
	public boolean isInteger() {
		if (isNaN || isPositiveInfinity || isNegativeInfinity) return false;
		return (mod(CALC.D_ONE).equals(CALC.D_ZERO));
	}

	@Override
	public int compareTo(CalcObject obj) {
		if (obj.isNumber()) {
			if (obj instanceof CalcInteger) {
				if (value.doubleValue() < (double)((CalcInteger)obj).intValue()) {
					return -1;
				}
				else if (value.doubleValue() > (double)((CalcInteger)obj).intValue()) {
					return 1;
				}
				else return 0;
			}
			else if (obj instanceof CalcDouble) {
				if (isPositiveInfinity) {
					if (((CalcDouble)obj).isPositiveInfinity())
						return 0;
					else if (((CalcDouble)obj).isNegativeInfinity())
						return 1;
					else return 1;
				}
				else if (isNegativeInfinity) {
					if (((CalcDouble)obj).isPositiveInfinity())
						return -1;
					else if (((CalcDouble)obj).isNegativeInfinity())
						return 0;
					else return -1;
				}
				else if (isNaN || ((CalcDouble)obj).isNaN()) {
					return 0;
				}
				else return value.compareTo(((CalcDouble)obj).bigDecimalValue());
			}
			else throw new CalcUnsupportedException(obj.toString());
		}
		if (getHierarchy() > obj.getHierarchy()) {
			return 1;
		}
		else if (getHierarchy() < obj.getHierarchy()) {
			return -1;
		}
		else return 0;
	}

	@Override
	public int getHierarchy() {
		return CalcObject.DOUBLE;
	}

	@Override
	public int getPrecedence() {
		if (value.compareTo(BigDecimal.ZERO) < 0) {
		//	return 100;
		}
		return 9999999; //it's over NINE MILLION AGAIN!!!!
	}
}
