/**
 * 
 */
package javacalculus.struct;

import java.util.ArrayList;

import javacalculus.core.CALC;
import javacalculus.exception.CalcDimensionException;

/**
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 *  
 *
 */
public class CalcVector implements CalcObject {
	
	
	public CalcVector(String string) {
		super(new CalcSymbol(string));
	}
	
	public CalcVector(CalcSymbol calcSymbol) {
		super(calcSymbol);
	}

	public CalcVector(CalcSymbol calcSymbol, ArrayList<CalcObject> elements) {
		super(calcSymbol, elements);
	}
	
	public CalcObject dot(CalcVector input) {
		CalcFunction returnVal = CALC.ADD.createFunction();
		
		if (size() != input.size()) {
			throw new CalcDimensionException("Mismatched vector sizes in dot product");
		}
		
		for (int ii = 0; ii < parameters.size(); ii++) {
			returnVal.add(CALC.MULTIPLY.createFunction(parameters.get(ii), input.get(ii)));
		}
		
		return returnVal;
	}

	@Override
	public int compareTo(CalcObject obj) {
		return 0;
	}

	@Override
	public CalcObject evaluate() throws Exception {
		return this;
	}

	@Override
	public CalcSymbol getHeader() {
		return null;
	}

	@Override
	public int getHierarchy() {
		return 0;
	}

	@Override
	public int getPrecedence() {
		return 0;
	}

	@Override
	public boolean isNumber() {
		return false;
	}

	@Override
	public StringBuffer toStringBuffer() {
		return null;
	}
}
