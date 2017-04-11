/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.redeye.FrameWork.base.bindtypes.DBStruktTestCases;

import at.redeye.FrameWork.base.bindtypes.DBDateTime;
import at.redeye.FrameWork.base.bindtypes.DBString;
import at.redeye.FrameWork.base.bindtypes.DBStrukt;
import java.util.Date;

/**
 *
 * @author martin
 */
public class DBSimpleHist extends DBStrukt
{
    public DBString ae_user = new DBString( "ae_user", 100);
    public DBDateTime ae_zeit = new DBDateTime( "ae_zeit");
    
    public DBSimpleHist(String name)
    {
        super(name);
        
        add( ae_user );
        add( ae_zeit );
        
    }
    
    public void setAeHist( String user )
    {
        ae_zeit.loadFromCopy(new Date( System.currentTimeMillis() ));
        ae_user.loadFromCopy(user);
    }    

    @Override
    public DBStrukt getNewOne() {
        return new DBSimpleHist(getName());
    }
    
}
