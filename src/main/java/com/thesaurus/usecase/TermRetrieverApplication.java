package com.thesaurus.usecase;

import com.thesaurus.domain.core.FileContent;
import com.thesaurus.domain.core.StopWord;
import com.thesaurus.domain.core.ThesaurusTerm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TermRetrieverApplication {

    private static final String WINDOWS_1252 = "windows-1252";

    private final List<StopWord> stopWords;
    private final List<ThesaurusTerm> thesaurusTerms;

    public List<ThesaurusTerm> retrieveTerms(Resource inputFile) {

        FileContent fileContent = getFileContent(inputFile);
        return fileContent.findThesaurusTerms(thesaurusTerms);
    }

    private FileContent getFileContent(Resource inputFile) {

        try {
            Path filePath = getFilePath(inputFile);
            String fileContentAsString = readString(filePath);
            FileContent fileContent = new FileContent(fileContentAsString);
            fileContent.removeStopWords(stopWords);
            return fileContent;
        } catch (IOException e) {
            log.error("error happened during reading the file: {}", inputFile);
            throw new RuntimeException(e);
        }
    }

    private Path getFilePath(Resource inputFile) throws IOException {

        return inputFile.getFilePath();
    }

    private String readString(Path filePath) throws IOException {

        try {
            return Files.readString(filePath, StandardCharsets.UTF_8);
        } catch (MalformedInputException e) {
            return Files.readString(filePath, Charset.forName(WINDOWS_1252));
        }
    }
}
