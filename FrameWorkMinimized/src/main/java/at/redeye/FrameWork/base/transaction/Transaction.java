package at.redeye.FrameWork.base.transaction;

import at.redeye.FrameWork.base.bindtypes.DBDateTime;
import at.redeye.FrameWork.base.bindtypes.DBStrukt;

import javax.swing.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public interface Transaction {

    boolean close();

    void commit();

    List<DBStrukt> fetchTable(DBStrukt binddesc);

    List<DBStrukt> fetchTable(DBStrukt binddesc, String where);

    boolean fetchTableWithPrimkey(DBStrukt binddesc);

    String getDayStmt(String column, DBDateTime day);

    String getDayStmt(String column, String dayStr);

    String getDayStmt(String string, Date toDate);

    String getDayStmt(String string, LocalDate day);

    String getDayStmt(DBDateTime to, LocalDate date);

    String getGUIFilterWhereStmt(Vector<? extends JComponent> fromFilter, Vector<? extends JComponent> toFilter);

    String getLowerDate(String column, LocalDate dm_from);

    String getLowerDate(DBDateTime column, LocalDate dm_from);

    int insertValues(DBStrukt binddesc);

    boolean isOpen();

    String markTable(String in);

    String markTable(DBStrukt table);

    void rollback();

    int updateValues(DBStrukt binddesc);
}
