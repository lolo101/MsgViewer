package at.redeye.FrameWork.base.bindtypes;

import at.redeye.FrameWork.base.Root;

public class DBFlagJaNein extends DBEnum<DBFlagJaNein.FLAGTYPES> {
    public enum FLAGTYPES {
        NEIN,
        JA
    }

    public static class FlagEnumHandler extends DBEnum.EnumHandler<FLAGTYPES> {
        public FlagEnumHandler() {
            super(FLAGTYPES.class, FLAGTYPES.NEIN);
        }

        @Override
        public FlagEnumHandler getNewOne() {
            return new FlagEnumHandler();
        }
    }

    public DBFlagJaNein(String name, String title) {
        super(name, title, new FlagEnumHandler());
    }

    public DBFlagJaNein getNewOne() {
        return new DBFlagJaNein(name, title);
    }

    public boolean isYes() {
        FlagEnumHandler my_handler = (FlagEnumHandler) handler;
        return my_handler.value == FLAGTYPES.JA;
    }

    public boolean isNo() {
        return !isYes();
    }


    @Override
    public void initLocalization(Root root) {
        root.loadMlM4Class(this, "de");
    }

}
