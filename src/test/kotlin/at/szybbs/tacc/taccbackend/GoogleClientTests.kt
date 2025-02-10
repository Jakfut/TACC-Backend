package at.szybbs.tacc.taccbackend

import at.szybbs.tacc.taccbackend.entity.calendarConnections.CalendarType
import at.szybbs.tacc.taccbackend.factory.CalendarConnectionFactory
import at.szybbs.tacc.taccbackend.repository.calendarConnections.GoogleCalendarConnectionRepository
import at.szybbs.tacc.taccbackend.service.UserInformationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant
import java.util.*
import kotlin.test.Test

@SpringBootTest
class GoogleClientTests {
    val userId = "e50d926b-f45d-4c0a-9345-0536c04b8162"

    @Autowired
    private lateinit var calendarConnectionFactory: CalendarConnectionFactory

    private val calendarConnectionClient by lazy { calendarConnectionFactory.createCalendarConnectionClient(
        UUID.fromString(userId)) }

    @Autowired
    lateinit var userInformationService: UserInformationService

    @Autowired
    lateinit var googleConnectionRepository: GoogleCalendarConnectionRepository

    @Test
    fun setCalendarConnectionType() {
        userInformationService.setActiveCalendarConnectionType(UUID.fromString(userId), CalendarType.GOOGLE_CALENDAR, googleConnectionRepository)
    }

    @Test
    fun getCalendarIdList() {
        val result = calendarConnectionClient.getCalendarIdList()

        println("CalendarList: $result")

        assert(result.isNotEmpty())
    }

    @Test
    fun getCalendarEvents() {
        val result = calendarConnectionClient.getEvents(
            "c77cff268c26de890ebec385cf0a28ecebadec6a39d8d526873af6cefb57d590@group.calendar.google.com",
            Instant.now().minusSeconds(60 * 60 * 24 * 365),
        )

        println("CalendarEvents: $result")

        assert(result.isNotEmpty())
    }

    @Test
    fun getCalendarEventsKeyword() {
        val result = calendarConnectionClient.getEventWithKeyword(
            "c77cff268c26de890ebec385cf0a28ecebadec6a39d8d526873af6cefb57d590@group.calendar.google.com",
            Instant.now().minusSeconds(60 * 60 * 24 * 365),
            "Test"
        )

        println("CalendarEvents: $result")

        assert(result.isNotEmpty())
    }

    @Test
    fun getAllCalendarEventsKeyword() {
        val result = calendarConnectionClient.getAllEventsWithKeywordStart(
            Instant.now().minusSeconds(60 * 60 * 24 * 365)
        )

        println("CalendarEvents: $result")

        assert(result.isNotEmpty())
    }
}