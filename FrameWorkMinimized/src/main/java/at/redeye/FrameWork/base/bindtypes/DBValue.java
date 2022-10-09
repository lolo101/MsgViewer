package at.redeye.FrameWork.base.bindtypes;

import at.redeye.SqlDBInterface.SqlDBIO.impl.DBDataType;

public abstract class DBValue {

    protected String name;
    protected String title;

    protected DBValue(String name) {
        this(name, "", false);
    }

    protected DBValue(String name, String title) {
        this(name, title, false);
    }

    protected DBValue(String name, String title, boolean name_is_already_lowercase) {
        this.name = name_is_already_lowercase ? name : name.toLowerCase();
        this.title = title;
    }

    public abstract DBDataType getDBType();

    public abstract void loadFromDB(Object obj);

    public abstract void loadFromString(String s);

    public abstract boolean acceptString(String s);

    public abstract void loadFromCopy(Object obj);

    /**
     * @return the name of this value in lower case letters.
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toLowerCase();
    }

    public String getTitle()
    {
        return title;
    }

    public abstract Object getValue();

    public abstract DBValue getCopy();

    public void setAsPrimaryKey()
    {
    }
}
