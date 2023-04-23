package at.redeye.FrameWork.base.bindtypes;

import java.util.List;

public class DBFlagInteger extends DBEnumAsInteger
{
    public static class FlagIntegerHandler extends EnumAsIntegerHandler
    {
        private Integer value = 0;

        @Override
        public boolean setValue(String val) {
            if( val.equals("1") || val.equalsIgnoreCase("X") )
                value = 1;
            else
                value = 0;

            return true;
        }

        @Override
        public void setValue(Integer val) {
            value = val > 0 ? 1 : 0;
        }

        @Override
        public Integer getValue() {
            return value;
        }

        @Override
        public String getValueAsString() {
            return value > 0 ? "X" : " ";
        }

        @Override
        public Iterable<String> getPossibleValues() {
            return List.of("X", " ");
        }
    }

    private DBFlagInteger(String name, String title) {
        super(name, title, new FlagIntegerHandler());
    }
}
