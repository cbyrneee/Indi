package dev.cbyrne.indi.listener.impl

import dev.cbyrne.indi.command.CommandHandler
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class ButtonClickListener : ListenerAdapter() {
    override fun onButtonClick(event: ButtonClickEvent) {
        event.deferEdit().queue()
        CommandHandler.buttonClicked(event)
    }
}