package at.redeye.FrameWork.base.prm.bindtypes;

import at.redeye.FrameWork.base.bindtypes.*;
import at.redeye.FrameWork.base.prm.PrmAttachInterface;
import at.redeye.FrameWork.base.prm.PrmCustomChecksInterface;
import at.redeye.FrameWork.base.prm.PrmDefaultChecksInterface;
import at.redeye.FrameWork.base.prm.PrmListener;
import at.redeye.FrameWork.base.prm.impl.PrmActionEvent;
import java.util.Vector;


/**
 *
 * @author martin
 */
public class DBConfig extends DBStrukt implements PrmAttachInterface
{
    public static String TABLENAME = "CONFIG";

    private Vector <PrmListener> prmListeners = new Vector<>();
    private String oldValue = "";
    private String [] possibleValues = {};

    private PrmCustomChecksInterface customChecks = null;
    private PrmDefaultChecksInterface defaultChecks = null;


    public DBString  name  = new DBString("name", "Name", 100 );
    public DBString  value = new DBString( "value", "Wert", 100 );
    public DBString  descr  = new DBString( "description", "Beschreibung", 250 );
    public DBHistory hist  = new DBHistory("hist");

    protected boolean changed = false;

    public DBConfig()
    {
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


    public boolean differs(DBConfig c_db) {
        if( getConfigName().equals(c_db.getConfigName() ) == false )
            return true;
        if( getConfigValue().equals(c_db.getConfigValue()) == false )
            return true;

        return false;
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
        oldValue = value.toString();
        value.loadFromString(val);
    }

    @Override
    public DBStrukt getNewOne() {
        return new DBConfig();
    }

    public void addPrmListener(PrmListener listener) {
        prmListeners.add(listener);
    }

    public void removePrmListener(PrmListener listener) {
        prmListeners.remove(listener);
    }

    public void addAllPrmListeners( DBConfig  other )
    {
        prmListeners.addAll(other.prmListeners);
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

    public String getOldValue() {
        return oldValue;
    }

    public String[] getPossibleValues() {
        return possibleValues;
    }

    public void setPossibleValues(String[] possibleValues) {
        this.possibleValues = possibleValues;
    }

    public void setCustomChecks(PrmCustomChecksInterface customChecks) {
        this.customChecks = customChecks;
    }

    public void setDefaultChecks(PrmDefaultChecksInterface defaultChecks) {
        this.defaultChecks = defaultChecks;
    }

    public PrmCustomChecksInterface getCustomChecks() {
        return customChecks;
    }

    public PrmDefaultChecksInterface getDefaultChecks() {
        return defaultChecks;
    }


    public boolean hasChanged()
    {
        return changed;
    }

    public void setChanged()
    {
        changed = true;
    }

    public void setChanged( boolean state )
    {
        changed = state;
    }

}
