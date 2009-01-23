package javacalculus.struct;

import java.util.ArrayList;

import javacalculus.core.CALC;
import javacalculus.exception.CalcDimensionException;
import javacalculus.struct.CalcFunction;
import javacalculus.struct.CalcObject;
import javacalculus.struct.CalcSymbol;

/**
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 *  
 *
 */
public class CalcVector implements CalcObject {
	
	private CalcObject[] elements;
	private static final char VECTOR_OPEN = '[';
	private static final char VECTOR_CLOSE = ']';
	private static final char VECTOR_DELIM = ',';
	
	/**
	 * Constructor for a zero vector of length len
	 * @param len
	 */
	public CalcVector(int len) {
		elements = new CalcObject[len];
		
		for (int ii = 0; ii < len; ii++) {
			elements[ii] = CALC.ZERO;
		}
	}
	
	/**
	 * Construct a vector from a given array of elements
	 * @param elements
	 */
	public CalcVector(CalcObject[] elements) {
		this.elements = elements;
	}
	
	public int size() {
		return elements.length;
	}
	
	public CalcObject get(int index) {
		return elements[index];
	}
	
	public CalcObject[] getAll() {
		return elements;
	}
	
	/**
	 * Returns the sum of this vector and another vector
	 * @param input
	 */
	public CalcVector add(CalcVector input) {
		if (input.size() != size()) {
			throw new CalcDimensionException("Different dimensions in vector addition");
		}
		CalcObject[] inputElements = input.getAll();
		
		for (int ii = 0; ii < elements.length; ii++) {
			elements[ii] = CALC.ADD.createFunction(elements[ii], inputElements[ii]);
		}
		
		return this;
	}
	
	public CalcVector multiply(CalcObject input) {
		for (int ii = 0; ii < elements.length; ii++) {
			elements[ii] = CALC.MULTIPLY.createFunction(input, elements[ii]);
		}
		return this;
	}
	
	/**
	 * Returns the dot product between this vector and another vector
	 * @param input
	 */
	public CalcObject dot(CalcVector input) {	//computes the dot product between two vectors
		CalcFunction returnVal = CALC.ADD.createFunction();
		
		if (size() != input.size()) {
			throw new CalcDimensionException("Mismatched vector sizes in dot product");
		}
		
		CalcObject[] inputElements = input.getAll();
		
		for (int ii = 0; ii < elements.length; ii++) {
			returnVal.add(CALC.MULTIPLY.createFunction(elements[ii], inputElements[ii]));
		}
		
		return returnVal;
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
	public CalcObject evaluate() throws Exception {
		for (int ii = 0; ii < elements.length; ii++) {	//evaluate each element first
			elements[ii] = CALC.SYM_EVAL(elements[ii]);
		}
		return this;
	}

	@Override
	public CalcSymbol getHeader() {
		return CALC.VECTOR;
	}

	@Override
	public int getHierarchy() {
		return CalcObject.VECTOR;
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
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(VECTOR_OPEN);
		
		for (int ii = 0; ii < elements.length; ii++) {
			if (ii > 0) buffer.append(VECTOR_DELIM);
			
			buffer.append(elements[ii].toString());
		}
		
		buffer.append(VECTOR_CLOSE);
		
		return buffer.toString();
	}
}
