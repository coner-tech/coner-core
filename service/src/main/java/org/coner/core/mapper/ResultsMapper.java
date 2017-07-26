package org.coner.core.mapper;

import org.coner.core.api.entity.ScoredRunApiEntity;
import org.coner.core.api.response.GetEventResultsRegistrationResponse;
import org.coner.core.domain.entity.ScoredRun;
import org.coner.core.domain.payload.GetRegistrationResultsPayload;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(
        config = ConerBaseMapStructConfig.class
)
public interface ResultsMapper {


    @Mappings({
            @Mapping(source = "registration.id", target = "registrationId")
    })
    GetEventResultsRegistrationResponse toApiResponse(GetRegistrationResultsPayload payload);

    @Mappings({
            @Mapping(source = "run.id", target = "runId")
    })
    ScoredRunApiEntity toApiEntity(ScoredRun scoredRun);
}
