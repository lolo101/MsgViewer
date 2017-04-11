/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.base.bindtypes;

import at.redeye.SqlDBInterface.SqlDBIO.impl.DBDataType;

/**
 *
 * @author martin
 */
public class DBInteger extends DBValue {

    Integer value = new Integer(0);
    
    public DBInteger( String name )
    {
        super( name );
    }
    
    public DBInteger( String name, String title )
    {
        super( name, title );
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
        return value.toString();
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
        i.value = new Integer( value );
        return i;
    }

    @Override
    public void loadFromCopy(Object obj) {
        value = new Integer( (Integer) obj );
    }

}
