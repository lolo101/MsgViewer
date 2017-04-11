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
public class DBDouble extends DBValue {

    Double value = new Double(0);
    
    public DBDouble( String name )
    {
        super( name );
    }
    
    public DBDouble( String name, String title )
    {
        super( name, title );
    }
    
    @Override
    public DBDataType getDBType() {
        return DBDataType.DB_TYPE_DOUBLE;
    }

    @Override
    public void loadFromDB(Object obj) {
        value = (Double)obj;
    }

    @Override
    public Double getValue() {
        return value;
    }
    
    @Override
    public String toString()
    {
        return value.toString();
    }

    @Override
    public void loadFromString(String s) {
        if( s.isEmpty() )
            value = 0.0;
        else
            value = Double.parseDouble(s);
    }

    @Override
    public boolean acceptString(String s) {
        
        try {
            Double.parseDouble(s);
        } catch ( NumberFormatException ex ) {
            return false;
        }
        
        return true;
    }

    @Override
    public DBDouble getCopy() {
        DBDouble i = new DBDouble(name);
        i.value = new Double( value );
        return i;
    }

    @Override
    public void loadFromCopy(Object obj) {
        value = new Double( (Double) obj );
    }

}
