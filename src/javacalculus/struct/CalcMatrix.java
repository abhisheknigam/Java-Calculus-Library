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
		return null;
	}
	
	/**
	 * 
	 * @return every element of this matrix in a 1D array
	 */
	public CalcObject[] getAll() {
		return elements;
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
			buffer.append(matrixDelim);
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
