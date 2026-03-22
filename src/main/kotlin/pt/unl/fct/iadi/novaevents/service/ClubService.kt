package pt.unl.fct.iadi.novaevents.service

import org.springframework.stereotype.Service
import pt.unl.fct.iadi.novaevents.model.*

@Service
class ClubService {

    private val clubs = listOf(
        Club(1, "Chess Club", "Play and learn chess with others.", ClubCategory.ACADEMIC),

        Club(2, "Robotics Club",
            "The Robotics Club is the place to turn ideas into machines",
            ClubCategory.TECHNOLOGY),

        Club(3, "Photography Club", "Capture and share moments.", ClubCategory.ARTS),

        Club(4, "Hiking & Outdoors Club", "Explore nature and adventure.", ClubCategory.SPORTS),

        Club(5, "Film Society", "Watch and discuss films together.", ClubCategory.CULTURAL)
    )

    fun getAll(): List<Club> = clubs

    fun getById(id: Long): Club {
        return clubs.find { it.id == id }
            ?: throw NoSuchElementException("Club not found")
    }
}