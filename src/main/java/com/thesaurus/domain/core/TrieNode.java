package com.thesaurus.domain.core;

import java.util.Map;

public interface TrieNode {

    Map<String, ThesaurusTerm> getChildren();

    TermType getTermType();

    void setTermType(TermType termType);

    String getTerm();

    void setTerm(String term);
}
