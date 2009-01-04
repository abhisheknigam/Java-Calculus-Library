package javacalculus.exception;

/**
 * Thrown when a mathematical indeterminate form is encountered (0/0, 0^0, etc.)
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 *  
 *
 */
public class CalcIndeterminateException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5544536779496728774L;

	public CalcIndeterminateException(String message) {
		super(message);
	}
}
