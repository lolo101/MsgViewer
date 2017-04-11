/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.SqlDBInterface.SqlDBIO.impl;

/**
 *
 * @author martin
 */
public class SqlDriverException extends Exception
{
    
	private static final long serialVersionUID = 1L;

	public SqlDriverException()
    {
        super();
    }

    public SqlDriverException(String message, Throwable cause)
    {
        super(message,cause);
    }

    public SqlDriverException(String message)
    {
        super(message);
    }

    public SqlDriverException(Throwable cause)
    {
        super(cause);
    }
}
