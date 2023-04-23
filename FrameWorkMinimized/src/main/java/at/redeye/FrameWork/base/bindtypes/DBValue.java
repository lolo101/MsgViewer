package at.redeye.FrameWork.base.bindtypes;

public abstract class DBValue {

    protected final String name;
    protected final String title;

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

    public abstract void loadFromString(String s);

    public abstract boolean acceptString(String s);

    /**
     * @return the name of this value in lower case letters.
     */
    public String getName() {
        return name;
    }

    public String getTitle()
    {
        return title;
    }
}
