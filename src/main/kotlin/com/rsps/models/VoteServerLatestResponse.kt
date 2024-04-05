package com.rsps.models

import java.time.OffsetDateTime

data class VoteServerLatestResponse(
    val vote_site: String?,
    val updated_at: OffsetDateTime?,
    val username: String?,
)