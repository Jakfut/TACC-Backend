package at.szybbs.tacc.taccbackend

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant

@SpringBootTest
class TaccBackendApplicationTests {

	@Test
	fun test(){
		println(Instant.now())
	}

}
