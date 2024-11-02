package at.szybbs.tacc.taccbackend.service.teslaConnections

import at.szybbs.tacc.taccbackend.dto.teslaConnections.tessie.TessieConnectionCreationDto
import at.szybbs.tacc.taccbackend.dto.teslaConnections.tessie.TessieConnectionUpdateDto
import at.szybbs.tacc.taccbackend.exception.userInformation.UserInformationNotFoundException
import at.szybbs.tacc.taccbackend.entity.teslaConnections.TeslaConnectionType
import at.szybbs.tacc.taccbackend.entity.teslaConnections.tessie.TessieConnection
import at.szybbs.tacc.taccbackend.entity.userInformation.UserInformation
import at.szybbs.tacc.taccbackend.exception.teslaConnections.TeslaConnectionAlreadyExistsException
import at.szybbs.tacc.taccbackend.exception.teslaConnections.TeslaConnectionNotFoundException
import at.szybbs.tacc.taccbackend.exception.teslaConnections.TeslaConnectionValidationException
import at.szybbs.tacc.taccbackend.exception.userInformation.UserInformationTeslaConnectionAlreadyActiveException
import at.szybbs.tacc.taccbackend.exception.userInformation.UserInformationUnexpectedStateException
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
        TeslaConnectionValidationException::class,
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
        TeslaConnectionValidationException::class,
    )
    override fun updateTeslaConnectionPublicFields(
        userInformationId: UUID,
        updateDto: TessieConnectionUpdateDto
    ): TessieConnection {
        val updatedTessieConnection = getTeslaConnection(userInformationId)

        updatedTessieConnection.vin = updateDto.vin
        updatedTessieConnection.accessToken = updateDto.accessToken

        return tessieConnectionRepository.save(updatedTessieConnection)
    }

    @Throws(TeslaConnectionNotFoundException::class)
    override fun deleteTeslaConnection(userInformationId: UUID) {
        val tessieConnection = getTeslaConnection(userInformationId)

        tessieConnectionRepository.delete(tessieConnection)
    }

    @Throws(
        UserInformationNotFoundException::class,
        TeslaConnectionNotFoundException::class,
        UserInformationTeslaConnectionAlreadyActiveException::class,
    )
    override fun setTeslaConnectionToActive(userInformationId: UUID): UserInformation {
        val userInformation = userInformationService.getUserInformation(userInformationId)

        if (userInformation.activeTeslaConnectionType == TeslaConnectionType.TESSIE)
            throw UserInformationTeslaConnectionAlreadyActiveException(userInformationId, TeslaConnectionType.TESSIE)

        if (!teslaConnectionExists(userInformationId))
            throw TeslaConnectionNotFoundException(TeslaConnectionType.TESSIE, userInformationId)

        return userInformationService.setActiveTeslaConnectionType(userInformationId, TeslaConnectionType.TESSIE)
    }

    @Throws(
        UserInformationNotFoundException::class,
        TeslaConnectionNotFoundException::class,
        UserInformationUnexpectedStateException::class,
    )
    override fun setTeslaConnectionToInactive(userInformationId: UUID): UserInformation {
        val userInformation = userInformationService.getUserInformation(userInformationId)

        if (userInformation.activeTeslaConnectionType != TeslaConnectionType.TESSIE)
            throw UserInformationUnexpectedStateException("activeTeslaConnectionType", TeslaConnectionType.TESSIE.name)

        if (!teslaConnectionExists(userInformationId))
            throw TeslaConnectionNotFoundException(TeslaConnectionType.TESSIE, userInformationId)

        return userInformationService.setActiveTeslaConnectionType(userInformationId, null)
    }

    override fun teslaConnectionExists(userInformationId: UUID): Boolean {
        return tessieConnectionRepository.findById(userInformationId).isPresent
    }
}