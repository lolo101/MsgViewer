/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.base;

import at.redeye.FrameWork.base.bindtypes.DBHistory;
import at.redeye.FrameWork.base.bindtypes.DBStrukt;
import at.redeye.FrameWork.base.transaction.Transaction;
import at.redeye.SqlDBInterface.SqlDBIO.impl.TableBindingNotRegisteredException;
import at.redeye.SqlDBInterface.SqlDBIO.impl.UnsupportedDBDataTypeException;
import at.redeye.SqlDBInterface.SqlDBIO.impl.WrongBindFileFormatException;
import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author martin
 */
public class DefaultInsertOrUpdater 
{
   public static void insertOrUpdateValuesWithPrimKey(Transaction trans, DBStrukt entry) 
           throws UnsupportedDBDataTypeException, WrongBindFileFormatException, SQLException, TableBindingNotRegisteredException, IOException 
   {
        DBStrukt e = entry.getNewOne();

        e.loadFromCopy(entry);

        if (trans.fetchTableWithPrimkey(e) == true) {            
            trans.updateValues(entry);
        } else {
            trans.insertValues(entry);
        }
    }

   public static void insertOrUpdateValuesWithPrimKey(Transaction trans, DBStrukt entry, DBHistory hist, String User ) 
           throws UnsupportedDBDataTypeException, WrongBindFileFormatException, SQLException, TableBindingNotRegisteredException, IOException 
   {
        DBStrukt e = entry.getNewOne();

        e.loadFromCopy(entry);

        if (trans.fetchTableWithPrimkey(e) == true) {
            hist.setAeHist(User);
            trans.updateValues(entry);
        } else {
            hist.setAnHist(User);
            trans.insertValues(entry);
        }
    }
   
}
