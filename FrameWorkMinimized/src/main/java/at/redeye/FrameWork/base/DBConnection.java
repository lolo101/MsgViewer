/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.base;

import at.redeye.FrameWork.base.transaction.Transaction;
import at.redeye.SqlDBInterface.SqlDBConnection.impl.ConnectionDefinition;
import at.redeye.SqlDBInterface.SqlDBConnection.impl.MissingConnectionParamException;
import at.redeye.SqlDBInterface.SqlDBConnection.impl.UnSupportedDatabaseException;
import java.sql.SQLException;

/**
 *
 * @author martin
 */
public interface DBConnection {

    boolean close();

    boolean closeTransaction(Transaction trans);

    Transaction getDefaultTransaction();

    Transaction getNewTransaction();

    boolean open(ConnectionDefinition definition) throws ClassNotFoundException, SQLException, MissingConnectionParamException, UnSupportedDatabaseException;
    
}
