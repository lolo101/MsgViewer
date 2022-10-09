package at.redeye.FrameWork.base.bindtypes;

import at.redeye.SqlDBInterface.SqlDBIO.impl.DBDataType;

public class DBInteger extends DBValue {

    private int value;

    public DBInteger( String name )
    {
        super( name );
    }

    @Override
    public DBDataType getDBType() {
        return DBDataType.DB_TYPE_INTEGER;
    }

    @Override
    public void loadFromDB(Object obj) {
        value = (Integer)obj;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String toString()
    {
        return String.valueOf(value);
    }

    @Override
    public void loadFromString(String s) {
        value = Integer.parseInt(s);
    }

    @Override
    public boolean acceptString(String s) {

        try {
            Integer.parseInt(s);
        } catch ( NumberFormatException ex ) {
            return false;
        }

        return true;
    }

    @Override
    public DBInteger getCopy() {
        DBInteger i = new DBInteger(name);
        i.value = value;
        return i;
    }

    @Override
    public void loadFromCopy(Object obj) {
        value = (Integer) obj;
    }
}
