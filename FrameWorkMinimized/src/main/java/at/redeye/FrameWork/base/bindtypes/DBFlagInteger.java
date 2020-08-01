/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.base.bindtypes;

import java.util.Vector;

/**
 *
 * @author martin
 */
public class DBFlagInteger extends DBEnumAsInteger
{
    public static class FlagIntegerHandler extends EnumAsIntegerHandler
    {
        Integer value = 0;

        @Override
        public int getMaxSize() {
            return 1;
        }

        @Override
        public boolean setValue(String val) {
            if( val.equals("1") || val.equalsIgnoreCase("X") )
                value = 1;
            else
                value = 0;

            return true;
        }

        @Override
        public boolean setValue(Integer val) {
            if( val > 0 )
                value = 1;
            else
                val = 0;

            return true;
        }

        @Override
        public Integer getValue() {
            return value;
        }

        @Override
        public String getValueAsString() {
            if( value > 0 )
                return "X";
            else
                return " ";
        }

        @Override
        public EnumAsIntegerHandler getNewOne() {
            return new FlagIntegerHandler();
        }

        @Override
        public Vector<String> getPossibleValues() {
           Vector<String> res = new Vector<>();

           res.add( "X" );
           res.add( " " );

           return res;
        }

        @Override
        public void refresh() {
            // nothing to do
        }

    }

    public DBFlagInteger( String name )
    {
        super( name, name, new FlagIntegerHandler() );
    }

    public DBFlagInteger( String name, String title )
    {
        super( name, title, new FlagIntegerHandler() );
    }

    public DBFlagInteger getNewOne()
    {
        return new DBFlagInteger(name, title);
    }
}
