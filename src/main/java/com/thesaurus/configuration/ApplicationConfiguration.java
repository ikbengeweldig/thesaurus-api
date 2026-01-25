package com.thesaurus.configuration;

import com.thesaurus.domain.core.StopWord;
import com.thesaurus.domain.core.TermType;
import com.thesaurus.domain.core.ThesaurusTerm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Configuration
public class ApplicationConfiguration {

    private static final String SEMICOLON = ";";

    @Bean
    List<StopWord> stopWords(@Value("classpath:input/stopwords.txt") Resource stopWordsFile) throws IOException {

        List<String> stopWords = Files.readAllLines(stopWordsFile.getFilePath());
        return stopWords.stream()
                        .map(StopWord::new)
                        .toList();
    }

    @Bean
    List<ThesaurusTerm> thesaurusTerms(@Value("classpath:input/gtaa-terms.csv") Resource thesaurusTermsFile) throws IOException {

        List<String> thesaurusTerms = Files.readAllLines(thesaurusTermsFile.getFilePath());
        return thesaurusTerms.stream()
                             .map(thesaurusTerm -> thesaurusTerm.split(SEMICOLON))
                             .map(array -> ThesaurusTerm.builder()
                                                        .term(array[0])
                                                        .termType(TermType.find(array[1]))
                                                        .build())
                             .toList();
    }
}
