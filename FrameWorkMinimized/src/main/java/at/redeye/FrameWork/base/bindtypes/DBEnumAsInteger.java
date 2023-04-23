package at.redeye.FrameWork.base.bindtypes;

import at.redeye.FrameWork.base.Root;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBEnumAsInteger extends DBValue {

    public static abstract class EnumAsIntegerHandler
    {
        public abstract boolean setValue(String val);

        public abstract void setValue(Integer val);

        public abstract Integer getValue();
        public abstract String getValueAsString();

        public abstract Iterable<String> getPossibleValues();
    }

    public EnumAsIntegerHandler handler;

    public DBEnumAsInteger( String name, String title, EnumAsIntegerHandler enumval )
    {
        super( name, title );
        handler = enumval;
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
    public String toString()
    {
        return getLocalizedString();
    }

    public List<String> getPossibleValues()
    {
        return getLocalizedPossibleValues();
    }

    /**
     * Required for translating back
     * a localized input method back into
     * the original language
     */
    private HashMap<String,String> localized_values_map;
    private List<String> localized_values;

    private List<String> getLocalizedPossibleValues(Root root) {
        if (localized_values_map == null) {
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

    private List<String> getLocalizedPossibleValues() {
        return getLocalizedPossibleValues(Root.getLastRoot());
    }

    private String delocalize(String message) {
        if (localized_values == null)
            getLocalizedPossibleValues();

        String res = localized_values_map.get(message);

        if (res != null)
            return res;

        return message;
    }

    private String getLocalizedString(Root root) {
        if (localized_values == null)
            getLocalizedPossibleValues();

        return root.MlM(handler.getValueAsString());
    }

    private String getLocalizedString() {
        Root root = Root.getLastRoot();

        if (root != null)
            return getLocalizedString(root);

        return handler.getValueAsString();
    }
}
