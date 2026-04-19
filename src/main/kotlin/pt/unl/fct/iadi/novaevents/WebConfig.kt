package pt.unl.fct.iadi.novaevents

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.HiddenHttpMethodFilter
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val loggingInterceptor: LoggingInterceptor
) : WebMvcConfigurer {

    @Bean
    fun hiddenHttpMethodFilter(): HiddenHttpMethodFilter {
        return HiddenHttpMethodFilter()
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(loggingInterceptor)
    }
}