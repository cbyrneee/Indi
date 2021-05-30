package dev.cbyrne.indi.extension

import dev.cbyrne.indi.database.Database
import net.dv8tion.jda.api.entities.User

val User.isSuperuser: Boolean
    get() {
        val user = Database.getOrInsertUser(this.idLong)
        return user.isSuperuser || user.isAdministrator
    }

val User.isAdministrator: Boolean
    get() {
        val user = Database.getOrInsertUser(this.idLong)
        return user.isAdministrator
    }

val User.asReference: String
    get() = "${this.name}#${this.discriminator}"