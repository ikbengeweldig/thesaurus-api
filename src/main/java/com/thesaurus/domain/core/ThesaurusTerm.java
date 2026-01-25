package com.thesaurus.domain.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThesaurusTerm implements TrieNode {

    @Builder.Default
    private Map<String, ThesaurusTerm> children = new HashMap<>();

    private String term;

    private TermType termType;


    public ThesaurusTerm(String term, TermType termType) {
        this.children = new HashMap<>();
        this.term = term;
        this.termType = termType;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof ThesaurusTerm other)) {
            return false;
        }
        return Objects.equals(term, other.term) && Objects.equals(termType, other.termType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(term, termType);
    }
}
