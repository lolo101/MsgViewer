package com.auxilii.msgparser;

public enum RecipientType {
    FROM(0),
    TO(1),
    CC(2),
    BCC(3);

    private static final int MUST_RESEND = 0x10000000;
    private final int type;

    private RecipientType(int type) {
        this.type = type;
    }

    public static RecipientType from(int type) {
        int cleanType = type & 0xFF;
        for (RecipientType value : values()) {
            if (value.type == cleanType) {
                return value;
            }
        }
        throw new IllegalArgumentException("No RecipientType with id " + cleanType);
    }

    public int getValue() {
        return MUST_RESEND | type;
    }
}
