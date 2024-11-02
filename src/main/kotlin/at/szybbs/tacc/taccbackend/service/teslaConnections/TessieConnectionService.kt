package at.szybbs.tacc.taccbackend.service.teslaConnections

import at.szybbs.tacc.taccbackend.dto.teslaConnections.tessie.TessieConnectionCreationDto
import at.szybbs.tacc.taccbackend.dto.teslaConnections.tessie.TessieConnectionUpdateDto
import at.szybbs.tacc.taccbackend.exception.userInformation.UserInformationNotFoundException
import at.szybbs.tacc.taccbackend.entity.teslaConnections.TeslaConnectionType
import at.szybbs.tacc.taccbackend.entity.teslaConnections.tessie.TessieConnection
import at.szybbs.tacc.taccbackend.exception.calendarConnections.CalendarConnectionValidationException
import at.szybbs.tacc.taccbackend.exception.teslaConnections.TeslaConnectionAlreadyExistsException
import at.szybbs.tacc.taccbackend.exception.teslaConnections.TeslaConnectionNotFoundException
import at.szybbs.tacc.taccbackend.repository.teslaConnections.TessieConnectionRepository
import at.szybbs.tacc.taccbackend.service.userInformation.UserInformationService
import org.springframework.stereotype.Service
import java.util.*

@Service
class TessieConnectionService (
    private val tessieConnectionRepository: TessieConnectionRepository,
    private val userInformationService: UserInformationService,
) : TeslaConnectionService<
        TessieConnection,
        TessieConnectionCreationDto,
        TessieConnectionUpdateDto> {

    @Throws(TeslaConnectionNotFoundException::class)
    override fun getTeslaConnection(userInformationId: UUID): TessieConnection {
       return tessieConnectionRepository.findById(userInformationId)
           .orElseThrow { TeslaConnectionNotFoundException(TeslaConnectionType.TESSIE, userInformationId) }
    }

    @Throws(
        UserInformationNotFoundException::class,
        TeslaConnectionAlreadyExistsException::class,
        CalendarConnectionValidationException::class,
    )
    override fun createTeslaConnection(
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

        return tessieConnectionRepository.save(newTessieConnection)
    }

    @Throws(
        TeslaConnectionNotFoundException::class,
        CalendarConnectionValidationException::class,
    )
    override fun updateTeslaConnection(
        userInformationId: UUID,
        updateDto: TessieConnectionUpdateDto
    ): TessieConnection {
        val updatedTessieConnectionOpt = tessieConnectionRepository.findById(userInformationId)

        if (updatedTessieConnectionOpt.isEmpty) throw TeslaConnectionNotFoundException(TeslaConnectionType.TESSIE, userInformationId)

        val updatedTessieConnection = updatedTessieConnectionOpt.get()

        updatedTessieConnection.vin = updateDto.vin
        updatedTessieConnection.accessToken = updateDto.accessToken

        return tessieConnectionRepository.save(updatedTessieConnection)
    }

    @Throws(TeslaConnectionNotFoundException::class)
    override fun deleteTeslaConnection(userInformationId: UUID) {
        val tessieConnection = getTeslaConnection(userInformationId)

        tessieConnectionRepository.delete(tessieConnection)
    }

    override fun teslaConnectionExists(userInformationId: UUID): Boolean {
        return tessieConnectionRepository.findById(userInformationId).isPresent
    }
}