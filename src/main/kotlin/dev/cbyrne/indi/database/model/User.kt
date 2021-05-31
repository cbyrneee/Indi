package dev.cbyrne.indi.database.model

data class User(
    val _id: Long,
    var isSuperuser: Boolean,
    var isAdministrator: Boolean
)