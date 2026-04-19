package pt.unl.fct.iadi.novaevents.service
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import pt.unl.fct.iadi.novaevents.repository.UserRepository

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {

        val user = userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User not found: $username")

        val authorities: List<GrantedAuthority> = user.roles.map {
            SimpleGrantedAuthority(it.name)
        }

        return org.springframework.security.core.userdetails.User(
            user.username,
            user.password,
            authorities
        )
    }
}