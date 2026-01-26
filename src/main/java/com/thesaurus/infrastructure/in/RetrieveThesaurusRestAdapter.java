package com.thesaurus.infrastructure.in;

import com.thesaurus.api.model.ThesaurusResponse;
import com.thesaurus.api.rest.RetrieveThesaurusApi;
import com.thesaurus.usecase.TermRetrieverApplication;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RetrieveThesaurusRestAdapter implements RetrieveThesaurusApi {

    private final ThesaurusResponseMapper thesaurusResponseMapper;
    private final TermRetrieverApplication termRetrieverApplication;

    @Value("classpath:input/sampledoc.txt")
    private Resource sampleDoc;

    @Value("classpath:input/stopwords.txt")
    private Resource stopWordsFile;

    @Value("classpath:input/gtaa-terms.csv")
    private Resource thesaurusTermsFile;

    @Override
    public ResponseEntity<List<ThesaurusResponse>> retrieve() {

        List<ThesaurusResponse> result = termRetrieverApplication.retrieveTerms(sampleDoc, thesaurusTermsFile, stopWordsFile)
                                                                 .stream()
                                                                 .map(thesaurusResponseMapper::map)
                                                                 .toList();
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<List<ThesaurusResponse>> uploadAndRetrieve(MultipartFile thesaurusTerms, MultipartFile stopWords, MultipartFile document) {

        List<ThesaurusResponse> result = termRetrieverApplication.retrieveTerms(document.getResource(),
                                                                                thesaurusTerms.getResource(),
                                                                                stopWords.getResource())
                                                                 .stream()
                                                                 .map(thesaurusResponseMapper::map)
                                                                 .toList();
        return ResponseEntity.ok(result);
    }
}
