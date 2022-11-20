package at.redeye.FrameWork.base.transaction;

import at.redeye.FrameWork.base.bindtypes.DBStrukt;

public interface Transaction {

    boolean close();

    boolean fetchTableWithPrimkey(DBStrukt binddesc);

    int insertValues(DBStrukt binddesc);

    boolean isOpen();

    int updateValues(DBStrukt binddesc);
}
