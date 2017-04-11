package at.redeye.FrameWork.base.bindtypes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;

import at.redeye.SqlDBInterface.SqlDBIO.StmtExecInterface;
import at.redeye.SqlDBInterface.SqlDBIO.impl.DBDataType;
import org.joda.time.LocalDate;

/**
 * 
 * @author Mario Mattl
 * 
 */
public class DBDateTime extends DBValue {

        private static final SimpleDateFormat SDF_4_STD_STRING = new SimpleDateFormat( StmtExecInterface.SQLIF_STD_DATE_FORMAT + " "
						+ StmtExecInterface.SQLIF_STD_TIME_FORMAT );

        private static final SimpleDateFormat SDF_4_SQLIF_STD_TIME_FORMAT = new SimpleDateFormat( StmtExecInterface.SQLIF_STD_TIME_FORMAT);

        private static final SimpleDateFormat SDF_4_SQLIF_STD_DATE_FORMAT = new SimpleDateFormat( StmtExecInterface.SQLIF_STD_DATE_FORMAT );

	protected Date value = new Date(0);

	public DBDateTime(String name) {
		super(name);	
	}

	public DBDateTime(String name, String title) {
		super(name, title);		
	}

	public DBDateTime(String name, String title, boolean name_is_already_lowercase) {
		super(name, title, name_is_already_lowercase);		
        }        
        
	@Override
	public boolean acceptString(String s) {
		return false;
	}

	@Override
	public DBDateTime getCopy() {
		DBDateTime datetime = new DBDateTime(name);
		datetime.value = value;

		return datetime;
	}

	@Override
	public DBDataType getDBType() {
		// TODO Auto-generated method stub
		return DBDataType.DB_TYPE_DATETIME;
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
	public void loadFromDB(Object obj) {

		value = (Date) obj;

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

	public static String getStdString(DateTime date) {
		return date.toString(StmtExecInterface.SQLIF_STD_DATE_FORMAT + " "
				+ StmtExecInterface.SQLIF_STD_TIME_FORMAT);
	}

	public static String getStdString(DateMidnight date) {
		return date.toString(StmtExecInterface.SQLIF_STD_DATE_FORMAT + " "
				+ StmtExecInterface.SQLIF_STD_TIME_FORMAT);
	}

	public static String getStdString(LocalDate date) 
        {
            DateTime dt = date.toDateTimeAtStartOfDay();
            return dt.toString(StmtExecInterface.SQLIF_STD_DATE_FORMAT + " "
				+ StmtExecInterface.SQLIF_STD_TIME_FORMAT);
	}


    public String getTimeStr() {
        return getTimeStr(value);
    }

    

	public static String getTimeStr(Date date) {

            return SDF_4_SQLIF_STD_TIME_FORMAT.format(date);

	}

	public String getDateStr() {
		return getDateStr(value);
	}        

	public static String getDateStr(Date value) {

		return SDF_4_SQLIF_STD_DATE_FORMAT.format(value);
	}

	public static String getDateStr(DateMidnight date) {

		return date.toString(StmtExecInterface.SQLIF_STD_DATE_FORMAT);

	}

	public static String getDateStr(LocalDate date) {
		return date.toString(StmtExecInterface.SQLIF_STD_DATE_FORMAT);
	}
        
	public boolean loadTimePart(String time) {
		if (time.matches("(([0-1][0-9])|(2[0-3])):[0-5][0-9]:[0-5][0-9]") == false)
			return false;

		String time_str = toString();
		String completet = time_str.substring(0, 10) + " " + time;

		loadFromString(completet);

        /*
		System.out.println("completed: '" + completet + "' toString '"
				+ toString() + "'");
        */
        
		if (completet.equalsIgnoreCase(toString()) == true) {
			//System.out.println("true");
			return true;
		}

		//System.out.println("false");

		return false;
	}

    public long getTime()
    {
        DateTime datetime = new DateTime(value);
        return datetime.getMillisOfDay();
    }
}
