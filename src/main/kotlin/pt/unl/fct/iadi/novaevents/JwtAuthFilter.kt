package pt.unl.fct.iadi.novaevents

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import pt.unl.fct.iadi.novaevents.service.CustomUserDetailsService

@Component
class JwtAuthFilter(
    private val jwtUtil: JwtUtil,
    private val userDetailsService: CustomUserDetailsService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val cookie = request.cookies?.find { it.name == "jwt" }

        if (cookie != null) {
            try {
                val claims = jwtUtil.parse(cookie.value)
                val username = claims.subject

                val userDetails = userDetailsService.loadUserByUsername(username)

                val auth = UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.authorities
                )

                SecurityContextHolder.getContext().authentication = auth

            } catch (e: Exception) {
            }
        }

        filterChain.doFilter(request, response)
    }
}