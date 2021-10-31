package com.auxilii.msgparser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessageTest {
    @ParameterizedTest
    @ValueSource(strings = {"A: ", "Re: ", "Fwd: "})
    void should_find_subject_prefix(String prefix) {
        Message message = new Message();

        message.setSubject(prefix + "Yo !");

        assertEquals(prefix, message.getSubjectPrefix());
        assertEquals("Yo !", message.getTopic());
    }

    @Test
    void should_skip_spaces_in_subject_prefix() {
        Message message = new Message();

        message.setSubject("Re:       Yo !");

        assertEquals("Re: ", message.getSubjectPrefix());
        assertEquals("Yo !", message.getTopic());
    }

    @Test
    void should_not_count_numbers_as_subject_prefix() {
        Message message = new Message();

        message.setSubject("Re1: Yo !");

        assertEquals("", message.getSubjectPrefix());
        assertEquals("Re1: Yo !", message.getTopic());
    }

    @Test
    void should_not_count_spaces_as_subject_prefix() {
        Message message = new Message();

        message.setSubject("Re : Yo !");

        assertEquals("", message.getSubjectPrefix());
        assertEquals("Re : Yo !", message.getTopic());
    }

    @Test
    void should_not_count_colon_as_subject_prefix() {
        Message message = new Message();

        message.setSubject(":Re: Yo !");

        assertEquals("", message.getSubjectPrefix());
        assertEquals(":Re: Yo !", message.getTopic());
    }
}
