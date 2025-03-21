package at.szybbs.tacc.taccbackend.entity

import java.time.Instant

data class ScheduleEntry (
    val jobKey: String,
    val nextTrigger: Instant,
    val eventTime: Instant,
    val tarLocation: String,
    val isActivateAc: Boolean,
    val isCheckAgain: Boolean,
)