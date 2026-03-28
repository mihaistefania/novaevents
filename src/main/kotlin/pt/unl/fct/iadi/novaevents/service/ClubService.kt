package pt.unl.fct.iadi.novaevents.service

import org.springframework.stereotype.Service
import pt.unl.fct.iadi.novaevents.model.Club
import pt.unl.fct.iadi.novaevents.repository.ClubRepository

@Service
class ClubService(
    private val clubRepository: ClubRepository
) {

    fun findAll(): List<Club> = clubRepository.findAll()

    fun findById(id: Long): Club =
        clubRepository.findById(id)
            .orElseThrow { NoSuchElementException("Club not found") }
    fun findAllWithEventCount(): List<Pair<Club, Long>> {
        return clubRepository.findAllWithEventCount().map {
            val club = it[0] as Club
            val count = it[1] as Long
            Pair(club, count)
        }
    }
}