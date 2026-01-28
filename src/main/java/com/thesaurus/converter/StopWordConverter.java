package com.thesaurus.converter;

import com.thesaurus.domain.core.StopWord;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StopWordConverter {

    public static List<StopWord> convert(Resource resource) throws IOException {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

            return reader.lines()
                         .toList()
                         .stream()
                         .map(StopWord::new)
                         .toList();
        }
    }
}
