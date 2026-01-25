package com.thesaurus.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TextNormalizerUtilTest {

    @Test
    void normalize_lowercases_and_trims() {

        String result = TextNormalizerUtil.normalize("  HeLLo WoRLd  ");
        assertEquals("hello world", result);
    }

    @Test
    void normalize_removes_punctuation_and_symbols() {

        String result = TextNormalizerUtil.normalize("Hello, world!!! #1");
        assertEquals("hello world 1", result);
    }

    @Test
    void normalize_collapses_multiple_whitespace() {

        String result = TextNormalizerUtil.normalize("a   b \n c\t d");
        assertEquals("a b c d", result);
    }

    @Test
    void normalize_keeps_letters_from_other_languages() {

        String result = TextNormalizerUtil.normalize("Ångström naïve façade");
        assertEquals("ångström naïve façade", result);
    }

    @Test
    void normalize_keeps_numbers() {

        String result = TextNormalizerUtil.normalize("Version 2.0 is out!");
        assertEquals("version 2 0 is out", result);
    }

    @Test
    void normalize_handles_unicode_compatibility_forms() {

        String result = TextNormalizerUtil.normalize("Ｆｕｌｌ－Ｗｉｄｔｈ");
        assertEquals("full width", result);
    }

    @Test
    void tokenise_null_returns_empty_list() {

        assertTrue(TextNormalizerUtil.tokenise(null)
                                     .isEmpty());
    }

    @Test
    void tokenise_empty_string_returns_empty_list() {

        assertTrue(TextNormalizerUtil.tokenise("")
                                     .isEmpty());
    }

    @Test
    void tokenise_whitespace_only_returns_empty_list() {

        assertTrue(TextNormalizerUtil.tokenise("   \n\t  ")
                                     .isEmpty());
    }

    @Test
    void tokenise_normalizes_and_splits_into_tokens() {

        List<String> tokens = TextNormalizerUtil.tokenise("Hello, WORLD!");
        assertEquals(List.of("hello", "world"), tokens);
    }

    @Test
    void tokenise_splits_on_any_whitespace() {

        List<String> tokens = TextNormalizerUtil.tokenise("a\tb\nc  d");
        assertEquals(List.of("a", "b", "c", "d"), tokens);
    }

    @Test
    void tokenise_preserves_word_order() {

        List<String> tokens = TextNormalizerUtil.tokenise("New York City");
        assertEquals(List.of("new", "york", "city"), tokens);
    }

    @Test
    void tokenise_handles_unicode_letters_correctly() {

        List<String> tokens = TextNormalizerUtil.tokenise("Málaga naïve");
        assertEquals(List.of("málaga", "naïve"), tokens);
    }

    @Test
    void tokenise_does_not_produce_empty_tokens() {

        List<String> tokens = TextNormalizerUtil.tokenise("  a   b   ");
        assertEquals(List.of("a", "b"), tokens);
    }
}
