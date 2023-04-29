package at.redeye.FrameWork.base.tablemanipulator;

import at.redeye.FrameWork.base.bindtypes.DBValue;

public class Coll {

    public final String title;
    public boolean isEditable;
    DBValue dbval;
    private boolean doAutocompleteForAllOfThisColl = true;

    public Coll(String title, Boolean isEditable, DBValue val) {
        this.title = title;
        this.isEditable = isEditable;
        this.dbval = val;
    }

    void setEditable() {
        this.isEditable = true;
    }

    void setDoAutocompleteForAllOfThisColl(boolean state) {
        doAutocompleteForAllOfThisColl = state;
    }

    boolean getDoAutocompleteForAllOfThisColl() {
        return doAutocompleteForAllOfThisColl;
    }

}
