/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.base.bindtypes;

import at.redeye.FrameWork.base.Root;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author martin
 */
public class DBFlagJaNein extends DBEnum 
{
    public static enum FLAGTYPES
    {
        NEIN,
        JA        
    };
           
    
    public static class FlagEnumHandler extends DBEnum.EnumHandler
    {
        FLAGTYPES types;

        public FlagEnumHandler()
        {
            types = FLAGTYPES.NEIN;
        }
        
        @Override
        public int getMaxSize() {
            int max=0;
            
            for( FLAGTYPES val :FLAGTYPES.values() )
            {
                if( max < val.toString().length() )
                    max = val.toString().length();
            }
            
            return max;
        }

        @Override
        public boolean setValue(String val) {
            try {
                types  = FLAGTYPES.valueOf(val);
            } catch( IllegalArgumentException ex ) {
                return false;
            }
            return true;
        }

        @Override
        public String getValue() {
            return types.toString();
        }

        @Override
        public EnumHandler getNewOne() {
            return new FlagEnumHandler();
        }

        @Override
        public List<String> getPossibleValues() {
            List<String> res = new ArrayList<String>();
            
            for( FLAGTYPES t : FLAGTYPES.values() )
                res.add( t.toString() );
            
            return res;
        }
    }
    
    public DBFlagJaNein( String name, String title )
    {
        super( name, title, new FlagEnumHandler() );
    }

    public DBFlagJaNein getNewOne()
    {
        return new DBFlagJaNein( name, title );
    }

    public boolean isYes()
    {
        FlagEnumHandler my_handler = (FlagEnumHandler) handler;

        if( my_handler.types == FLAGTYPES.JA )
            return true;

        return false;
    }

    public boolean isNo()
    {
        return !isYes();
    }


    @Override
    public void initLocalization(Root root)
    {
        root.loadMlM4Class(this, "de");
    }
 
}
