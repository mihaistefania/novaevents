package pt.unl.fct.iadi.novaevents

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import pt.unl.fct.iadi.novaevents.model.*
import pt.unl.fct.iadi.novaevents.repository.*
import java.time.LocalDate

@Component
class DataInitializer(
    private val eventTypeRepository: EventTypeRepository,
    private val clubRepository: ClubRepository,
    private val eventRepository: EventRepository,
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder
) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {

        if (eventTypeRepository.count() > 0) return

        val types = eventTypeRepository.saveAll(listOf(
            EventType(name = "WORKSHOP"),
            EventType(name = "TALK"),
            EventType(name = "COMPETITION"),
            EventType(name = "SOCIAL"),
            EventType(name = "MEETING"),
            EventType(name = "OTHER")
        ))

        val clubs = clubRepository.saveAll(listOf(
            Club(name = "Chess Club", description = "Play and learn chess.", category = ClubCategory.ACADEMIC),
            Club(name = "Robotics Club", description = "The Robotics Club is the place to turn ideas into machines.", category = ClubCategory.TECHNOLOGY),
            Club(name = "Photography Club", description = "Capture moments.", category = ClubCategory.ARTS),
            Club(name = "Hiking & Outdoors Club", description = "Explore nature.", category = ClubCategory.SPORTS),
            Club(name = "Film Society", description = "Watch movies.", category = ClubCategory.CULTURAL)
        ))

        eventRepository.saveAll(listOf(
            Event(name = "Beginner's Chess Workshop", date = LocalDate.now(), location = "Room A", description = "Basics", club = clubs[0], type = types[0]),
            Event(name = "Spring Chess Tournament", date = LocalDate.now(), location = "Hall", description = "Competition", club = clubs[0], type = types[2]),

            Event(name = "Robotics Intro", date = LocalDate.now(), location = "Lab", description = "Intro robotics", club = clubs[1], type = types[0]),

            Event(name = "Photo Walk", date = LocalDate.now(), location = "City", description = "Outdoor", club = clubs[2], type = types[3]),

            Event(name = "Mountain Hike", date = LocalDate.now(), location = "Trail", description = "Adventure", club = clubs[3], type = types[3]),

            Event(name = "Film Night", date = LocalDate.now(), location = "Cinema", description = "Movies", club = clubs[4], type = types[3])
        ))

        if (userRepository.count() == 0L) {

            val alice = userRepository.save(
                User(
                    username = "alice",
                    password = passwordEncoder.encode("password123")
                )
            )

            val bob = userRepository.save(
                User(
                    username = "bob",
                    password = passwordEncoder.encode("password123")
                )
            )

            val charlie = userRepository.save(
                User(
                    username = "charlie",
                    password = passwordEncoder.encode("password123")
                )
            )

            roleRepository.saveAll(listOf(
                Role(name = "ROLE_EDITOR", user = alice),
                Role(name = "ROLE_EDITOR", user = bob),
                Role(name = "ROLE_ADMIN", user = charlie)
            ))
        }
    }
}