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
public class DBMoreComplex extends DBStrukt {    
    
    public DBInteger id = new DBInteger("id");
    public DBSimpleHist hist = new DBSimpleHist("HIST");
    public DBInteger hist_id = new DBInteger("hist_id");
    
    public DBMoreComplex()
    {
        super( "MORECOMPLEX" );
        
        add(id);        
        add(hist);
        add(hist_id);
                        
    }

    @Override
    public DBStrukt getNewOne() {
        return new DBMoreComplex();
    }
    
}

