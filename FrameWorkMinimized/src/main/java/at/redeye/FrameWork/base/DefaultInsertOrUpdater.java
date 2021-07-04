package at.redeye.FrameWork.base;

import at.redeye.FrameWork.base.bindtypes.DBStrukt;
import at.redeye.FrameWork.base.transaction.Transaction;

public class DefaultInsertOrUpdater
{
   public static void insertOrUpdateValuesWithPrimKey(Transaction trans, DBStrukt entry) {
        DBStrukt e = entry.getNewOne();

        e.loadFromCopy(entry);

        if (trans.fetchTableWithPrimkey(e)) {
            trans.updateValues(entry);
        } else {
            trans.insertValues(entry);
        }
    }
}
