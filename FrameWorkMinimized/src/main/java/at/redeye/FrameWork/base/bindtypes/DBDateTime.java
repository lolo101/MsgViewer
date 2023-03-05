package at.redeye.FrameWork.base.bindtypes;

import at.redeye.SqlDBInterface.SqlDBIO.StmtExecInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DBDateTime extends DBValue {

    private static final SimpleDateFormat SDF_4_STD_STRING = new SimpleDateFormat(StmtExecInterface.SQLIF_STD_DATE_FORMAT + " "
            + StmtExecInterface.SQLIF_STD_TIME_FORMAT);

    protected Date value = new Date(0);

    public DBDateTime(String name, String title, boolean name_is_already_lowercase) {
        super(name, title, name_is_already_lowercase);
    }

    @Override
    public boolean acceptString(String s) {
        return false;
    }

    @Override
    public Date getValue() {
        return value;
    }

    @Override
    public void loadFromCopy(Object obj) {
        value = (Date) ((Date) obj).clone();
    }

    @Override
    public void loadFromString(String s) {
        try {
            value = SDF_4_STD_STRING.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return getStdString(value);
    }

    public static String getStdString(Date date) {
        return SDF_4_STD_STRING.format(date);
    }
}
