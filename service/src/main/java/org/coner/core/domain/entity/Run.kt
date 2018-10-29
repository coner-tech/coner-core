package org.coner.core.domain.entity

import java.math.BigDecimal
import java.time.Instant

data class Run(
        var id: String? = null,
        var event: Event? = null,
        var registration: Registration? = null,
        var sequence: Int = 0,
        var timestamp: Instant? = null,
        var rawTime: BigDecimal? = null,
        var cones: Int = 0,
        var didNotFinish: Boolean = false,
        var disqualified: Boolean = false,
        var rerun: Boolean = false,
        var competitive: Boolean = false
) : DomainEntity() {

}
