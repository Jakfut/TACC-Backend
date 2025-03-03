package at.szybbs.tacc.taccbackend.controller

import at.szybbs.tacc.taccbackend.client.TaccDirections
import at.szybbs.tacc.taccbackend.dto.userInformation.UserInformationCreationDto
import at.szybbs.tacc.taccbackend.dto.userInformation.UserInformationResponseDto
import at.szybbs.tacc.taccbackend.dto.userInformation.UserInformationUpdateDefaultValuesDto
import at.szybbs.tacc.taccbackend.entity.ScheduleEntry
import at.szybbs.tacc.taccbackend.factory.CalendarConnectionFactory
import at.szybbs.tacc.taccbackend.job.RefreshSchedules
import at.szybbs.tacc.taccbackend.service.SchedulerService
import at.szybbs.tacc.taccbackend.service.UserInformationService
import org.quartz.JobKey
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/user/{user-information-id}")
class UserInformationController(
    private val userInformationService: UserInformationService,
    private val schedulerService: SchedulerService,
    private val calendarConnectionFactory: CalendarConnectionFactory,
    private val taccDirections: TaccDirections
) {

    @PreAuthorize("@userSecurity.idEqualsAuthenticationId(#userInformationId)")
    @GetMapping
    fun getUserInformation(
        @PathVariable("user-information-id") userInformationId: UUID
    ) : ResponseEntity<UserInformationResponseDto> {
        val responseDto = userInformationService.getUserInformation(userInformationId)
            .toResponseDto()

        return ResponseEntity.ok(responseDto)
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_' + @environment.getProperty('security.authentication.roles.create-user'))")
    fun createUserInformation(
        @PathVariable("user-information-id") userInformationId: UUID,
        @RequestBody creationDto: UserInformationCreationDto
    ) : ResponseEntity<UserInformationResponseDto> {
        val responseDto = userInformationService.createUserInformation(
            userInformationId,
            creationDto
        ).toResponseDto()

        return ResponseEntity.ok(responseDto)
    }

    @DeleteMapping
    @PreAuthorize("@userSecurity.idEqualsAuthenticationId(#userInformationId)")
    fun deleteUserInformation(
        @PathVariable("user-information-id") userInformationId: UUID
    ) : ResponseEntity<Void> {
        userInformationService.deleteUserInformation(userInformationId)

        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/default-values")
    @PreAuthorize("@userSecurity.idEqualsAuthenticationId(#userInformationId)")
    fun updateUserInformationDefaultValues(
        @PathVariable("user-information-id") userInformationId: UUID,
        @RequestBody updateDto: UserInformationUpdateDefaultValuesDto
    ) : ResponseEntity<UserInformationResponseDto> {
        val responseDto = userInformationService.updateUserInformationDefaultValues(userInformationId, updateDto)
            ?.toResponseDto() ?: return ResponseEntity.noContent().build()

        return ResponseEntity.ok(responseDto)
    }

    @PatchMapping("calendar-connections/deactivate")
    @PreAuthorize("@userSecurity.idEqualsAuthenticationId(#userInformationId)")
    fun setActiveCalendarConnectionToNull(
        @PathVariable("user-information-id") userInformationId: UUID
    ) : ResponseEntity<UserInformationResponseDto> {
        val responseDto = userInformationService.setActiveCalendarConnectionTypeToNull(userInformationId)
            ?.toResponseDto() ?: return ResponseEntity.noContent().build()

        return ResponseEntity.ok(responseDto)
    }

    @PatchMapping("tesla-connections/deactivate")
    @PreAuthorize("@userSecurity.idEqualsAuthenticationId(#userInformationId)")
    fun setActiveTeslaConnectionToNull(
        @PathVariable("user-information-id") userInformationId: UUID
    ) : ResponseEntity<UserInformationResponseDto> {
        val responseDto = userInformationService.setActiveTeslaConnectionTypeToNull(userInformationId)
            ?.toResponseDto() ?: return ResponseEntity.noContent().build()

        return ResponseEntity.ok(responseDto)
    }

    @GetMapping("/scheduled-entries")
    @PreAuthorize("@userSecurity.idEqualsAuthenticationId(#userInformationId)")
    fun getScheduleEntries(
        @PathVariable("user-information-id") userInformationId: UUID
    ): ResponseEntity<List<ScheduleEntry>> {
        val scheduledEntries = schedulerService.getScheduleEntries(userInformationId)

        return ResponseEntity.ok(scheduledEntries)
    }

    @GetMapping("refresh-schedules")
    @PreAuthorize("@userSecurity.idEqualsAuthenticationId(#userInformationId)")
    fun refreshSchedulesEndpoint(
        @PathVariable("user-information-id") userInformationId: UUID
    ) : ResponseEntity<String> {
        RefreshSchedules(calendarConnectionFactory, schedulerService, userInformationService, taccDirections)
            .refreshUserSchedules(userInformationService.getUserInformation(userInformationId))

        return ResponseEntity.ok("Schedules refreshed")
    }

    @GetMapping("/{job-key}/unschedule-job")
    @PreAuthorize("@userSecurity.idEqualsAuthenticationId(#userInformationId)")
    fun unscheduleJob(
        @PathVariable("user-information-id") userInformationId: UUID,
        @PathVariable("user-information-id") jobKey: String
    ) : ResponseEntity<String> {
        schedulerService.unscheduleJob(JobKey(jobKey))

        return ResponseEntity.ok("Job unscheduled")
    }
}