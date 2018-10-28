package org.coner.core.api.response;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.coner.core.api.entity.ScoredRunApiEntity;

public class GetEventResultsRegistrationResponse {
    private String registrationId;
    private List<ScoredRunApiEntity> scoredRuns;
    private ScoredRunApiEntity score;

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public List<ScoredRunApiEntity> getScoredRuns() {
        return scoredRuns;
    }

    public void setScoredRuns(List<ScoredRunApiEntity> scoredRuns) {
        this.scoredRuns = scoredRuns;
    }

    public ScoredRunApiEntity getScore() {
        return score;
    }

    public void setScore(ScoredRunApiEntity score) {
        this.score = score;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }
}
