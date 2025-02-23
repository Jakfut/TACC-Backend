package at.szybbs.tacc.taccbackend.job

import at.szybbs.tacc.taccbackend.factory.TeslaConnectionFactory
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class AcJob : Job {
    @Autowired
    private lateinit var teslaConnectionFactory: TeslaConnectionFactory

    override fun execute(context: JobExecutionContext) {
        val userId = context.jobDetail.jobDataMap.getString("userId")?.let { UUID.fromString(it) } ?: return
        val targetState = context.jobDetail.jobDataMap.getBoolean("targetState")

        val teslaConnectionClient = teslaConnectionFactory.createTeslaConnectionClient(userId)

        if (!targetState && teslaConnectionClient.isUserPresent()) {
            return
        }

        teslaConnectionClient.changeAcState(targetState)
    }
}