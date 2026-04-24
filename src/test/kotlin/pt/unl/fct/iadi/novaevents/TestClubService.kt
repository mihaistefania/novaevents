package pt.unl.fct.iadi.novaevents

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import pt.unl.fct.iadi.novaevents.model.Club
import pt.unl.fct.iadi.novaevents.repository.ClubRepository
import pt.unl.fct.iadi.novaevents.service.ClubService
import java.util.*

class TestClubService {

    private val repo: ClubRepository = mock(ClubRepository::class.java)
    private val service = ClubService(repo)

    @Test
    fun `find all clubs`() {
        `when`(repo.findAll()).thenReturn(listOf(Club(name = "C1")))

        val result = service.findAll()

        assertEquals(1, result.size)
    }

    @Test
    fun `find by id`() {
        `when`(repo.findById(1)).thenReturn(Optional.of(Club(id = 1)))

        val result = service.findById(1)

        assertEquals(1, result.id)
    }

    @Test
    fun `find by id throws`() {
        `when`(repo.findById(1)).thenReturn(Optional.empty())

        assertThrows(NoSuchElementException::class.java) {
            service.findById(1)
        }
    }
}