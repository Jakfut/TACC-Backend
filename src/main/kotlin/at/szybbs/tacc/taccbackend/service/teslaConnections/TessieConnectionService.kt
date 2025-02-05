package at.szybbs.tacc.taccbackend.service.teslaConnections

import at.szybbs.tacc.taccbackend.dto.teslaConnections.tessie.TessieConnectionCreationDto
import at.szybbs.tacc.taccbackend.dto.teslaConnections.tessie.TessieConnectionUpdateDto
import at.szybbs.tacc.taccbackend.entity.calendarConnections.CalendarType
import at.szybbs.tacc.taccbackend.exception.userInformation.UserInformationNotFoundException
import at.szybbs.tacc.taccbackend.entity.teslaConnections.TeslaConnectionType
import at.szybbs.tacc.taccbackend.entity.teslaConnections.tessie.TessieConnection
import at.szybbs.tacc.taccbackend.entity.userInformation.UserInformation
import at.szybbs.tacc.taccbackend.exception.calendarConnections.CalendarConnectionNotFoundException
import at.szybbs.tacc.taccbackend.exception.teslaConnections.TeslaConnectionAlreadyExistsException
import at.szybbs.tacc.taccbackend.exception.teslaConnections.TeslaConnectionNotFoundException
import at.szybbs.tacc.taccbackend.exception.teslaConnections.TeslaConnectionValidationException
import at.szybbs.tacc.taccbackend.repository.teslaConnections.TessieConnectionRepository
import at.szybbs.tacc.taccbackend.service.UserInformationService
import org.springframework.stereotype.Service
import java.util.*

@Service
class TessieConnectionService (
    private val tessieConnectionRepository: TessieConnectionRepository,
    private val userInformationService: UserInformationService,
) {

    @Throws(TeslaConnectionNotFoundException::class)
    fun getTeslaConnection(userInformationId: UUID): TessieConnection {
       return tessieConnectionRepository.findById(userInformationId)
           .orElseThrow { TeslaConnectionNotFoundException(TeslaConnectionType.TESSIE, userInformationId) }
    }

    @Throws(
        UserInformationNotFoundException::class,
        TeslaConnectionAlreadyExistsException::class,
        TeslaConnectionValidationException::class,
    )
    fun createTeslaConnection(
        userInformationId: UUID,
        creationDto: TessieConnectionCreationDto
    ): TessieConnection {
        if (!userInformationService.userInformationExists(userInformationId)) throw UserInformationNotFoundException(userInformationId)

        if (teslaConnectionExists(userInformationId)) throw TeslaConnectionAlreadyExistsException(TeslaConnectionType.TESSIE, userInformationId)

        val newTessieConnection = TessieConnection(
            userInformationId = userInformationId,
            vin = creationDto.vin,
            accessToken = creationDto.accessToken,
        )

        // TODO: Test connection with http-Client

        return tessieConnectionRepository.save(newTessieConnection)
    }

    @Throws(
        TeslaConnectionNotFoundException::class,
        TeslaConnectionValidationException::class,
    )
    fun updateTeslaConnectionPublicFields(
        userInformationId: UUID,
        updateDto: TessieConnectionUpdateDto
    ): TessieConnection? {
        val teslaConnection = getTeslaConnection(userInformationId)

        if (!updateDto.hasChanged(teslaConnection)) return null

        teslaConnection.vin = updateDto.vin
        teslaConnection.accessToken = updateDto.accessToken

        val updatedTeslaConnection = tessieConnectionRepository.save(teslaConnection)

        // TODO: Test connection with http-Client

        return updatedTeslaConnection
    }

    @Throws(
        TeslaConnectionNotFoundException::class,
        UserInformationNotFoundException::class,
    )
    fun deleteTeslaConnection(userInformationId: UUID) {
        val tessieConnection = getTeslaConnection(userInformationId)

        if (teslaTypeIsCurrentlyActive(userInformationId)) {
            userInformationService.setActiveTeslaConnectionTypeToNull(userInformationId)
        }

        tessieConnectionRepository.delete(tessieConnection)
    }

    @Throws(
        UserInformationNotFoundException::class,
        TeslaConnectionNotFoundException::class,
    )
    fun setTeslaConnectionToActive(userInformationId: UUID): UserInformation? {
        return userInformationService.setActiveTeslaConnectionType(userInformationId, TeslaConnectionType.TESSIE, tessieConnectionRepository)
    }

    fun teslaConnectionExists(userInformationId: UUID): Boolean {
        return tessieConnectionRepository.findById(userInformationId).isPresent
    }

    @Throws(
        CalendarConnectionNotFoundException::class
    )
    fun teslaTypeIsCurrentlyActive(userInformationId: UUID): Boolean {
        val userInformation = userInformationService.getUserInformation(userInformationId)

        return userInformation.activeTeslaConnectionType == TeslaConnectionType.TESSIE
    }
}