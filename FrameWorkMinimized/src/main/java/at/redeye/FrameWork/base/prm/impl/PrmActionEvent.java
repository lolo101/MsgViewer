package at.redeye.FrameWork.base.prm.impl;

import at.redeye.FrameWork.base.bindtypes.DBString;

public class PrmActionEvent {

    private final DBString oldPrmValue = new DBString("oldPrmValue", 100);
    private final DBString newPrmValue = new DBString("newPrmValue", 100);
    private final DBString parameterName = new DBString("Parameter Name", 100);
    private String[] possibleVals = {};

    public boolean valueHasChanged() {
        return !newPrmValue.toString().equals(oldPrmValue.toString());
    }

    public String[] getPossibleVals() {
        return possibleVals;
    }

    public void setPossibleVals(String[] possibleVals) {
        this.possibleVals = possibleVals;
    }

    public DBString getNewPrmValue() {
        return newPrmValue;
    }

    public void setNewPrmValue(String newPrmValue) {
        this.newPrmValue.loadFromString(newPrmValue);
    }

    public DBString getOldPrmValue() {
        return oldPrmValue;
    }

    public void setOldPrmValue(String oldPrmValue) {
        this.oldPrmValue.loadFromString(oldPrmValue);
    }

    public DBString getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName.loadFromString(parameterName);
    }
}
