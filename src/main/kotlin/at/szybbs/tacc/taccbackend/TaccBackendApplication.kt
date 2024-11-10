package at.szybbs.tacc.taccbackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TaccBackendApplication

fun main(args: Array<String>) {
	runApplication<TaccBackendApplication>(*args)
}
