package pt.unl.fct.iadi.novaevents.controller

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import pt.unl.fct.iadi.novaevents.JwtUtil
import pt.unl.fct.iadi.novaevents.service.CustomUserDetailsService

@Controller
class AuthController(
    private val jwtUtil: JwtUtil,
    private val userDetailsService: CustomUserDetailsService,
    private val passwordEncoder: PasswordEncoder
) {

    @GetMapping("/login")
    fun loginPage(): String = "login"

    @PostMapping("/login")
    fun login(
        @RequestParam username: String,
        @RequestParam password: String,
        response: HttpServletResponse
    ): String {

        val user = userDetailsService.loadUserByUsername(username)

        if (!passwordEncoder.matches(password, user.password)) {
            return "redirect:/login?error"
        }

        val roles = user.authorities.map { it.authority }

        val token = jwtUtil.generate(username, roles)

        val cookie = Cookie("jwt", token)
        cookie.isHttpOnly = true
        cookie.path = "/"
        cookie.maxAge = 60 * 60 * 10

        response.addCookie(cookie)

        return "redirect:/"
    }

    @PostMapping("/logout")
    fun logout(response: HttpServletResponse): String {

        val cookie = Cookie("jwt", "")
        cookie.path = "/"
        cookie.maxAge = 0

        response.addCookie(cookie)

        return "redirect:/login"
    }
}