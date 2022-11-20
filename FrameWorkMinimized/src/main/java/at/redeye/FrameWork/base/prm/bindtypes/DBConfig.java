package at.redeye.FrameWork.base.prm.bindtypes;

import at.redeye.FrameWork.base.bindtypes.DBHistory;
import at.redeye.FrameWork.base.bindtypes.DBString;
import at.redeye.FrameWork.base.bindtypes.DBStrukt;
import at.redeye.FrameWork.base.prm.PrmAttachInterface;
import at.redeye.FrameWork.base.prm.PrmCustomChecksInterface;
import at.redeye.FrameWork.base.prm.PrmDefaultChecksInterface;
import at.redeye.FrameWork.base.prm.PrmListener;
import at.redeye.FrameWork.base.prm.impl.PrmActionEvent;

import java.util.Vector;

public class DBConfig extends DBStrukt implements PrmAttachInterface {
    private static final String TABLENAME = "CONFIG";

    private final Vector<PrmListener> prmListeners = new Vector<>();
    private String[] possibleValues = {};

    private PrmCustomChecksInterface customChecks;
    private PrmDefaultChecksInterface defaultChecks;

    public DBString name = new DBString("name", "Name", 100);
    public DBString value = new DBString("value", "Wert", 100);
    public DBString descr = new DBString("description", "Beschreibung", 250);
    public DBHistory hist = new DBHistory("hist");

    public DBConfig() {
        super(TABLENAME);

        register();
    }

    public DBConfig( String name, String value, String descr )
    {
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

    public DBConfig( String name, String value, PrmDefaultChecksInterface checks, String [] possibleValues )
    {
        super(TABLENAME);

        register();

        this.name.loadFromString(name);
        this.value.loadFromString(value);
        this.defaultChecks = checks;
        this.possibleValues = possibleValues;
    }

    public DBConfig( String name, String value, PrmDefaultChecksInterface checks, String [] possibleValues, PrmCustomChecksInterface specialChecks )
    {
        super(TABLENAME);

        register();

        this.name.loadFromString(name);
        this.value.loadFromString(value);
        this.defaultChecks = checks;
        this.possibleValues = possibleValues;
        this.customChecks = specialChecks;
    }

    private void register()
    {
        add( name );
        add( value );
        add( descr );
        add( hist );

        name.setAsPrimaryKey();
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
    public DBStrukt getNewOne() {
        return new DBConfig();
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

        System.out.println ("PRM " + name.toString()+
                " has " + prmListeners.size() + " listener(s) registered!");

        for (PrmListener currentListener : prmListeners) {

            if (defaultChecks != null) {
                // Only send, if value has changed
                if (!prmActionEvent.getNewPrmValue().toString().equals(prmActionEvent.getOldPrmValue().toString()))
                    currentListener.onChange(defaultChecks, prmActionEvent);
            } else {
                System.out.println("-> no default check");
            }

            if (customChecks != null) {
                // Only send, if value has changed
                if (!prmActionEvent.getNewPrmValue().toString().equals(prmActionEvent.getOldPrmValue().toString()))
                    currentListener.onChange(customChecks, prmActionEvent);
            } else {
                System.out.println("-> no custom check");
            }
        }
    }

    public String[] getPossibleValues() {
        return possibleValues;
    }
}
