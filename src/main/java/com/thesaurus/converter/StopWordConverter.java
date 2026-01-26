package com.thesaurus.converter;

import com.thesaurus.domain.core.StopWord;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StopWordConverter {

    public static List<StopWord> convert(Resource resource) throws IOException {

        List<String> stopWords = Files.readAllLines(resource.getFilePath());
        return stopWords.stream()
                        .map(StopWord::new)
                        .toList();
    }
}
