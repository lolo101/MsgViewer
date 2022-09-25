package com.auxilii.msgparser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.ZonedDateTime;

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

    @ParameterizedTest
    @CsvSource(value = {
            "1 Jan 2022 12:06:30 +0000; 2022-01-01T12:06:30+00:00",
            "1 Jan 2022 12:06:30 -0130; 2022-01-01T12:06:30-01:30",
            "01 Jan 2022 12:06:30 +0000; 2022-01-01T12:06:30+00:00",
            "29 Feb 2020 12:06:30 +0000; 2020-02-29T12:06:30+00:00",
            "Sat, 29 Feb 2020 12:06:30 +0000; 2020-02-29T12:06:30+00:00",
            "Sat, 29 Feb 2020 12:06 +0000; 2020-02-29T12:06:00+00:00"
    }, delimiterString = ";")
    void should_parse_date(String line, ZonedDateTime expected) {
        ZonedDateTime date = ZonedDateTime.parse(line, Message.DATE_TIME_FORMATTER);
        Assertions.assertEquals(expected, date);
    }
}
