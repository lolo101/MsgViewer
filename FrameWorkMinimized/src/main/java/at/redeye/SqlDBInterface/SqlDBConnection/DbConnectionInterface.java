package at.redeye.SqlDBInterface.SqlDBConnection;

import java.sql.Connection;
import java.sql.SQLException;

import at.redeye.SqlDBInterface.SqlDBConnection.impl.MissingConnectionParamException;
import at.redeye.SqlDBInterface.SqlDBConnection.impl.UnSupportedDatabaseException;


/**
 * @author Mario Mattl
 *
 */
public interface DbConnectionInterface {

	public Connection connectToDatabase() throws ClassNotFoundException,
			UnSupportedDatabaseException, SQLException, MissingConnectionParamException;

	public void disconnectDatabase(Connection conn) throws SQLException;

}
