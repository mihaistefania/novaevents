package pt.unl.fct.iadi.novaevents.model

import jakarta.persistence.*

@Entity
@Table(name = "clubs")
open class Club(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open val id: Long = 0,

    open val name: String = "",

    @Column(length = 2000)
    open val description: String = "",

    @Enumerated(EnumType.STRING)
    open val category: ClubCategory = ClubCategory.ACADEMIC,

    @OneToMany(mappedBy = "club", fetch = FetchType.LAZY)
    open val events: MutableList<Event> = mutableListOf()
)