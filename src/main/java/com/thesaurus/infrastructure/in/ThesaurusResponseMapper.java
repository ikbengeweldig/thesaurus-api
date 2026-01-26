package com.thesaurus.infrastructure.in;

import com.thesaurus.api.model.ThesaurusResponse;
import com.thesaurus.domain.core.ThesaurusTerm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public abstract class ThesaurusResponseMapper {

    @Mapping(target = "type", source = "termType")
    abstract ThesaurusResponse map(ThesaurusTerm thesaurusTerm);
}
