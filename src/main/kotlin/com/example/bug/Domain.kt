package com.example.bug


data class EntityOne(
    override val id: String
) : Entity {
    override val type: EntityType = EntityType.FIRST
}

data class EntityTwo(
    override val id: String
) : Entity {
    override val type: EntityType = EntityType.SECOND
}

interface Entity {
    val id: String
    val type: EntityType
}

enum class EntityType {
    FIRST, SECOND
}
