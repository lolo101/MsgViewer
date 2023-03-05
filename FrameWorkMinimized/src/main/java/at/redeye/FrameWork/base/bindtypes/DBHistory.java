package at.redeye.FrameWork.base.bindtypes;

import java.util.Date;

public class DBHistory extends DBStrukt {
    public DBDateTime an_zeit = new DBDateTime("anzeit", "Anlegezeit", true);
    public DBDateTime lo_zeit = new DBDateTime("lozeit", "Löschzeit", true);
    public DBDateTime ae_zeit = new DBDateTime("aezeit", "Änderungszeit", true);
    public DBString an_user = new DBString("anuser", "Anlegebenutzer", 30, true);
    public DBString lo_user = new DBString("louser", "Löschbenutzer", 30, true);
    public DBString ae_user = new DBString("aeuser", "Änderungsbenutzer", 30, true);

    public DBHistory(String name) {
        super(name, "Geschichte");
        create();
    }

    private void create() {
        add(an_zeit);
        add(an_user);
        add(ae_zeit);
        add(ae_user);
        add(lo_zeit);
        add(lo_user);
    }

    public void setAeHist(String user) {
        ae_zeit.loadFromCopy(new Date(System.currentTimeMillis()));
        ae_user.loadFromCopy(user);
    }
}
