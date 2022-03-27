package at.redeye.FrameWork.base.bindtypes;

import at.redeye.FrameWork.base.Root;
import at.redeye.SqlDBInterface.SqlDBIO.impl.DBDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DBEnum<T extends Enum<T>> extends DBValue {

    public static abstract class EnumHandler<T extends Enum<T>> {
        protected final Class<T> type;
        protected T value;

        protected EnumHandler(Class<T> type, T value) {
            this.type = type;
            this.value = value;
        }

        public abstract EnumHandler<T> getNewOne();

        public int getMaxSize() {
            return Arrays.stream(type.getEnumConstants())
                    .mapToInt(val -> val.toString().length())
                    .max()
                    .orElse(0);
        }

        public boolean setValue(String val) {
            try {
                value = Enum.valueOf(type, val);
                return true;
            } catch (IllegalArgumentException ex) {
                return false;
            }
        }

        public String getValue() {
            return value.toString();
        }

        public List<String> getPossibleValues() {
            List<String> res = new ArrayList<>();

            for (T t : type.getEnumConstants())
                res.add(t.toString());

            return res;
        }
    }

    public EnumHandler<T> handler;

    public DBEnum(String name, EnumHandler<T> enumval) {
        super(name);
        handler = enumval;
    }

    public DBEnum(String name, String title, EnumHandler<T> enumval) {
        super(name, title);
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
        DBEnum<?> copy = new DBEnum<>(name, handler.getNewOne());
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
            localized_values_map = new HashMap<>();
            localized_values = new ArrayList<>();

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
