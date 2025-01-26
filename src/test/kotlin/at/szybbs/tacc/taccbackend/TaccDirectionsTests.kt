package at.szybbs.tacc.taccbackend

import at.szybbs.tacc.taccbackend.client.TaccDirections
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.UUID
import kotlin.test.Test

@SpringBootTest
class TaccDirectionsTests {
    val userId = UUID.fromString("c8e2ceb5-f691-4952-a8d6-2a7e796cfbb8")

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

    @Test
    fun testGetDriveTimeFromCurrentLocation() {
        val to = "Ybbs an der Donau, Österreich"
        val result = taccDirections.getDriveTimeFromCurrentLocationWithVariables(to, userId)
        println("Drive time from current location to $to: $result minutes")

        assert(result > 1)
    }
}