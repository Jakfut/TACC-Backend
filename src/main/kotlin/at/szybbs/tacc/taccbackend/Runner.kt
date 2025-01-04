package at.szybbs.tacc.taccbackend

import at.szybbs.tacc.taccbackend.entity.calendarConnections.googleCalendar.GoogleCalendarConnection
import at.szybbs.tacc.taccbackend.entity.teslaConnections.tessie.TessieConnection
import at.szybbs.tacc.taccbackend.repository.calendarConnections.GoogleCalendarConnectionRepository
import at.szybbs.tacc.taccbackend.repository.teslaConnections.TessieConnectionRepository
import at.szybbs.tacc.taccbackend.repository.UserInformationRepository
import at.szybbs.tacc.taccbackend.entity.userInformation.UserInformation
import org.keycloak.OAuth2Constants
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.util.*

@Component
class Runner(
    private val tessieConnectionRepository: TessieConnectionRepository,
    private val userInformationRepository: UserInformationRepository,
    private val googleCalendarConnectionRepository: GoogleCalendarConnectionRepository,
) : CommandLineRunner {

    override fun run(vararg args: String?) {

        println("RUN SEED DATA")

        var userInformationInfo = UserInformation(
            id = UUID.fromString("e569839d-0a4e-42e6-a065-e5c83157f71e"),
            email = "sommer2006@gmail.com",
            noDestMinutes = 10,
            ccRuntimeMinutes = 20,
            arrivalBufferMinutes = 30,
        )

        userInformationRepository.save(userInformationInfo)

        userInformationInfo = UserInformation(
            id = UUID.fromString("a569839d-0a4e-42e6-a065-e5c83157f71e"),
            email = "christoph.sommer2006@gmail.com",
            noDestMinutes = 10,
            ccRuntimeMinutes = 20,
            arrivalBufferMinutes = 30,
        )

        userInformationRepository.save(userInformationInfo)

        var teslaConnection = TessieConnection(
            userInformationId = userInformationInfo.id,
            vin = "vin",
            accessToken = "access_token",
        )

        tessieConnectionRepository.save(teslaConnection)

        val calendarConnection = GoogleCalendarConnection(
            userInformationId = userInformationInfo.id,
            keyword = "#key",
            accessToken = "aaaaaa",
            userInformation = null,
            refreshToken = "asd",
            email = "email",
        )

        googleCalendarConnectionRepository.save(calendarConnection)


        println(userInformationInfo.id.toString())
    }

}