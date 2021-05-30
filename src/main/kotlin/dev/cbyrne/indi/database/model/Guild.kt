package dev.cbyrne.indi.database.model

import dev.cbyrne.indi.config.Configuration

data class Guild(
    val _id: Long,
    var name: String,
    var prefix: String = Configuration.prefix
)
