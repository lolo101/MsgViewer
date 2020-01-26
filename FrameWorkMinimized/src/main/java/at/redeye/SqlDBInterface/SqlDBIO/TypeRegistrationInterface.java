package at.redeye.SqlDBInterface.SqlDBIO;

import java.io.IOException;
import java.util.HashMap;

import at.redeye.SqlDBInterface.SqlDBIO.impl.ColumnAttribute;
import at.redeye.SqlDBInterface.SqlDBIO.impl.DBDataType;
import at.redeye.SqlDBInterface.SqlDBIO.impl.UnsupportedDBDataTypeException;
import at.redeye.SqlDBInterface.SqlDBIO.impl.WrongBindFileFormatException;

public interface TypeRegistrationInterface {

	/**
	 *
	 * @param filename
	 */
    void registerTableBindings(String filename);

	/**
	 *
	 * @param data
	 *            Usage: <br>
	 *            HashMap (String tablename, <br>
	 *            &nbsp;&nbsp;&nbsp;&nbsp;HashMap(String column_name,
	 *            ColumnAttribute column_attributes) ) <br>
	 */
    void registerTableBindings(
            HashMap<String, HashMap<String, ColumnAttribute>> data)
	;

	/**
	 *
	 * @param ident
	 *            The type identifier.
	 * @return The associated database type (DBDataType)
	 */
    DBDataType getRealDBType(String ident)
	;

	/**
	 *
	 * @return The whole registration of table bindings
	 */
    HashMap<String, HashMap<String, ColumnAttribute>> getAllRegisteredTables();

	/**
	 *
	 * @param tablename
	 * @return The table bindings for the given table
	 */
    HashMap<String, ColumnAttribute> getRegisteredTableByString(
            String tablename);
}
