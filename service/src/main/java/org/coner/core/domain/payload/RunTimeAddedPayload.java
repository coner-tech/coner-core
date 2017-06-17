package org.coner.core.domain.payload;

import org.coner.core.domain.entity.Run;

public class RunTimeAddedPayload {

    private Run run;
    private Outcome outcome;

    public Run getRun() {
        return run;
    }

    public void setRun(Run run) {
        this.run = run;
    }

    public Outcome getOutcome() {
        return outcome;
    }

    public void setOutcome(Outcome outcome) {
        this.outcome = outcome;
    }

    public enum Outcome {
        RUN_RAWTIME_ASSIGNED_TO_EXISTING,
        RUN_ADDED_WITH_RAWTIME
    }
}
