package com.thesaurus.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TextNormalizerUtil {

    public static String normalize(String content) {

        content = Normalizer.normalize(content, Normalizer.Form.NFKC);
        content = content.toLowerCase(Locale.ROOT);
        content = content.replaceAll("[^\\p{L}\\p{N} ]+", " ");
        content = content.replaceAll("\\s+", " ")
                         .trim();

        return content;
    }

    public static List<String> tokenise(String content) {

        return Optional.ofNullable(content)
                       .filter(StringUtils::hasText)
                       .map(TextNormalizerUtil::normalize)
                       .filter(StringUtils::hasText)
                       .map(normalized -> normalized.split("\\s+"))
                       .map(Arrays::asList)
                       .orElseGet(ArrayList::new);
    }
}
