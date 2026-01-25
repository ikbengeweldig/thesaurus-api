package com.thesaurus.usecase;

import com.thesaurus.domain.core.TermType;
import com.thesaurus.domain.core.ThesaurusTerm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.thesaurus.util.TextNormalizerUtil.normalize;
import static java.util.Objects.nonNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("it")
class TermRetrieverApplicationIT {

    @Autowired
    private TermRetrieverApplication termRetrieverApplication;

    @Value("classpath:input/sampledoc.txt")
    private Resource sampleDoc;

    private static void assertContainsTerm(List<ThesaurusTerm> actual, String expectedTerm, TermType expectedType) {

        String expectedNorm = normalize(expectedTerm);

        boolean found = actual.stream()
                              .anyMatch(arg -> nonNull(arg) && expectedNorm.equals(normalize(arg.getTerm())) && expectedType == arg.getTermType());

        assertTrue(found,
                   () -> "Expected to find term '%s' with type %s but it was not present. Found: %s".formatted(expectedNorm,
                                                                                                               expectedType,
                                                                                                               actual.stream()
                                                                                                                     .filter(arg -> nonNull(arg))
                                                                                                                     .limit(25)
                                                                                                                     .map(arg -> normalize(arg.getTerm()) + ":" + arg.getTermType())
                                                                                                                     .toList()));
    }

    @Test
    void retrieveTerms_happy_path() {

        List<ThesaurusTerm> terms = termRetrieverApplication.retrieveTerms(sampleDoc);

        assertNotNull(terms, "retrieveTerms should never return null");

        assertFalse(terms.isEmpty(), "Expected at least some recognised thesaurus terms");

        assertContainsTerm(terms, "VPRO", TermType.ORGANISATION);
        assertContainsTerm(terms, "Amsterdam", TermType.LOCATION);
        assertContainsTerm(terms, "Nederland", TermType.LOCATION);
        assertContainsTerm(terms, "Hadimassa", TermType.ORGANISATION);
        assertContainsTerm(terms, "Simplisties Verbond", TermType.ORGANISATION);
        assertContainsTerm(terms, "televisie", TermType.SUBJECT);
    }
}
