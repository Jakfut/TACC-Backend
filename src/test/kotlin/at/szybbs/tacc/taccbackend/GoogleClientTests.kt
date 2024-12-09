package at.szybbs.tacc.taccbackend

import at.szybbs.tacc.taccbackend.factory.CalendarConnectionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*
import kotlin.test.Test

@SpringBootTest
class GoogleClientTests {
    val userId = "409cd7c7-e82d-406c-a784-621598ff45e9"

    @Autowired
    private lateinit var calendarConnectionFactory: CalendarConnectionFactory

    private val calendarConnectionClient by lazy { calendarConnectionFactory.createCalendarConnectionClient(
        UUID.fromString(userId)) }

    @Test
    fun getCalendarList() {
        val result = calendarConnectionClient.getCalendarList()

        println("CalendarList: $result")

        assert(result.isNotEmpty())
    }

    @Test
    fun getCalendarEvents() {
        val result = calendarConnectionClient.getEvents("c77cff268c26de890ebec385cf0a28ecebadec6a39d8d526873af6cefb57d590@group.calendar.google.com")

        println("CalendarEvents: $result")

        assert(result.isNotEmpty())
    }

    @Test
    fun getCalendarEventsKeyword() {
        val result = calendarConnectionClient.getEventWithKeyword(
            "c77cff268c26de890ebec385cf0a28ecebadec6a39d8d526873af6cefb57d590@group.calendar.google.com",
            "TestWord"
        )

        println("CalendarEvents: $result")

        assert(result.isNotEmpty())
    }

}