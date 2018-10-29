package org.coner.core.api.request

import java.math.BigDecimal
import java.time.Instant

data class AddRunRequest(
        var registrationId: String? = null,
        var timestamp: Instant? = null,
        var rawTime: BigDecimal? = null,
        var cones: Int = 0,
        var isDidNotFinish: Boolean = false,
        var isDisqualified: Boolean = false,
        var isRerun: Boolean = false,
        var isCompetitive: Boolean = false
)
