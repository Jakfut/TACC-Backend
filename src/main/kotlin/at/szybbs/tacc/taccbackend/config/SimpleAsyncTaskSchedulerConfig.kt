package at.szybbs.tacc.taccbackend.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.SimpleAsyncTaskScheduler

@Configuration
class SimpleAsyncTaskSchedulerConfig {
    @Bean
    fun simpleAsyncTaskScheduler(): SimpleAsyncTaskScheduler {
        return SimpleAsyncTaskScheduler()
    }
}