/**
 * 
 */
package javacalculus.exception;

/**
 * This will be thrown if an expression string cannot be parsed into
 * a HObject recursive hierarchy due to syntactic errors.
 * @author Duyun Chen
 *
 */
public class CalcSyntaxException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * @param message
	 * The error message
	 */
	public CalcSyntaxException(String message) {
		super(message);
	}

}
