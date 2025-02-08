package at.szybbs.tacc.taccbackend.entity

import java.time.Instant

data class ScheduleEntry (
    val date: Instant,
    val tarLocation: String?,
    val isActivateAc: Boolean,
    val isCheckAgain: Boolean,
)