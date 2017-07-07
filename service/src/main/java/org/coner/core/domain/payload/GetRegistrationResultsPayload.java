package org.coner.core.domain.payload;

import java.util.List;

import org.coner.core.domain.entity.Registration;
import org.coner.core.domain.entity.ScoredRun;

public class GetRegistrationResultsPayload {

    private Registration registration;
    private List<ScoredRun> scoredRuns;
    private ScoredRun score;

    public Registration getRegistration() {
        return registration;
    }

    public void setRegistration(Registration registration) {
        this.registration = registration;
    }

    public List<ScoredRun> getScoredRuns() {
        return scoredRuns;
    }

    public void setScoredRuns(List<ScoredRun> scoredRuns) {
        this.scoredRuns = scoredRuns;
    }

    public ScoredRun getScore() {
        return score;
    }

    public void setScore(ScoredRun score) {
        this.score = score;
    }
}
