package com.thesaurus.domain.core;

import com.thesaurus.util.TextNormalizerUtil;

import java.util.Objects;

public record StopWord(String word) {

    public StopWord {

        if (Objects.isNull(word) || word.length() == 0 || word.replaceAll("\\s+", "")
                                                              .length() == 0) {
            throw new IllegalArgumentException("word content cannot be empty!");
        }

        word = TextNormalizerUtil.normalize(word);
    }
}
