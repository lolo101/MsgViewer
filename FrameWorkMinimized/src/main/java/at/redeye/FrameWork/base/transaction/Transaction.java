/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.base.transaction;

import at.redeye.FrameWork.base.bindtypes.DBDateTime;
import at.redeye.FrameWork.base.bindtypes.DBStrukt;
import at.redeye.FrameWork.base.bindtypes.DBValue;
import at.redeye.SqlDBInterface.SqlDBConnection.impl.SupportedDBMSTypes;
import at.redeye.SqlDBInterface.SqlDBIO.StmtExecInterface;
import at.redeye.SqlDBInterface.SqlDBIO.TypeRegistrationInterface;

import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public interface Transaction {

    boolean close();

    void commit();

    List<DBStrukt> fetchTable(DBStrukt binddesc);

    List<DBStrukt> fetchTable(DBStrukt binddesc, String where);

    <T extends DBStrukt> List<T> fetchTable2(T binddesc);

    <T extends DBStrukt> List<T> fetchTable2(T binddesc, String where);

    <T extends DBStrukt> ArrayList<T> fetchTableList(T binddesc);

    <T extends DBStrukt> ArrayList<T> fetchTableList(T binddesc, String where);

    boolean fetchTableWithPrimkey(DBStrukt binddesc);

    SupportedDBMSTypes getDBMSType();

    String getDayStmt(String column, DBDateTime day);

    String getDayStmt(String column, String dayStr);

    String getDayStmt(String string, Date toDate);

    String getDayStmt(String string, LocalDate day);

    String getDayStmt(DBDateTime to, LocalDate date);

    String getGUIFilterWhereStmt(Vector<? extends JComponent> fromFilter, Vector<? extends JComponent> toFilter);

    String getHigherDate(String column, LocalDate dm_from);

    String getHigherDate(DBDateTime column, LocalDate dm_from);

    String getHigherDateExl(String column, LocalDate dm_from);

    String getHigherDateExl(DBDateTime column, LocalDate dm_from);

    String getLowerDate(String column, LocalDate dm_from);

    String getLowerDate(DBDateTime column, LocalDate dm_from);

    String getLowerDateExl(String column, LocalDate dm_from);

    String getLowerDateExl(DBDateTime column, LocalDate dm_from);

    int getNewSequenceValue(String seqName, int magic);

    int getNewSequenceValues(String seqName, int number, int magic);

    String getPeriodStmt(String column, DBDateTime begin, DBDateTime end);

    String getPeriodStmt(String column, String beginStr, String endStr);

    String getPeriodStmt(String string, LocalDate dm_from, LocalDate dm_to);

    /**
     *
     * @param column
     * @param dm_from
     * @param dm_to
     * @return
     */
    String getPeriodStmt(DBValue column, LocalDate dm_from, LocalDate dm_to);

    /**
     * @return: column1 &gt;= date && column2 &lt; date
     */
    String getPeriodStmt(String column1, String column2, LocalDate date);

    /**
     * @return: column1 &gt;= date && column2 &lt; date
     */
    String getPeriodStmt(DBValue column1, DBValue column2, LocalDate dm);

    String getSql();

    StmtExecInterface getStmtExecInterface();

    TypeRegistrationInterface getTypeRegistration();

    int insertValues(DBStrukt binddesc);

    boolean isOpen();

    String markColumn(String in);

    String markColumn(DBValue val);

    /**
     * Erzeugt TABELLE.name paar zb: PB.login, korrekt hervorgehoben für das DBMS
     * @param table
     * @param column
     * @return
     */
    String markColumn(DBStrukt table, DBValue column);

    String markTable(String in);

    String markTable(DBStrukt table);

    void rollback();

    int updateValues(String stmt);

    int updateValues(DBStrukt binddesc);

    int updateValues(DBStrukt binddesc, String where);

}
