package com.auxilii.msgparser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PidTest {

    @Test
    void should_discriminate_pid_by_type() {
        assertEquals(Pid.PidTagBodyHtml, Pid.from(0x1013, Ptyp.PtypString));
        assertEquals(Pid.PidTagHtml, Pid.from(0x1013, Ptyp.PtypBinary));
    }
}