package javacalculus.struct;

import java.math.BigDecimal;

import javacalculus.core.CALC;

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
	
	public CalcDouble() {
		value = new BigDecimal(0.0d);
	}
	
	public CalcDouble(BigDecimal bigDecimalIn) {
		value = bigDecimalIn;
	}
	
	public CalcDouble(double doubleIn) {
		if (doubleIn != Double.POSITIVE_INFINITY && doubleIn != Double.NEGATIVE_INFINITY) {
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
			return value.doubleValue() == (((CalcDouble)obj).doubleValue());
		}
		return false;
	}

	@Override
	public boolean isNumber() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public boolean isPositiveInfinity() {
		return isPositiveInfinity;
	}
	
	public boolean isNegativeInfinity() {
		return isNegativeInfinity;
	}

	@Override
	public CalcSymbol getHeader() {
		return CALC.DOUBLE;
	}

	public CalcDouble add(CalcDouble input) {
		return new CalcDouble(value.add(input.bigDecimalValue()));
	}

	public CalcDouble multiply(CalcDouble input) {
		return new CalcDouble(value.multiply(input.bigDecimalValue()));
	}
	
	public CalcDouble divide(CalcDouble input) {
		return new CalcDouble(value.divide(input.bigDecimalValue()));
	}
	
	public CalcDouble power(CalcDouble input) {
		return new CalcDouble(Math.pow(doubleValue(), input.doubleValue()));
	}
	
	public CalcDouble mod(CalcDouble input) {
		return new CalcDouble(value.remainder(input.bigDecimalValue()));
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
		else value = value.negate();
	}
	
	public boolean isInteger() {
		return (mod(CALC.D_ONE).equals(CALC.D_ZERO));
	}

	@Override
	public int compareTo(CalcObject obj) {
		if (getHierarchy() > obj.getHierarchy()) {
			return 1;
		}
		else if (getHierarchy() < obj.getHierarchy()) {
			return -1;
		}
		return 0;
	}

	@Override
	public int getHierarchy() {
		return CalcObject.DOUBLE;
	}

	@Override
	public int getPrecedence() {
		if (value.compareTo(BigDecimal.ZERO) < 0) {
			return 100;
		}
		return 9999999; //it's over NINE MILLION AGAIN!!!!
	}
}
