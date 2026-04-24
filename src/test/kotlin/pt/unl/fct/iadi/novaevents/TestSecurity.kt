package pt.unl.fct.iadi.novaevents

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
class TestSecurity {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `api requires authentication`() {
        mockMvc.get("/api/weather?location=Lisbon")
            .andExpect {
                status { is3xxRedirection() }
            }
    }
}