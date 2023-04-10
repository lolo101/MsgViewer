package at.redeye.FrameWork.base.prm.bindtypes;

import at.redeye.FrameWork.base.bindtypes.DBHistory;
import at.redeye.FrameWork.base.bindtypes.DBString;
import at.redeye.FrameWork.base.bindtypes.DBStrukt;
import at.redeye.FrameWork.base.prm.PrmAttachInterface;
import at.redeye.FrameWork.base.prm.PrmDefaultChecksInterface;
import at.redeye.FrameWork.base.prm.PrmListener;
import at.redeye.FrameWork.base.prm.impl.PrmActionEvent;

import java.util.Collection;
import java.util.Vector;

public class DBConfig extends DBStrukt implements PrmAttachInterface {
    private static final String TABLENAME = "CONFIG";

    private final Collection<PrmListener> prmListeners = new Vector<>();
    private String[] possibleValues = {};

    private PrmDefaultChecksInterface defaultChecks;

    public final DBString name = new DBString("name", "Name", 100);
    public final DBString value = new DBString("value", "Wert", 100);
    public final DBString descr = new DBString("description", "Beschreibung", 250);
    public final DBHistory hist = new DBHistory("hist");

    public DBConfig() {
        super(TABLENAME);

        register();
    }

    public DBConfig(String name, String value, String descr) {
        super(TABLENAME);

        register();

        this.name.loadFromString(name);
        this.value.loadFromString(value);
        this.descr.loadFromString(descr);
    }

    public DBConfig( String name, String value )
    {
        super(TABLENAME);

        register();

        this.name.loadFromString(name);
        this.value.loadFromString(value);
    }

    public DBConfig( String name, String value, String descr, PrmDefaultChecksInterface checks )
    {
        super(TABLENAME);

        register();

        this.name.loadFromString(name);
        this.value.loadFromString(value);
        this.descr.loadFromString(descr);
        this.defaultChecks = checks;
    }

    public DBConfig( String name, String value, String descr, PrmDefaultChecksInterface checks, String [] possibleValues )
    {
        super(TABLENAME);

        register();

        this.name.loadFromString(name);
        this.value.loadFromString(value);
        this.descr.loadFromString(descr);
        this.defaultChecks = checks;
        this.possibleValues = possibleValues;
    }

    private void register()
    {
        add( name );
        add( value );
        add( descr );
        add( hist );
    }

    public String getConfigName()
    {
        return name.getValue();
    }

    public String getConfigValue()
    {
        return value.getValue();
    }

    public void setConfigValue( String val )
    {
        value.loadFromString(val);
    }

    @Override
    public void addPrmListener(PrmListener listener) {
        prmListeners.add(listener);
    }

    @Override
    public void removePrmListener(PrmListener listener) {
        prmListeners.remove(listener);
    }

    @Override
    public void updateListeners(PrmActionEvent prmActionEvent) {

        System.out.println("PRM " + name +
                " has " + prmListeners.size() + " listener(s) registered!");

        boolean valueHasChanged = prmActionEvent.valueHasChanged();

        for (PrmListener currentListener : prmListeners) {
            if (defaultChecks == null) {
                System.out.println("-> no default check");
            } else if (valueHasChanged) {
                currentListener.onChange(defaultChecks, prmActionEvent);
            }
        }
    }

    public String[] getPossibleValues() {
        return possibleValues;
    }
}
