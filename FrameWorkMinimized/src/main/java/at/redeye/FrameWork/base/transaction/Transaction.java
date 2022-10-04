package at.redeye.FrameWork.base.transaction;

import at.redeye.FrameWork.base.bindtypes.DBStrukt;

import java.util.List;

public interface Transaction {

    boolean close();

    void commit();

    List<DBStrukt> fetchTable(DBStrukt binddesc);

    boolean fetchTableWithPrimkey(DBStrukt binddesc);

    int insertValues(DBStrukt binddesc);

    boolean isOpen();

    void rollback();

    int updateValues(DBStrukt binddesc);
}
