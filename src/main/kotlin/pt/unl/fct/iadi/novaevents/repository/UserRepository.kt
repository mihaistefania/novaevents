package pt.unl.fct.iadi.novaevents.repository

import org.springframework.data.jpa.repository.JpaRepository
import pt.unl.fct.iadi.novaevents.model.User

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
}