package net.sourceforge.MSGViewer.rtfparser;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RTFGroupTest {

    @Test
    void should_not_show_text_when_commands_empty() {
        RTFGroup group = new RTFGroup(StandardCharsets.UTF_8, new StringBuilder(), null);
        group.addTextContent("Coucou !");
        assertEquals("", group.getTextContent());
    }

    @Test
    void should_show_text_when_htmlrtf_is_disabled() {
        RTFGroup group = new RTFGroup(StandardCharsets.UTF_8, new StringBuilder(), null);
        group.handleCommand("\\htmlrtf0");
        group.addTextContent("Coucou !");
        assertEquals("Coucou !", group.getTextContent());
    }

    @Test
    void should_show_text_when_htmlrtf_is_disabled_and_another_command_is_issued() {
        RTFGroup group = new RTFGroup(StandardCharsets.UTF_8, new StringBuilder(), null);
        group.handleCommand("\\htmlrtf0");
        group.handleCommand("\\another-command");
        group.addTextContent("Coucou !");
        assertEquals("Coucou !", group.getTextContent());
    }

    @Test
    void should_not_show_text_when_htmlrtf_is_enabled() {
        RTFGroup group = new RTFGroup(StandardCharsets.UTF_8, new StringBuilder(), null);
        group.handleCommand("\\htmlrtf1");
        group.addTextContent("Coucou !");
        assertEquals("", group.getTextContent());
    }

    @Test
    void should_not_show_text_when_htmlrtf_is_reenabled() {
        RTFGroup group = new RTFGroup(StandardCharsets.UTF_8, new StringBuilder(), null);
        group.handleCommand("\\htmlrtf0");
        group.handleCommand("\\htmlrtf1");
        group.addTextContent("Coucou !");
        assertEquals("", group.getTextContent());
    }

    @Test
    void should_show_text_when_htmlrtf_is_redisabled() {
        RTFGroup group = new RTFGroup(StandardCharsets.UTF_8, new StringBuilder(), null);
        group.handleCommand("\\htmlrtf0");
        group.handleCommand("\\htmlrtf1");
        group.handleCommand("\\htmlrtf0");
        group.addTextContent("Coucou !");
        assertEquals("Coucou !", group.getTextContent());
    }

    @Test
    void should_inherit_parent_commands() {
        RTFGroup parent = new RTFGroup(StandardCharsets.UTF_8, new StringBuilder(), null);
        parent.handleCommand("\\htmlrtf0");
        RTFGroup group = new RTFGroup(StandardCharsets.UTF_8, new StringBuilder(), parent);
        group.addTextContent("Coucou !");
        assertEquals("Coucou !", group.getTextContent());
    }

    @Test
    void should_not_print_plain_text_bullets() {
        RTFGroup group = new RTFGroup(StandardCharsets.UTF_8, new StringBuilder(), null);
        group.handleCommand("\\htmlrtf0");
        group.handleCommand("\\pntext");
        group.addTextContent("Coucou !");
        assertEquals("", group.getTextContent());
    }
}