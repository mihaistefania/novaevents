package pt.unl.fct.iadi.novaevents.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import pt.unl.fct.iadi.novaevents.model.Club

interface ClubRepository : JpaRepository<Club, Long> {

    @Query("""
        SELECT c, COUNT(e)
        FROM Club c LEFT JOIN c.events e
        GROUP BY c
    """)
    fun findAllWithEventCount(): List<Array<Any>>
}