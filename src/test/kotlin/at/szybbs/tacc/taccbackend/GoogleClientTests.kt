package at.szybbs.tacc.taccbackend

import at.szybbs.tacc.taccbackend.factory.CalendarConnectionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant
import java.util.*
import kotlin.test.Test

@SpringBootTest
class GoogleClientTests {
    val userId = "c8e2ceb5-f691-4952-a8d6-2a7e796cfbb8"

    @Autowired
    private lateinit var calendarConnectionFactory: CalendarConnectionFactory

    private val calendarConnectionClient by lazy { calendarConnectionFactory.createCalendarConnectionClient(
        UUID.fromString(userId)) }

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
            Instant.now().minusSeconds(60 * 60 * 24 * 365)
        )

        println("CalendarEvents: $result")

        assert(result.isNotEmpty())
    }

    @Test
    fun getAllCalendarEventsKeyword() {
        val result = calendarConnectionClient.getAllEventsWithKeyword(
            Instant.now().minusSeconds(60 * 60 * 24 * 365)
        )

        println("CalendarEvents: $result")

        assert(result.isNotEmpty())
    }
}