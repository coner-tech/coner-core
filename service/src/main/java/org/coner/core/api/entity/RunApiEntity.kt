package org.coner.core.api.entity

import java.math.BigDecimal
import java.time.Instant

data class RunApiEntity(
        var id: String? = null,
        var eventId: String? = null,
        var registrationId: String? = null,
        var sequence: Int = 0,
        var timestamp: Instant? = null,
        var rawTime: BigDecimal? = null,
        var cones: Int = 0,
        var didNotFinish: Boolean = false,
        var disqualified: Boolean = false,
        var rerun: Boolean = false,
        var competitive: Boolean = false
) : ApiEntity()