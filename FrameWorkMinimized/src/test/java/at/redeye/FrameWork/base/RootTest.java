package at.redeye.FrameWork.base;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RootTest {
    @Test
    void should_select_configured_display_language() {
        BaseAppConfigDefinitions.DisplayLanguage.setConfigValue("display-language");
        Root root = new Root("test");
        assertEquals("display-language", root.getDisplayLanguage());
    }

    @Test
    void should_select_default_language_when_configured_language_is_blank() {
        BaseAppConfigDefinitions.DisplayLanguage.setConfigValue("");
        Root root = new Root("test");
        root.setDefaultLanguage("expected");
        assertEquals("expected", root.getDisplayLanguage());
    }

    @Test
    void should_select_default_language_when_configured_language_is_null() {
        Root root = new Root("test");
        root.setDefaultLanguage("expected");
        assertEquals("expected", root.getDisplayLanguage());
    }
}