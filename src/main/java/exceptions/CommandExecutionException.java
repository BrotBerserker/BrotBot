package exceptions;

/**
 * @author Sigi
 *
 */
public class CommandExecutionException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 750034617571999256L;

	/**
	 *
	 */
	public CommandExecutionException() {
		super();
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public CommandExecutionException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public CommandExecutionException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 */
	public CommandExecutionException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public CommandExecutionException(Throwable arg0) {
		super(arg0);
	}

}
