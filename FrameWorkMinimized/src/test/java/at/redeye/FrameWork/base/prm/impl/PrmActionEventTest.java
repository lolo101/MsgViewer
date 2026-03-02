package at.redeye.FrameWork.base.prm.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PrmActionEventTest {
    @Test
    void should_have_changed_value() {
        PrmActionEvent event = new PrmActionEvent();
        event.getOldPrmValue().loadFromString("old value");
        event.getNewPrmValue().loadFromString("new value");

        assertTrue(event.valueHasChanged());
    }

    @Test
    void should_not_have_changed_value() {
        PrmActionEvent event = new PrmActionEvent();
        event.getOldPrmValue().loadFromString("same value");
        event.getNewPrmValue().loadFromString("same value");

        assertFalse(event.valueHasChanged());
    }
}