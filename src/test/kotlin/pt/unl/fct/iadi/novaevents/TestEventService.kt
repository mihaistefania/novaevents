package pt.unl.fct.iadi.novaevents

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import pt.unl.fct.iadi.novaevents.model.*
import pt.unl.fct.iadi.novaevents.repository.EventRepository
import pt.unl.fct.iadi.novaevents.service.EventService
import java.util.*

class TestEventService {

    private val repo: EventRepository = mock(EventRepository::class.java)
    private val service = EventService(repo)

    private val club = Club(id = 1, name = "Test Club")
    private val type = EventType(id = 1, name = "WORKSHOP")

    @Test
    fun `create event success`() {
        val event = Event(name = "Test", club = club, type = type)

        `when`(repo.existsByNameIgnoreCaseAndClub_Id("Test", 1)).thenReturn(false)
        `when`(repo.save(event)).thenReturn(event)

        val result = service.create(event)

        assertEquals("Test", result.name)
    }

    @Test
    fun `create duplicate event throws`() {
        val event = Event(name = "Test", club = club, type = type)

        `when`(repo.existsByNameIgnoreCaseAndClub_Id("Test", 1)).thenReturn(true)

        assertThrows(IllegalArgumentException::class.java) {
            service.create(event)
        }
    }

    @Test
    fun `update event success`() {
        val existing = Event(id = 1, name = "Old", club = club, type = type)

        `when`(repo.findById(1)).thenReturn(Optional.of(existing))
        `when`(repo.existsByNameIgnoreCaseAndIdNotAndClub_Id("New", 1, 1)).thenReturn(false)
        `when`(repo.save(any())).thenReturn(existing)

        val updated = Event(id = 1, name = "New", club = club, type = type)

        val result = service.update(1, updated)

        assertEquals("New", result.name)
    }

    @Test
    fun `filter by club`() {
        `when`(repo.findByClub_Id(1)).thenReturn(listOf(
            Event(name = "E1", club = club, type = type)
        ))

        val result = service.filter(null, 1, null, null)

        assertEquals(1, result.size)
    }
}