package com.thesaurus.domain.core;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrieTest {

    @Test
    void extract_null_words_returns_empty() {

        Trie trie = new Trie();
        assertTrue(trie.extract(null)
                       .isEmpty());
    }

    @Test
    void extract_empty_words_returns_empty() {

        Trie trie = new Trie();
        assertTrue(trie.extract(List.of())
                       .isEmpty());
    }

    @Test
    void extract_no_terms_inserted_returns_empty() {

        Trie trie = new Trie();

        List<ThesaurusTerm> found = trie.extract(List.of(
                new Word("amsterdam"),
                new Word("is"),
                new Word("mooi")
        ));

        assertTrue(found.isEmpty());
    }

    @Test
    void insert_single_word_term_extract_finds_it() {

        Trie trie = new Trie();
        trie.insert(new ThesaurusTerm("Amsterdam", TermType.LOCATION));

        List<ThesaurusTerm> found = trie.extract(List.of(
                new Word("amsterdam"),
                new Word("is"),
                new Word("mooi")
        ));

        assertEquals(1, found.size());
        assertEquals("Amsterdam",
                     found.get(0)
                          .getTerm());
        assertEquals(TermType.LOCATION,
                     found.get(0)
                          .getTermType());
    }

    @Test
    void insert_multi_word_term_extract_finds_it() {

        Trie trie = new Trie();
        trie.insert(new ThesaurusTerm("New York City", TermType.LOCATION));

        List<ThesaurusTerm> found = trie.extract(List.of(
                new Word("new"),
                new Word("york"),
                new Word("city"),
                new Word("is"),
                new Word("big")
        ));

        assertEquals(1, found.size());
        assertEquals("New York City",
                     found.get(0)
                          .getTerm());
        assertEquals(TermType.LOCATION,
                     found.get(0)
                          .getTermType());
    }

    @Test
    void extract_prefers_longest_match_starting_at_position_and_skips_overlaps() {

        Trie trie = new Trie();
        trie.insert(new ThesaurusTerm("New York", TermType.LOCATION));
        trie.insert(new ThesaurusTerm("New York City", TermType.LOCATION));

        List<ThesaurusTerm> found = trie.extract(List.of(
                new Word("new"),
                new Word("york"),
                new Word("city"),
                new Word("is"),
                new Word("bigger"),
                new Word("than"),
                new Word("new"),
                new Word("york")
        ));

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
    void extract_can_return_multiple_non_overlapping_matches() {

        Trie trie = new Trie();
        trie.insert(new ThesaurusTerm("amsterdam", TermType.LOCATION));
        trie.insert(new ThesaurusTerm("vpro", TermType.ORGANISATION));

        List<ThesaurusTerm> found = trie.extract(List.of(
                new Word("vpro"),
                new Word("in"),
                new Word("amsterdam")
        ));

        assertEquals(2, found.size());
        assertEquals("vpro",
                     found.get(0)
                          .getTerm());
        assertEquals(TermType.ORGANISATION,
                     found.get(0)
                          .getTermType());
        assertEquals("amsterdam",
                     found.get(1)
                          .getTerm());
        assertEquals(TermType.LOCATION,
                     found.get(1)
                          .getTermType());
    }

    @Test
    void extract_does_not_match_if_word_sequence_breaks() {

        Trie trie = new Trie();
        trie.insert(new ThesaurusTerm("new york city", TermType.LOCATION));

        List<ThesaurusTerm> found = trie.extract(List.of(
                new Word("new"),
                new Word("york"),
                new Word("big"),
                new Word("city")
        ));

        assertTrue(found.isEmpty());
    }

    @Test
    void insert_term_with_punctuation_is_tokenised_and_matches_normalized_words() {

        Trie trie = new Trie();
        trie.insert(new ThesaurusTerm("U.S.A.", TermType.LOCATION));

        List<ThesaurusTerm> found = trie.extract(List.of(
                new Word("u"),
                new Word("s"),
                new Word("a")
        ));

        assertEquals(1, found.size());
        assertEquals("U.S.A.",
                     found.get(0)
                          .getTerm());
        assertEquals(TermType.LOCATION,
                     found.get(0)
                          .getTermType());
    }

    @Test
    void insert_null_term_then_extract_does_not_throw_and_ignores_it() {

        Trie trie = new Trie();

        trie.insert(ThesaurusTerm.builder()
                                 .term(null)
                                 .termType(null)
                                 .build());

        List<ThesaurusTerm> found = trie.extract(List.of(
                new Word("anything")
        ));

        assertTrue(found.isEmpty());
    }
}
