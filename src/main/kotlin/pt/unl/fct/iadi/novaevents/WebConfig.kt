package pt.unl.fct.iadi.novaevents

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.HiddenHttpMethodFilter

@Configuration
class WebConfig {

    @Bean
    fun hiddenHttpMethodFilter(): HiddenHttpMethodFilter {
        return HiddenHttpMethodFilter()
    }
}