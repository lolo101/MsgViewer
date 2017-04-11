/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.redeye.FrameWork.base.bindtypes.DBStruktTestCases;

import at.redeye.FrameWork.base.bindtypes.DBInteger;
import at.redeye.FrameWork.base.bindtypes.DBStrukt;

/**
 *
 * @author martin
 */
public class DBSimple extends  DBStrukt
{
    public static final String NAME = "Einfach";
    public static final String TITLE = "SIMPLE";
    public DBInteger id = new DBInteger("id");
    
    public DBSimple()
    {
        super( NAME, TITLE ); 
        
        add(id);
    }
    

    @Override
    public DBStrukt getNewOne() {
        return new DBSimple();
    }
    
}
