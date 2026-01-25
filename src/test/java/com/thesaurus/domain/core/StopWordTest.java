package com.thesaurus.domain.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StopWordTest {

    @Test
    void constructor_null_throws() {

        assertThrows(IllegalArgumentException.class, () -> new StopWord(null));
    }

    @Test
    void constructor_emptyString_throws() {

        assertThrows(IllegalArgumentException.class, () -> new StopWord(""));
    }

    @Test
    void constructor_whitespaceOnly_throws() {

        assertThrows(IllegalArgumentException.class, () -> new StopWord("   \n\t  "));
    }

    @Test
    void constructor_trims_and_lowercases_and_strips_punctuation() {

        StopWord sw = new StopWord("  ThE!!  ");
        assertEquals("the", sw.word());
    }

    @Test
    void constructor_normalizes_internal_whitespace_to_single_spaces() {

        StopWord sw = new StopWord("  van   der \n  sar  ");
        assertEquals("van der sar", sw.word());
    }

    @Test
    void constructor_keeps_letters_and_numbers() {

        StopWord sw = new StopWord("  a1b2  ");
        assertEquals("a1b2", sw.word());
    }
}
