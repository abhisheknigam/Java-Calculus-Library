package javacalculus.exception;

/**
 * Thrown when an illegal dimensional calculation is performed (e.g. adding matrices
 * of different sizes)
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 *  
 *
 */
public class CalcDimensionException extends RuntimeException {

	private static final long serialVersionUID = 4298179502229064227L;

	public CalcDimensionException(String message) {
		super(message);
	}
}
