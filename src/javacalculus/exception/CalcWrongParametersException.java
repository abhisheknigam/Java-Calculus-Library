package javacalculus.exception;

/**
 * Thrown when extra parameters are given to a function that does not support
 * that many parameters (e.g. SIN(1,2))
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 *  
 *
 */
public class CalcWrongParametersException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2485098497101022477L;

	public CalcWrongParametersException(String message) {
		super(message);
	}
}
