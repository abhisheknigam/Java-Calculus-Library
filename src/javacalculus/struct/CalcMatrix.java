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

	public CalcMatrix() {}
	
	public CalcMatrix(int width, int height) {
		elements = new CalcObject[width*height];
		this.width = width;
		this.height = height;
	}
	
	public CalcMatrix(int rows, CalcObject[] elements) {
		if (elements.length % rows != 0) {
			
		}
		this.elements = elements;
		height = rows;
		width = elements.length / rows;
//		width=rows;
//		if(elements.length % rows != 0)
//			throw new CalcDimensionException("Given width and array sizes do not like each other");
//		height = elements.length/width;
//		elements = new CalcObject[width*height];
//		int index=0;
//		for(int i=0; i < width; i++) {
//			for (int j=0; j < height; index++, j++)
//				elements[i][j]=element[index];
//		}
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
				//buffer.append(get(index).toString());
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
