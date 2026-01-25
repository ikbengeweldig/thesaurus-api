package com.thesaurus.domain.core;

import com.thesaurus.util.TextNormalizerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.util.regex.Pattern.quote;

@Slf4j
public class FileContent {

    private static final String STOP_WORD_REMOVAL_PATTERN = "(?<!\\p{L})(%s)(?!\\p{L})";

    private String content;

    public FileContent(String content) {

        if (Objects.isNull(content) || content.length() == 0 || content.replaceAll("\\s+", "")
                                                                       .length() == 0) {
            throw new IllegalArgumentException("file content cannot be empty!");
        }

        this.content = TextNormalizerUtil.normalize(content);
    }

    public void removeStopWords(List<StopWord> stopWords) {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start("RemoveStopWords");

        for (StopWord sw : stopWords) {
            String pattern = STOP_WORD_REMOVAL_PATTERN.formatted(quote(sw.word()));
            content = content.replaceAll(pattern, " ");
        }

        content = content.replaceAll("\\s+", " ")
                         .trim();
        stopWatch.stop();
        log.debug("removing stop words took: {}", stopWatch.prettyPrint());
    }

    public List<Word> getWords() {

        return Arrays.stream(content.split("\\s+"))
                     .map(Word::new)
                     .toList();
    }

    public List<ThesaurusTerm> findThesaurusTerms(List<ThesaurusTerm> thesaurusTerms) {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start("FindThesaurusTerms");

        Trie trie = new Trie();
        if (Objects.nonNull(thesaurusTerms)) {
            thesaurusTerms.forEach(trie::insert);

        }
        List<ThesaurusTerm> result = trie.extract(getWords());

        stopWatch.stop();
        log.debug("finding thesaurus terms took: {}", stopWatch.prettyPrint());

        return result;
    }
}
