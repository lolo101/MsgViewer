package at.redeye.FrameWork.base.bindtypes;

public class DBString extends DBValue {
    protected String value = "";
    private final int max_len;

    public DBString( String name, int max_len )
    {
        super( name );
        this.max_len = max_len;
    }

    public DBString( String name, String title, int max_len )
    {
        super( name, title );
        this.max_len = max_len;
    }

    public DBString( String name, String title, int max_len, boolean is_already_lowercase )
    {
        super( name, title, is_already_lowercase );
        this.max_len = max_len;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String toString()
    {
        return value;
    }

    @Override
    public void loadFromString(String s) {
        value = s;
    }

    @Override
    public boolean acceptString(String s) {
        return s.length() <= max_len;
    }

    @Override
    public void loadFromCopy(Object obj) {
        value = (String)obj;
    }

    public int getMaxLen()
    {
        return max_len;
    }

    public boolean isEmpty()
    {
        return value.isEmpty();
    }

    public boolean isEmptyTrimmed() {
        return value.trim().isEmpty();
    }
}
