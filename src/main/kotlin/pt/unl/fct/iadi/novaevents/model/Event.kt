package pt.unl.fct.iadi.novaevents.model

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "events")
open class Event(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long = 0,

    open var name: String = "",

    open var date: LocalDate = LocalDate.now(),

    open var location: String? = null,

    open var description: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    open var club: Club = Club(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_type_id")
    open var type: EventType = EventType(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    open var owner: User? = null
)