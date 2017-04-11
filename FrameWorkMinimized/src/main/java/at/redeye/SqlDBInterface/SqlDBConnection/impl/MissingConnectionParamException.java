/**
 * 
 */
package at.redeye.SqlDBInterface.SqlDBConnection.impl;

/**
 * @author Mario Mattl
 *
 */
public class MissingConnectionParamException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8653338172502035844L;

	/**
	 * 
	 */
	public MissingConnectionParamException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public MissingConnectionParamException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public MissingConnectionParamException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public MissingConnectionParamException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
