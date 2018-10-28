package org.coner.core.api.request;

import java.math.BigDecimal;

public class AddRawTimeToFirstRunLackingRequest {

    private BigDecimal rawTime;

    public BigDecimal getRawTime() {
        return rawTime;
    }

    public void setRawTime(BigDecimal rawTime) {
        this.rawTime = rawTime;
    }
}
