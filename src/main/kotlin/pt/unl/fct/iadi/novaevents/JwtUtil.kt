package pt.unl.fct.iadi.novaevents

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtUtil {

    private val key: SecretKey = Keys.hmacShaKeyFor(
        "my-super-secret-key-my-super-secret-key".toByteArray()
    )

    fun generate(username: String, roles: List<String>): String {
        return Jwts.builder()
            .subject(username)
            .claim("roles", roles)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
            .signWith(key)
            .compact()
    }

    fun parse(token: String): Claims {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload
    }
}