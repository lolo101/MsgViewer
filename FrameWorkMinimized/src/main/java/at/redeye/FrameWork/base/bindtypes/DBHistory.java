package at.redeye.FrameWork.base.bindtypes;

import java.util.Date;

public class DBHistory extends DBStrukt {
    public DBDateTime  an_zeit = new DBDateTime( "anzeit", "Anlegezeit", true );
    public DBDateTime  lo_zeit = new DBDateTime( "lozeit", "L\u00f6schzeit", true );
    public DBDateTime  ae_zeit = new DBDateTime( "aezeit", "\u00c4nderungszeit", true  );
    public DBString  an_user = new DBString( "anuser", "Anlegebenutzer", 30, true );
    public DBString  lo_user = new DBString( "louser", "L\u00f6schbenutzer", 30, true );
    public DBString  ae_user = new DBString( "aeuser", "\u00c4nderungsbenutzer", 30, true );

    public DBHistory( String name )
    {
        super( name, "Geschichte" );
        create();
    }

    private void create()
    {
        add( an_zeit );
        add(an_user);
        add(ae_zeit);
        add(ae_user);
        add(lo_zeit);
        add(lo_user);
    }

    @Override
    public DBHistory getNewOne() {
        return new DBHistory(strukt_name);
    }

    public void setAeHist(String user) {
        ae_zeit.loadFromCopy(new Date(System.currentTimeMillis()));
        ae_user.loadFromCopy(user);
    }
}
