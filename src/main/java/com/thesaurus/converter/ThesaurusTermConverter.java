package com.thesaurus.converter;

import com.thesaurus.domain.core.TermType;
import com.thesaurus.domain.core.ThesaurusTerm;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ThesaurusTermConverter {

    private static final String SEMICOLON = ";";

    public static List<ThesaurusTerm> convert(Resource resource) throws IOException {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

            return reader.lines()
                         .toList()
                         .stream()
                         .filter(StringUtils::hasText)
                         .map(thesaurusTerm -> thesaurusTerm.split(SEMICOLON))
                         .filter(array -> array.length == 2)
                         .map(array -> ThesaurusTerm.builder()
                                                    .term(array[0])
                                                    .termType(TermType.find(array[1]))
                                                    .build())
                         .toList();
        }
    }
}
