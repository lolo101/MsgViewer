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
	 * @throws IOException
	 * @throws UnsupportedDBDataTypeException
	 * @throws WrongBindFileFormatException
	 */
	public void registerTableBindings(String filename) throws IOException,
			UnsupportedDBDataTypeException, WrongBindFileFormatException;

	/**
	 * 
	 * @param data
	 *            Usage: <br>
	 *            HashMap (String tablename, <br>
	 *            &nbsp;&nbsp;&nbsp;&nbsp;HashMap(String column_name,
	 *            ColumnAttribute column_attributes) ) <br>
	 * @throws UnsupportedDBDataTypeException
	 * @throws WrongBindFileFormatException
	 */
	public void registerTableBindings(
			HashMap<String, HashMap<String, ColumnAttribute>> data)
			throws UnsupportedDBDataTypeException, WrongBindFileFormatException;

	/**
	 * 
	 * @param ident
	 *            The type identifier.
	 * @return The associated database type (DBDataType)
	 * @throws UnsupportedDBDataTypeException
	 */
	public DBDataType getRealDBType(String ident)
			throws UnsupportedDBDataTypeException;

	/**
	 * 
	 * @return The whole registration of table bindings
	 */
	public HashMap<String, HashMap<String, ColumnAttribute>> getAllRegisteredTables();

	/**
	 * 
	 * @param tablename
	 * @return The table bindings for the given table
	 */
	public HashMap<String, ColumnAttribute> getRegisteredTableByString(
			String tablename);
}
