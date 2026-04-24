package pt.unl.fct.iadi.novaevents
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
class TestEventControllerIntegration {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `club list loads`() {
        mockMvc.get("/clubs")
            .andExpect {
                status { isOk() }
            }
    }

    @Test
    fun `event list loads`() {
        mockMvc.get("/events")
            .andExpect {
                status { isOk() }
            }
    }
}