package com.thesaurus.usecase;

import com.thesaurus.api.model.AlgorithmTypeEnum;
import com.thesaurus.converter.StopWordConverter;
import com.thesaurus.converter.ThesaurusTermConverter;
import com.thesaurus.domain.core.AlgorithmType;
import com.thesaurus.domain.core.FileContent;
import com.thesaurus.domain.core.StopWord;
import com.thesaurus.domain.core.ThesaurusTerm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.detect.AutoDetectReader;
import org.apache.tika.exception.TikaException;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static java.util.Optional.ofNullable;

@Slf4j
@Component
@RequiredArgsConstructor
public class TermRetrieverApplication {

    public List<ThesaurusTerm> retrieveTerms(Resource inputFile,
                                             Resource thesaurusTermsFile,
                                             Resource stopWordsFile,
                                             AlgorithmTypeEnum algorithmType) {

        try {
            List<StopWord> stopWords = StopWordConverter.convert(stopWordsFile);
            FileContent fileContent = getFileContent(inputFile, stopWords);

            List<ThesaurusTerm> thesaurusTerms = ThesaurusTermConverter.convert(thesaurusTermsFile);
            AlgorithmType algType = ofNullable(algorithmType).map(Enum::name)
                                                             .map(AlgorithmType::valueOf)
                                                             .orElse(AlgorithmType.TRIE);
            return fileContent.findThesaurusTerms(thesaurusTerms, algType);
        } catch (IOException e) {
            log.debug("error happened while processing the file: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private FileContent getFileContent(Resource inputFile, List<StopWord> stopWords) {

        try {
            String fileContentAsString = readResourceAsString(inputFile);
            FileContent fileContent = new FileContent(fileContentAsString);
            fileContent.removeStopWords(stopWords);
            return fileContent;
        } catch (IOException e) {
            log.error("error happened during reading the file: {}", inputFile);
            throw new RuntimeException(e);
        }
    }

    private String readResourceAsString(Resource resource) throws IOException {

        Charset detected;
        try {
            AutoDetectReader autoDetectReader = new AutoDetectReader(resource.getInputStream());
            detected = autoDetectReader.getCharset();
        } catch (TikaException e) {
            detected = StandardCharsets.UTF_8;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), detected))) {

            return reader.readAllAsString();
        }
    }
}
