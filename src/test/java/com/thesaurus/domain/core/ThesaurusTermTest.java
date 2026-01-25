package com.thesaurus.domain.core;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ThesaurusTermTest {

    @Test
    void constructor_sets_term_and_type_and_initializes_children() {

        ThesaurusTerm term = new ThesaurusTerm("Amsterdam", TermType.LOCATION);

        assertEquals("Amsterdam", term.getTerm());
        assertEquals(TermType.LOCATION, term.getTermType());
        assertNotNull(term.getChildren(), "children map must be initialized");
        assertTrue(term.getChildren()
                       .isEmpty());
    }

    @Test
    void builder_initializes_children_map_by_default() {

        ThesaurusTerm term = ThesaurusTerm.builder()
                                          .term("VPRO")
                                          .termType(TermType.ORGANISATION)
                                          .build();

        assertNotNull(term.getChildren(), "children map must be initialized");
        assertTrue(term.getChildren()
                       .isEmpty());
    }

    @Test
    void equals_same_term_and_type_returns_true() {

        ThesaurusTerm t1 = new ThesaurusTerm("Nederland", TermType.LOCATION);
        ThesaurusTerm t2 = new ThesaurusTerm("Nederland", TermType.LOCATION);

        assertEquals(t1, t2);
        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    void equals_different_term_returns_false() {

        ThesaurusTerm t1 = new ThesaurusTerm("Amsterdam", TermType.LOCATION);
        ThesaurusTerm t2 = new ThesaurusTerm("Rotterdam", TermType.LOCATION);

        assertNotEquals(t1, t2);
    }

    @Test
    void equals_different_type_returns_false() {

        ThesaurusTerm t1 = new ThesaurusTerm("VPRO", TermType.ORGANISATION);
        ThesaurusTerm t2 = new ThesaurusTerm("VPRO", TermType.SUBJECT);

        assertNotEquals(t1, t2);
    }

    @Test
    void equals_ignores_children_map() {

        ThesaurusTerm t1 = new ThesaurusTerm("New York", TermType.LOCATION);
        ThesaurusTerm t2 = new ThesaurusTerm("New York", TermType.LOCATION);

        t1.getChildren()
          .put("city", new ThesaurusTerm("New York City", TermType.LOCATION));

        assertEquals(t1, t2, "children must not affect equality");
    }

    @Test
    void children_map_is_mutable_and_shared_per_instance() {

        ThesaurusTerm parent = new ThesaurusTerm("New", TermType.LOCATION);
        ThesaurusTerm child = new ThesaurusTerm("New York", TermType.LOCATION);

        parent.getChildren()
              .put("york", child);

        Map<String, ThesaurusTerm> children = parent.getChildren();
        assertEquals(1, children.size());
        assertSame(child, children.get("york"));
    }

    @Test
    void equals_null_and_other_type_return_false() {

        ThesaurusTerm term = new ThesaurusTerm("Amsterdam", TermType.LOCATION);

        assertNotEquals(term, null);
        assertNotEquals(term, "Amsterdam");
    }
}
