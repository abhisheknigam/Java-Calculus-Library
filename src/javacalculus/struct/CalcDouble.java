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
	
	public CalcDouble() {
		value = new BigDecimal(0.0d);
	}
	
	public CalcDouble(BigDecimal bigDecimalIn) {
		value = bigDecimalIn;
	}
	
	public CalcDouble(double doubleIn) {
		value = new BigDecimal(doubleIn, CALC.mathcontext);
	}
	
	public CalcDouble(String stringIn) {
		value = new BigDecimal(stringIn, CALC.mathcontext);
	}
	
	public CalcDouble(CalcInteger calcIntegerIn) {
		value = new BigDecimal(calcIntegerIn.bigIntegerValue());
	}
	
	public double doubleValue() {
		return value.doubleValue();
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
		return new StringBuffer(value.toString());
	}
	
	@Override
	public String toString() {
		return value.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CalcDouble) {
			return value.doubleValue() == (((CalcDouble)obj).doubleValue());
		}
		return false;
	}

	@Override
	public boolean isNumber() {
		// TODO Auto-generated method stub
		return true;
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
