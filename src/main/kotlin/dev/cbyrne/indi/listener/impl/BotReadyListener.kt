package dev.cbyrne.indi.listener.impl

import dev.cbyrne.indi.Indi
import dev.cbyrne.indi.config.Configuration
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class BotReadyListener : ListenerAdapter() {
    private val permissions =
        listOf(Permission.MESSAGE_WRITE, Permission.MESSAGE_ATTACH_FILES, Permission.MESSAGE_MANAGE)

    override fun onReady(event: ReadyEvent) {
        Indi.logger.info("Ready!")

        event.jda.presence.activity = Activity.listening("your commands | Prefix: \"${Configuration.prefix}\"")
        Indi.logger.info("Invite URL: ${event.jda.getInviteUrl(permissions)}")
    }
}