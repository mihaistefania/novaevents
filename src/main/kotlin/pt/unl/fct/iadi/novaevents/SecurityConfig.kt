package pt.unl.fct.iadi.novaevents

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
    private val jwtAuthFilter: JwtAuthFilter
) {

    @Bean
    fun apiFilterChain(http: HttpSecurity): SecurityFilterChain {

        http
            .securityMatcher("/api/**")

            .csrf { it.disable() }

            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }

            .authorizeHttpRequests {
                it.anyRequest().authenticated()
            }

            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    @Bean
    fun webFilterChain(http: HttpSecurity): SecurityFilterChain {

        http
            .securityMatcher("/**")

            .csrf { it.disable() }

            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }

            .authorizeHttpRequests {
                it
                    .requestMatchers("/login").permitAll()
                    .requestMatchers(HttpMethod.GET, "/**").permitAll()

                    .requestMatchers(HttpMethod.POST, "/clubs/*/events")
                    .hasAnyRole("EDITOR", "ADMIN")

                    .requestMatchers(HttpMethod.PUT, "/clubs/*/events/*")
                    .hasAnyRole("EDITOR", "ADMIN")

                    .requestMatchers(HttpMethod.DELETE, "/clubs/*/events/*")
                    .hasRole("ADMIN")

                    .anyRequest().authenticated()
            }

            .formLogin {
                it.loginPage("/login").permitAll()
            }

            .exceptionHandling {
                it.authenticationEntryPoint { _, response, _ ->
                    response.sendRedirect("http://localhost/login")
                }
            }

            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}