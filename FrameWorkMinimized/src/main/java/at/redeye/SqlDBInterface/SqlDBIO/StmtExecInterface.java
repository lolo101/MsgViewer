package at.redeye.SqlDBInterface.SqlDBIO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import at.redeye.SqlDBInterface.SqlDBIO.impl.DBDataType;
import at.redeye.SqlDBInterface.SqlDBIO.impl.TableBindingNotRegisteredException;
import at.redeye.SqlDBInterface.SqlDBIO.impl.UnsupportedDBDataTypeException;

/**
 *
 * @author Mario Mattl
 *
 */
public interface StmtExecInterface {

	String SQLIF_STD_DATE_FORMAT = "yyyy-MM-dd";
	String SQLIF_STD_TIME_FORMAT = "HH:mm:ss";
	String SQLIF_STD_SHORTTIME_FORMAT = "HH:mm";

	/**
	 *
	 * @param tablenames
	 * @param whereStmt
	 * @return The selected rows
	 */
    List<HashMap<String, Object>> fetchTableValue(String[] tablenames,
                                                  String whereStmt);

	/**
	 *
	 * @param tablename
	 * @param primaryKeyData
	 * @return
	 */
    HashMap<String, Object> fetchTableValue(String tablename,
                                            HashMap<String, Object> primaryKeyData);

	/**
	 *
	 * @param stmt
	 * @param typelist
	 * @return The selected rows
	 */
    List<List<?>> fetchColumnValue(String stmt, List<DBDataType> typelist)
	;

	/**
	 *
	 * Method is similar to a "toString()" overload.
	 *
	 * @param rs
	 *            Database result set:<br>
	 *            A Vector (rows) of HashMaps (columns)
	 * @return A String for displaying all rows
	 */
    String printFetchTableOutput(List<HashMap<String, Object>> rs);

	/**
	 *
	 * @param rs
	 *            The selected row.
	 * @return The printable output (similar to a toString overload)
	 */
    String printFetchTableOutput(HashMap<String, Object> rs);

	/**
	 *
	 * @param stmt
	 *            The insert Statement. The user is fully responsible for giving
	 *            a valid statement.
	 * @return Number of inserted elements
	 */
    int updateValues(String stmt);

	/**
	 *
	 * @param stmt
	 *            The update Statement. The user is fully responsible for giving
	 *            a valid statement.
	 * @return Number of inserted elements
	 */
    int insertValues(String stmt);

	/**
	 *
	 * @param tablename
	 *            The table's name
	 * @param values
	 *            Columns and associated values
	 * @param whereStmt
	 *            Optional, but if not specified all PrimaryKey elements must
	 *            exist <br>
	 *            in the given map "values".
	 * @return Number of updated elements
	 */
    int updateTableValues(String tablename,
                          HashMap<String, Object> values, String whereStmt)
	;

	/**
	 *
	 * @param tablename
	 *            The table's name
	 * @param values
	 *            Columns and associated values
	 * @return Number of inserted elements
	 */
    int insertTableValues(String tablename,
                          HashMap<String, Object> values);

	/**
	 *
	 * @return The previously executed statement.
	 */
    String getLastStmt();

	StmtCreatorInterface getStmtCreator();

}
