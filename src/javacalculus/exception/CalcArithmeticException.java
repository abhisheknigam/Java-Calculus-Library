package javacalculus.exception;

/**
 * Thrown when an illegal math operation is required (e.g. 1/0 or arccos(2))
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 *  
 *
 */
public class CalcArithmeticException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2485098497101022477L;

	public CalcArithmeticException(String message) {
		super(message);
	}
}
