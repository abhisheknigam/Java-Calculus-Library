package javacalculus.exception;

/**
 * Thrown when an unidentified function needs to be parsed or evaluated.
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 * @see javacalculus.core.CALC 
 *
 */
public class CalcUnsupportedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4339047940003069345L;

	public CalcUnsupportedException(String message) {
		super(message);
	}
}
