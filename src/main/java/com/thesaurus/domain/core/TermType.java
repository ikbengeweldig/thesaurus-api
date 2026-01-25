package com.thesaurus.domain.core;

import com.thesaurus.util.TextNormalizerUtil;

public enum TermType {

    LOCATION, PERSON_NAME, SUBJECT, ORGANISATION;

    public static TermType find(String termType) {

        return switch (TextNormalizerUtil.normalize(termType)) {
            case "geografischenamen" -> LOCATION;
            case "persoonsnamen" -> PERSON_NAME;
            case "namen" -> ORGANISATION;
            case "onderwerpen" -> SUBJECT;
            default -> throw new IllegalArgumentException("value not defined: " + termType);
        };
    }
}
