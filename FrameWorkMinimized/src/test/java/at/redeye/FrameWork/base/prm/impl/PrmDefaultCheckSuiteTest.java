package at.redeye.FrameWork.base.prm.impl;

import org.junit.jupiter.api.Test;

import static at.redeye.FrameWork.base.prm.PrmDefaultChecksInterface.PRM_IS_LONG;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PrmDefaultCheckSuiteTest {

    @Test
    void should_validate_when_checking_nothing() {
        PrmDefaultCheckSuite checkSuite = new PrmDefaultCheckSuite(0L);
        PrmActionEvent event = new PrmActionEvent();
        assertTrue(checkSuite.doChecks(event));
    }

    @Test
    void should_not_validate_when_parameter_value_is_missing() {
        PrmDefaultCheckSuite checkSuite = new PrmDefaultCheckSuite(PRM_IS_LONG);
        PrmActionEvent event = new PrmActionEvent();
        assertFalse(checkSuite.doChecks(event));
    }

    @Test
    void should_not_validate_when_parameter_value_is_invalid() {
        PrmDefaultCheckSuite checkSuite = new PrmDefaultCheckSuite(PRM_IS_LONG);
        PrmActionEvent event = new PrmActionEvent();
        event.setNewPrmValue("abc");
        assertFalse(checkSuite.doChecks(event));
    }

    @Test
    void should_validate_when_parameter_value_is_valid() {
        PrmDefaultCheckSuite checkSuite = new PrmDefaultCheckSuite(PRM_IS_LONG);
        PrmActionEvent event = new PrmActionEvent();
        event.setNewPrmValue("1");
        assertTrue(checkSuite.doChecks(event));
    }
}