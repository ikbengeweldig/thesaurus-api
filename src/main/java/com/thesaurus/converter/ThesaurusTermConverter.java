package com.thesaurus.converter;

import com.thesaurus.domain.core.TermType;
import com.thesaurus.domain.core.ThesaurusTerm;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ThesaurusTermConverter {

    private static final String SEMICOLON = ";";

    public static List<ThesaurusTerm> convert(Resource resource) throws IOException {

        List<String> thesaurusTerms = Files.readAllLines(resource.getFilePath());
        return thesaurusTerms.stream()
                             .map(thesaurusTerm -> thesaurusTerm.split(SEMICOLON))
                             .map(array -> ThesaurusTerm.builder()
                                                        .term(array[0])
                                                        .termType(TermType.find(array[1]))
                                                        .build())
                             .toList();
    }
}
