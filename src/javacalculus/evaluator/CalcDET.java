/**
 * 
 */
package javacalculus.evaluator;

import javacalculus.core.CALC;
import javacalculus.evaluator.extend.Calc1ParamFunctionEvaluator;
import javacalculus.exception.CalcDimensionException;
import javacalculus.exception.CalcWrongParametersException;
import javacalculus.struct.CalcDouble;
import javacalculus.struct.CalcFraction;
import javacalculus.struct.CalcFunction;
import javacalculus.struct.CalcInteger;
import javacalculus.struct.CalcMatrix;
import javacalculus.struct.CalcObject;
import javacalculus.struct.CalcSymbol;
import javacalculus.struct.CalcVector;

/**
 * Matrix Determinant evaluator. Attempts LUD decomposition algorithm to avoid O(n!) runtime
 * caused by using standard laplace identity algorithm. However, that failed in an epic
 * fashion for now. Maybe I don't understand LUD enough. So... I wrote the laplace implementation
 * as a placeholder. It has merits because it can evaluate determinants symbolically.
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 *  
 *
 */
public class CalcDET extends Calc1ParamFunctionEvaluator {
	
	private CalcMatrix LU;
	
	private int m, n, pivotSign;
	
	private CalcVector pivot;
	
	@Override
	protected CalcObject evaluateObject(CalcObject input) {
		if (input instanceof CalcMatrix) {
			CalcMatrix matrix = (CalcMatrix)input;
			
			//return LUDdecomposition(matrix);
			
			return LaplaceAlgorithm(matrix);
		}
		else {
			return null;
		}
	}
	
	/**
	 * Computes the determinant by expansion of minors (laplace algorithm).
	 * Works even if the entries are symbolic but runs at O(n!) efficiency.
	 */
	private CalcObject LaplaceAlgorithm(CalcMatrix matrix) {
		CalcFunction determinant = CALC.ADD.createFunction();
		
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		
		//recursion base case: 1 by 1 matrix
		if (width == 1 && height == 1) {
			return matrix.get(0, 0);
		}
		
		int mainVectorIndex = 0;
		//the vector that the algorithm will execute across/down
		CalcVector mainVector = matrix.get(mainVectorIndex);
		
		for (int ii = 0; ii < mainVector.size(); ii++) {
			//create "minor" matrix
			CalcVector[] newMatrix = new CalcVector[height - 1];
			int index = 0;
			for (int jj = 0; jj < height; jj++) {
				if (jj == mainVectorIndex) {
					continue;
				}
				newMatrix[index++] = new CalcVector(matrix.get(jj).getAll(), ii);
			}
			
			//this is the gist of evaluation recursively by minors. So inefficient that I want to puke.
			CalcFunction term = CALC.MULTIPLY.createFunction(mainVector.get(ii), LaplaceAlgorithm(new CalcMatrix(newMatrix)));
			
			//if odd index, must negate (checkerboard rule)
			if (ii % 2 != 0) {
				term.add(CALC.NEG_ONE);
			}
			
			determinant.add(term);
		}
		
		return determinant;
	}
	
	/**
	 * Computes the determinant of a matrix by LUD decomposition. Only works
	 * if all elements are numerical.
	 * @param matrix
	 * @return determinant
	 */
	private CalcFunction LUDdecomposition(CalcMatrix matrix) {
		CalcFunction determinant = CALC.MULTIPLY.createFunction();
		
		LU = (CalcMatrix)matrix.clone();
		
		m = matrix.getHeight();
		n = matrix.getWidth();
		
		if (m != n) {
			throw new CalcDimensionException("Determinants are only defined for square matrices");
		}
		
		pivot = new CalcVector(m);
		
		CalcVector LURow;
		CalcVector LUColumn = new CalcVector(m);
		
		for (int jj = 0; jj < n; jj++) {
			for (int ii = 0; ii < m; ii++) {
				LUColumn.set(ii, LU.get(ii, jj));
			}
			
	        //Apply transformations.

			for (int ii = 0; ii < m; ii++) {
				LURow = LU.get(ii);

				//Most of the time is spent in the following partial dot product.

				int kkmax = Math.min(ii, jj);
				
				CalcFunction partialDotProduct = CALC.ADD.createFunction();
				
				for (int kk = 0; kk < kkmax; kk++) {
					partialDotProduct.add(CALC.MULTIPLY.createFunction(LURow.get(kk),LUColumn.get(kk)));
				}

				LURow.set(jj, CALC.ADD.createFunction(LURow.get(jj), CALC.MULTIPLY.createFunction(partialDotProduct, CALC.NEG_ONE)));
				LUColumn.set(jj, CALC.ADD.createFunction(LURow.get(jj), CALC.MULTIPLY.createFunction(partialDotProduct, CALC.NEG_ONE)));
			}
			
			//Find pivot and exchange if necessary.

			int p = jj;
			for (int ii = jj + 1; ii < m; ii++) {
				if (LUColumn.get(ii).compareTo(LUColumn.get(p)) > 0) {
	            	p = ii;
	            }
			}
			if (p != jj) {
				for (int kk = 0; kk < n; kk++) {
					CalcObject temp = LU.get(p, kk); 
					LU.set(p, kk, LU.get(jj, kk));
					LU.set(jj, kk, temp);
				}
				CalcObject k = pivot.get(p); 
				pivot.set(p, pivot.get(jj)); 
				pivot.set(jj, k);
				pivotSign = -pivotSign;
			}

	         // Compute multipliers.
	         
			if (jj < m && LU.get(jj, jj).equals(CALC.ZERO)) {
	        	 for (int ii = jj +1; ii < m; ii++) {
	            	LU.set(ii, jj, CALC.MULTIPLY.createFunction(LU.get(ii, jj), CALC.POWER.createFunction(LU.get(jj, jj), CALC.NEG_ONE)));
	        	 }
			}
		}
		
		determinant = CALC.MULTIPLY.createFunction();
		determinant.add(new CalcDouble(pivotSign));
		
		for (int ii = 0; ii < n; ii++) {
			determinant.add(LU.get(ii, ii));
		}
		
		return determinant;
	}
	
	@Override
	protected CalcObject evaluateDouble(CalcDouble input) {
		return input; //determinants of constants = the constants? must verify
	}

	@Override
	protected CalcObject evaluateFraction(CalcFraction input) {
		return input;
	}

	@Override
	protected CalcObject evaluateFunction(CalcFunction input) {
		throw new CalcWrongParametersException("Determinants can only be evaluated given matrices");
	}

	@Override
	protected CalcObject evaluateInteger(CalcInteger input) {
		return input;
	}

	@Override
	protected CalcObject evaluateSymbol(CalcSymbol input) {
		//cannot evaluate symbols, so just return the original function
		return CALC.DET.createFunction(input);
	}

}
