package at.szybbs.tacc.taccbackend.controller

import at.szybbs.tacc.taccbackend.entity.userInformation.UserInformation
import at.szybbs.tacc.taccbackend.repository.UserInformationRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class TestController (
    private val userInformationRepository: UserInformationRepository
) {
    @GetMapping("/users")
    fun getUsers(): List<UserInformation> {
        return userInformationRepository.findAll()
    }
}