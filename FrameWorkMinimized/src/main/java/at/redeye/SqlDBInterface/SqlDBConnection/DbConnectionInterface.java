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

	Connection connectToDatabase();

	void disconnectDatabase(Connection conn);

}
