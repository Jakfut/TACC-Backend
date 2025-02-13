package at.szybbs.tacc.taccbackend.controller

import at.szybbs.tacc.taccbackend.factory.CalendarConnectionFactory
import at.szybbs.tacc.taccbackend.job.RefreshSchedules
import at.szybbs.tacc.taccbackend.service.SchedulerService
import at.szybbs.tacc.taccbackend.service.UserInformationService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.UUID


@RestController
class OAuth2GrantController(
    private val userInformationService: UserInformationService,
    private val calendarConnectionFactory: CalendarConnectionFactory,
    private val schedulerService: SchedulerService,
)
{
    @GetMapping("/auth/google/start/{user-information-id}")
    @PreAuthorize("@userSecurity.idEqualsAuthenticationId(#userInformationId)")
    fun handleGoogleRedirect(
        @PathVariable("user-information-id") userInformationId: UUID
    ): String {
        val generatedSession = UUID.randomUUID().toString()

        userInformationService.setOauth2Session(userInformationId, generatedSession)

        return "https://tacc.jakfut.at/oauth2/authorization/google?session_id=$generatedSession"
    }

    @GetMapping("/test/refresh-schedules")
    fun refreshSchedulesEndpoint() : ResponseEntity<String> {
        RefreshSchedules(calendarConnectionFactory, schedulerService, userInformationService).refreshSchedules()

        return ResponseEntity.ok("Schedules refreshed")
    }
}
