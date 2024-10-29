package at.szybbs.tacc.taccbackend.controller.calendarConnection

import at.szybbs.tacc.taccbackend.dto.calendarConnection.CalendarConnectionCreationDto
import at.szybbs.tacc.taccbackend.dto.calendarConnection.CalendarConnectionResponseDto
import at.szybbs.tacc.taccbackend.dto.calendarConnection.toResponseDto
import at.szybbs.tacc.taccbackend.model.calendarConnection.CalendarConnectionId
import at.szybbs.tacc.taccbackend.model.calendarConnection.CalendarType
import at.szybbs.tacc.taccbackend.service.calendarConnection.CalendarConnectionService
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/user/{userInformationId}/calendar-connection")
class CalendarConnectionController (
    private val calendarConnectionService: CalendarConnectionService
) {
    @GetMapping
    fun getAllCalendarConnections(
        @PathVariable userInformationId: UUID
    ) : ResponseEntity<List<CalendarConnectionResponseDto>> {
        val calendarConnectionResponseDtos = calendarConnectionService.getAllCalendarConnections(userInformationId)
            .toResponseDto()

        return ResponseEntity.ok(calendarConnectionResponseDtos)
    }

    @GetMapping("/{calendarConnectionType}")
    fun getCalendarConnectionByType(
        @PathVariable userInformationId: UUID,
        @PathVariable calendarConnectionType: CalendarType
    ) : ResponseEntity<CalendarConnectionResponseDto> {
        val calendarConnectionResponseDto = calendarConnectionService.getCalendarConnectionByType(
            CalendarConnectionId(
                type = calendarConnectionType,
                userInformationId = userInformationId
            )
        ).toResponseDto()

        return ResponseEntity.ok(calendarConnectionResponseDto)
    }

    @GetMapping("/active")
    fun getActiveCalendarConnection(
        @PathVariable userInformationId: UUID
    ) : ResponseEntity<CalendarConnectionResponseDto> {
        val calendarConnectionResponseDto = calendarConnectionService.getActiveCalendarConnection(userInformationId)
            .toResponseDto()

        return ResponseEntity.ok(calendarConnectionResponseDto)
    }

    @PostMapping("/{calendarConnectionType}")
    fun createCalendarConnectionByType(
        @PathVariable userInformationId: UUID,
        @PathVariable calendarConnectionType: CalendarType,
        @RequestBody calendarConnectionCreationDto: CalendarConnectionCreationDto
    ) : ResponseEntity<CalendarConnectionResponseDto> {
        val calendarConnectionResponseDto = calendarConnectionService.createCalendarConnection(
            calendarConnectionId = CalendarConnectionId(
                type = calendarConnectionType,
                userInformationId = userInformationId
            ),
            calendarConnectionCreationDto = calendarConnectionCreationDto
        ).toResponseDto()

        return ResponseEntity.ok(calendarConnectionResponseDto)
    }

    @PatchMapping("/{calendarConnectionType}/active")
    fun setCalendarConnectionToActive(
        @PathVariable userInformationId: UUID,
        @PathVariable calendarConnectionType: CalendarType
    ) : ResponseEntity<CalendarConnectionResponseDto> {
        val calendarConnectionResponseDto = calendarConnectionService.setCalendarConnectionToActive(
            CalendarConnectionId(
                type = calendarConnectionType,
                userInformationId = userInformationId
            )
        ).toResponseDto()

        return ResponseEntity.ok(calendarConnectionResponseDto)
    }

    @PatchMapping("/{calendarConnectionType}/inactive")
    fun setCalendarConnectionToInactive(
        @PathVariable userInformationId: UUID,
        @PathVariable calendarConnectionType: CalendarType
    ) : ResponseEntity<CalendarConnectionResponseDto> {
        val calendarConnectionResponseDto = calendarConnectionService.setCalendarConnectionToInactive(
            CalendarConnectionId(
                type = calendarConnectionType,
                userInformationId = userInformationId
            )
        ).toResponseDto()

        return ResponseEntity.ok(calendarConnectionResponseDto)
    }

    @PatchMapping("/{calendarConnectionType}/config")
    fun updateCalendarConnectionConfig(
        @PathVariable userInformationId: UUID,
        @PathVariable calendarConnectionType: CalendarType,
        @RequestBody config: JsonNode
    ) : ResponseEntity<CalendarConnectionResponseDto> {
        val calendarConnectionResponseDto = calendarConnectionService.updateCalendarConnectionConfig(
            calendarConnectionId = CalendarConnectionId(
                type = calendarConnectionType,
                userInformationId = userInformationId
            ),
            config = config,
            fromUserInput = true
        ).toResponseDto()

        return ResponseEntity.ok(calendarConnectionResponseDto)
    }

    @DeleteMapping("/{calendarConnectionType}")
    fun deleteCalendarConnection(
        @PathVariable userInformationId: UUID,
        @PathVariable calendarConnectionType: CalendarType
    ) : ResponseEntity<Void> {
        calendarConnectionService.deleteCalendarConnection(CalendarConnectionId(
            type = calendarConnectionType,
            userInformationId = userInformationId
        ))

        return ResponseEntity.noContent().build()
    }
}