package pt.unl.fct.iadi.novaevents.repository

import org.springframework.data.jpa.repository.JpaRepository
import pt.unl.fct.iadi.novaevents.model.Event

interface EventRepository : JpaRepository<Event, Long> {

    fun existsByNameIgnoreCase(name: String): Boolean

    fun existsByNameIgnoreCaseAndIdNot(name: String, id: Long): Boolean

    fun findByClub_Id(clubId: Long): List<Event>

    fun findByType_Id(typeId: Long): List<Event>

    fun findByClub_IdAndType_Id(clubId: Long, typeId: Long): List<Event>
    fun existsByNameIgnoreCaseAndClub_Id(name: String, clubId: Long): Boolean
    fun existsByNameIgnoreCaseAndIdNotAndClub_Id(name: String, id: Long, clubId: Long): Boolean
}