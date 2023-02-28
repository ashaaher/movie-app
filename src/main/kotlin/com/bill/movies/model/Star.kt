package com.bill.movies.model

import javax.persistence.*


@Entity
@Table(name = "star")
data class Star(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @Column(nullable = false)
    val name: String = "",

    @ManyToMany(mappedBy = "stars")
    val movies: MutableSet<Movie> = mutableSetOf(),
) {

    constructor(name: String) : this(0, name)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        other as Star
        return id == other.id
    }

    override fun hashCode(): Int {
        return id
    }

    override fun toString(): String {
        return "Star(id=$id, name='$name')"
    }
}