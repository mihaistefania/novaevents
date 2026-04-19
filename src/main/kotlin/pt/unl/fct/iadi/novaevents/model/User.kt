package pt.unl.fct.iadi.novaevents.model

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "users")
class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(unique = true)
    val username: String,

    val password: String,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    val roles: MutableList<Role> = mutableListOf()
)