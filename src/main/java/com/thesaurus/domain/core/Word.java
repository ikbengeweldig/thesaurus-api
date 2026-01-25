package com.thesaurus.domain.core;

import com.thesaurus.util.TextNormalizerUtil;

import java.util.Objects;

public record Word(String word) {

    public Word {

        if (Objects.isNull(word) || word.length() == 0 || word.replaceAll("\\s+", "")
                                                              .length() == 0) {
            throw new IllegalArgumentException("word content cannot be empty!");
        }

        word = TextNormalizerUtil.normalize(word);
    }
}
