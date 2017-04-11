/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.base.bindtypes;

import at.redeye.FrameWork.base.Root;

import at.redeye.SqlDBInterface.SqlDBIO.impl.DBDataType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author martin
 */
public class DBEnum extends DBValue {

    public static abstract class EnumHandler
    {
        public abstract int getMaxSize();
        public abstract boolean setValue( String val );
        public abstract String getValue();
        public abstract EnumHandler getNewOne();
        public abstract List<String> getPossibleValues();
    }
    
    public EnumHandler handler; 
    
    public DBEnum( String name, EnumHandler enumval )
    {
        super( name );
        handler = enumval;
    }
    
    public DBEnum( String name, String title, EnumHandler enumval )
    {
        super( name, title );
        handler = enumval;
    }
    
    @Override
    public DBDataType getDBType() {
        return DBDataType.DB_TYPE_STRING;
    }

    @Override
    public void loadFromDB(Object obj) {
        handler.setValue((String)obj);
    }

    @Override
    public void loadFromString(String s) {
        handler.setValue(delocalize(s));
    }

    @Override
    public boolean acceptString(String s) {
       return handler.setValue(delocalize(s));
    }

    @Override
    public void loadFromCopy(Object obj) {
       handler = handler.getNewOne();
       handler.setValue(delocalize((String)obj));
    }

    @Override
    public String getValue() {
        return handler.getValue();
    }

    @Override
    public DBValue getCopy() {
        DBEnum copy = new DBEnum(name, handler.getNewOne() );
        copy.handler.setValue(handler.getValue());
        return copy;
    }

    public int getMaxLen()
    {
        return handler.getMaxSize();
    }
    
    @Override
    public String toString()
    {
        return handler.getValue();
    }
    
    public List<String> getPossibleValues()
    {
        return handler.getPossibleValues();
    }

    /**
     * Required for translating back
     * a localized input method back into
     * the original language
     */
    private HashMap<String,String> localized_values_map;
    private List<String> localized_values;

    public List<String> getLocalizedPossibleValues(Root root)
    {
        if( localized_values_map == null )
        {
            initLocalization( root );
            localized_values_map = new HashMap<String,String>();
            localized_values = new ArrayList<String>();
            
            for (String original : handler.getPossibleValues()) {
                String translated = root.MlM(original);

                localized_values.add(translated);
                localized_values_map.put(translated, original);
            }
        }

        return localized_values;
    }
    
    public List<String> getLocalizedPossibleValues()
    {
        return getLocalizedPossibleValues(Root.getLastRoot());
    }

    public String delocalize( String message )
    {
        if( localized_values == null )
            getLocalizedPossibleValues();

        String res = localized_values_map.get(message);

        if( res != null )
            return res;

        return message;
    }

    /**
     * call here root.loadMlM4Class(root, language);
     * @param root
     */
    public void initLocalization(Root root)
    {
    
    }

    public String getLocalizedString(Root root)
    {
        if( localized_values == null )
            getLocalizedPossibleValues();

       return root.MlM(handler.getValue());
    }

    public String getLocalizedString()
    {
       Root root = Root.getLastRoot();

       if( root != null )
           return getLocalizedString( root );

       return handler.getValue();
    }
}
