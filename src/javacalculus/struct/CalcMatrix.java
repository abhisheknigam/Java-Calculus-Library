/**
 * 
 */
package javacalculus.struct;

import javacalculus.core.CALC;
import javacalculus.exception.CalcDimensionException;

/**
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 *  
 *
 */
public class CalcMatrix implements CalcObject {
	
	private CalcObject[] elements;
	private int width = 0, height = 0;
	
	public static final char matrixOpen = '[';
	public static final char matrixClose = ']';
	public static final char matrixDelim = ',';
	
	/**
	 * Default constructor
	 */
	public CalcMatrix() {}
	
	public CalcMatrix(int width, int height) {
		elements = new CalcObject[width*height];
		this.width = width;
		this.height = height;
	}
	
	public CalcMatrix(int rows, CalcObject[] elements) {
		if (elements.length % rows != 0) {
			throw new CalcDimensionException("CalcMatrix -> Element dimension mismatch");
		}
		this.elements = elements;
		height = rows;
		width = elements.length / rows;
	}
	
	/**
	 * Returns the element matrix at i = <b>row</b> and j = <b>col</b> 
	 * @param row
	 * @param col
	 * @return
	 */
	public CalcObject get(int row, int col) {
		return elements[row*width + col];
	}
	
	/**
	 * 
	 * @return every element of this matrix in a 1D array
	 */
	public CalcObject[] getAll() {
		return elements;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public CalcObject add(CalcMatrix input) {
		if (input.getWidth() != width || input.getHeight() != height) {
			throw new CalcDimensionException("Adding matrices of different dimensions");
		}
		
		CalcObject[] inputElements = input.getAll();
		
		for (int ii = 0; ii < elements.length; ii++) {
			elements[ii] = CALC.ADD.createFunction(elements[ii], inputElements[ii]);
		}
		
		return this;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		int index = 0;
		
		buffer.append(matrixOpen);
		
		for (int ii = 0; ii < height; ii++) {
			buffer.append(matrixOpen);
			for (int jj = 0; jj < width; jj++) {
				if (jj > 0) buffer.append(matrixDelim);
				buffer.append(get(ii, jj).toString());
				index++;
			}
			buffer.append(matrixClose);
			if (ii < height - 1) buffer.append(matrixDelim);
		}
		
		buffer.append(matrixClose);
		
		return buffer.toString();
	}

	@Override
	public int compareTo(CalcObject obj) {
		//TODO figure out how to determine if one matrix is greater than/less than than another
		return 0;
	}
	
	@Override
	public CalcObject evaluate() throws Exception {
		//evaluate every entry first
		for (int ii = 0; ii < elements.length; ii++) {
			elements[ii] = CALC.SYM_EVAL(elements[ii]);
		}
		return this;
	}
	
	@Override
	public CalcSymbol getHeader() {
		return CALC.MATRIX;
	}
	
	@Override
	public int getHierarchy() {
		return CalcObject.MATRIX;
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
