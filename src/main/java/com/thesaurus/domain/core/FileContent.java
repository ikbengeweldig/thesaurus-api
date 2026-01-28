package com.thesaurus.domain.core;

import com.thesaurus.util.TextNormalizerUtil;
import lombok.extern.slf4j.Slf4j;
import org.ahocorasick.trie.Emit;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.function.Predicate.not;
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

    public List<ThesaurusTerm> findThesaurusTerms(List<ThesaurusTerm> thesaurusTerms, AlgorithmType algorithmType) {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start("FindThesaurusTerms");


        if (isNull(thesaurusTerms) || thesaurusTerms.isEmpty()) {
            return List.of();
        }

        List<ThesaurusTerm> result = switch (algorithmType) {
            case TRIE -> {
                Trie trie = new Trie();
                if (nonNull(thesaurusTerms)) {
                    thesaurusTerms.forEach(trie::insert);

                }
                yield trie.extract(getWords());
            }
            case AHO_CORASICK -> {

                String tokenized = " " + String.join(" ",
                                                     getWords().stream()
                                                               .map(Word::word)
                                                               .toList()) + " ";

                Map<String, ThesaurusTerm> byKeyword = new HashMap<>();
                var builder = org.ahocorasick.trie.Trie.builder()
                                                       .ignoreCase();

                for (ThesaurusTerm thesaurusTerm : thesaurusTerms) {
                    ofNullable(thesaurusTerm).map(ThesaurusTerm::getTerm)
                                             .filter(not(String::isBlank))
                                             .ifPresent(keyword -> {
                                                 String kw = " " + keyword + " ";
                                                 byKeyword.put(kw, thesaurusTerm);
                                                 builder.addKeyword(kw);
                                             });
                }

                var ahoCorasick = builder.build();

                List<Emit> emits = new ArrayList<>(ahoCorasick.parseText(tokenized)
                                                              .stream()
                                                              .toList());

                emits.sort((a, b) -> {
                    int c = Integer.compare(a.getStart(), b.getStart());
                    if (c != 0) {
                        return c;
                    }
                    int lenA = a.getEnd() - a.getStart();
                    int lenB = b.getEnd() - b.getStart();
                    return Integer.compare(lenB, lenA);
                });

                List<ThesaurusTerm> results = new ArrayList<>();
                int lastEnd = -1;

                for (Emit e : emits) {
                    if (e.getStart() <= lastEnd) {
                        continue;
                    }
                    ThesaurusTerm matched = byKeyword.get(e.getKeyword());
                    if (nonNull(matched)) {
                        results.add(matched);
                        lastEnd = e.getEnd();
                    }
                }

                yield results;
            }
        };

        stopWatch.stop();
        log.debug("finding thesaurus terms took: {}", stopWatch.prettyPrint());

        return result;
    }
}
