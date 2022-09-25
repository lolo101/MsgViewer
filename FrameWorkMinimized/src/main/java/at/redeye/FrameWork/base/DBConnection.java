package at.redeye.FrameWork.base;

import at.redeye.FrameWork.base.transaction.Transaction;
import at.redeye.SqlDBInterface.SqlDBConnection.impl.ConnectionDefinition;

public interface DBConnection {

    boolean close();

    boolean closeTransaction(Transaction trans);

    Transaction getNewTransaction();

    boolean open(ConnectionDefinition definition);

}
