package at.szybbs.tacc.taccbackend

import at.szybbs.tacc.taccbackend.client.TaccDirections
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.UUID
import kotlin.test.Test

@SpringBootTest
class TaccDirectionsTests {
    val userId = UUID.fromString("409cd7c7-e82d-406c-a784-621598ff45e9")

    @Autowired
    lateinit var taccDirections: TaccDirections

    @Test
    fun testGetDriveTimeMinutes() {
        val from = "An Der Feichsen 43, 3251 Purgstall an der Erlauf, Austria"
        val to = "Ybbs an der Donau, Österreich"
        val result = taccDirections.getDriveTimeMinutes(from, to)
        println("Drive time from $from to $to: $result minutes")

        assert(result > 16)
    }

    @Test
    fun testGetDriveTimeWithVariables() {
        val from = "An Der Feichsen 43, 3251 Purgstall an der Erlauf, Austria"
        val to = "Ybbs an der Donau, Österreich"
        val result = taccDirections.getDriveTimeWithVariables(from, to, userId)
        println("Drive time from $from to $to: $result minutes")

        assert(result > 25)
    }
}