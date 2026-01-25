package com.thesaurus.domain.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WordTest {

    @Test
    void constructor_null_throws() {

        assertThrows(IllegalArgumentException.class, () -> new Word(null));
    }

    @Test
    void constructor_emptyString_throws() {

        assertThrows(IllegalArgumentException.class, () -> new Word(""));
    }

    @Test
    void constructor_whitespaceOnly_throws() {

        assertThrows(IllegalArgumentException.class, () -> new Word("   \n\t  "));
    }

    @Test
    void constructor_normalizes_lowercase_and_strips_punctuation() {

        Word word = new Word("  HeLLo!! ");
        assertEquals("hello", word.word());
    }

    @Test
    void constructor_normalizes_internal_whitespace_to_single_spaces() {

        Word word = new Word("  new   york \n city ");
        assertEquals("new york city", word.word());
    }

    @Test
    void constructor_keeps_letters_and_numbers() {

        Word word = new Word("A1b2");
        assertEquals("a1b2", word.word());
    }

    @Test
    void equals_and_hashCode_work_for_record() {

        Word w1 = new Word("Amsterdam");
        Word w2 = new Word("amsterdam");

        assertEquals(w1, w2);
        assertEquals(w1.hashCode(), w2.hashCode());
    }

    @Test
    void record_toString_contains_normalized_word() {

        Word word = new Word("TeSt");
        assertTrue(word.toString()
                       .contains("test"));
    }
}
