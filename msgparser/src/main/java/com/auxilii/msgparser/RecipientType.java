package com.auxilii.msgparser;

public enum RecipientType {
    FROM(0),
    TO(1),
    CC(2),
    BCC(3);

    private final int type;

    private RecipientType(int type) {
        this.type = type;
    }

    public static RecipientType from(long type) {
        for (RecipientType value : values()) {
            if (value.type == type) {
                return value;
            }
        }
        throw new IllegalArgumentException("No RecipientType with id " + type);
    }

    public int getValue() {
        int flag = 0x10000000;
        return flag | type;
    }
}
