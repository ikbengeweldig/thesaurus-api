package com.thesaurus.domain.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.thesaurus.util.TextNormalizerUtil.tokenise;
import static java.util.Objects.nonNull;

@Getter
@RequiredArgsConstructor
public class Trie {

    private final TrieNode root = new ThesaurusTerm();

    void insert(TrieNode trieNode) {

        TrieNode node = root;
        for (String token : tokenise(trieNode.getTerm())) {
            node = node.getChildren()
                       .computeIfAbsent(token, k -> new ThesaurusTerm());
        }
        node.setTermType(trieNode.getTermType());
        node.setTerm(trieNode.getTerm());
    }

    public List<ThesaurusTerm> extract(List<Word> words) {

        List<ThesaurusTerm> results = new ArrayList<>();
        if (CollectionUtils.isEmpty(words)) {
            return results;
        }

        for (int i = 0; i < words.size(); i++) {
            TrieNode node = root;

            ThesaurusTerm best = null;
            int bestEnd = -1;

            for (int j = i; j < words.size(); j++) {
                String word = words.get(j)
                                   .word();
                ThesaurusTerm next = node.getChildren()
                                         .get(word);
                if (Objects.isNull(next)) {
                    break;
                }

                node = next;
                if (nonNull(node.getTermType()) && nonNull(node.getTerm())) {
                    best = new ThesaurusTerm(node.getTerm(), node.getTermType());
                    bestEnd = j;
                }
            }

            if (nonNull(best)) {
                results.add(best);
                i = bestEnd;
            }
        }

        return results;
    }
}
