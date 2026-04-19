package pt.unl.fct.iadi.novaevents

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class LoggingInterceptor : HandlerInterceptor {

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {

        val auth = SecurityContextHolder.getContext().authentication

        val user = if (auth != null && auth.isAuthenticated && auth.name != "anonymousUser")
            auth.name else "anonymous"

        println("[$user] ${request.method} ${request.requestURI} [${response.status}]")
    }
}