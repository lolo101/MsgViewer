/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.base.tablemanipulator.validators;

import java.text.SimpleDateFormat;
import java.util.Date;

import at.redeye.FrameWork.base.bindtypes.DBDateTime;
import at.redeye.FrameWork.base.bindtypes.DBValue;
import at.redeye.FrameWork.base.tablemanipulator.TableValidator;

/**
 *
 * @author martin
 */
public class TimeHourMinuteValidator extends TableValidator {

    @Override
    public String formatData(Object data) {
        // System.out.println("HERE");
        DBDateTime val = (DBDateTime) data;

        SimpleDateFormat formater_time = new SimpleDateFormat("HH:mm");

        Date time = (Date) val.getValue();

        String res = formater_time.format(time.getTime());

        return res;
    }

    @Override
    public boolean wantDoLoadSelf() {
        return true;
    }

    @Override
    public boolean loadToValue(DBValue val, String s, int row) {
       
        DBDateTime time = (DBDateTime) val;
        
        if( time.loadTimePart(s + ":00") == false )
        {
            return false;
        }
        
        return true;
    }

    @Override
    public boolean acceptData(String data) {
        if (data.matches("(([0-1][0-9])|(2[0-3])):[0-5][0-9]") == true) {
            return true;
        }
        return false;
    }
}
