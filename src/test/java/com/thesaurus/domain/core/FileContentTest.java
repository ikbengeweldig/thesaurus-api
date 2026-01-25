package com.thesaurus.domain.core;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileContentTest {

    @Test
    void constructor_null_throws() {

        assertThrows(IllegalArgumentException.class, () -> new FileContent(null));
    }

    @Test
    void constructor_blank_throws() {

        assertThrows(IllegalArgumentException.class, () -> new FileContent("   \n\t  "));
    }

    @Test
    void getWords_returns_normalized_tokens_lowercased_no_punctuation() {

        FileContent fc = new FileContent("Hello,\nWORLD!!  ");

        List<Word> words = fc.getWords();

        assertEquals(List.of(new Word("hello"), new Word("world")), words);
    }

    @Test
    void removeStopWords_removes_only_standalone_words_not_substrings() {

        FileContent fc = new FileContent("the theatre theme THE");

        fc.removeStopWords(List.of(new StopWord("the")));

        assertEquals(
                List.of(new Word("theatre"), new Word("theme")),
                fc.getWords()
        );
    }

    @Test
    void removeStopWords_collapses_whitespace_after_removal() {

        FileContent fc = new FileContent("a   b \n c");
        fc.removeStopWords(List.of(new StopWord("b")));

        assertEquals(List.of(new Word("a"), new Word("c")), fc.getWords());
    }

    @Test
    void findThesaurusTerms_prefers_longest_match_and_skips_overlaps() {

        FileContent fc = new FileContent("New York City is bigger than New York");

        ThesaurusTerm newYork = ThesaurusTerm.builder()
                                             .term("New York")
                                             .termType(TermType.LOCATION)
                                             .build();

        ThesaurusTerm newYorkCity = ThesaurusTerm.builder()
                                                 .term("New York City")
                                                 .termType(TermType.LOCATION)
                                                 .build();

        List<ThesaurusTerm> found = fc.findThesaurusTerms(List.of(newYork, newYorkCity));

        assertEquals(2, found.size());

        assertEquals("New York City",
                     found.get(0)
                          .getTerm());
        assertEquals(TermType.LOCATION,
                     found.get(0)
                          .getTermType());

        assertEquals("New York",
                     found.get(1)
                          .getTerm());
        assertEquals(TermType.LOCATION,
                     found.get(1)
                          .getTermType());
    }

    @Test
    void findThesaurusTerms_null_input_returns_empty_list() {

        FileContent fc = new FileContent("Amsterdam");
        List<ThesaurusTerm> found = fc.findThesaurusTerms(null);

        assertNotNull(found);
        assertTrue(found.isEmpty());
    }
}
