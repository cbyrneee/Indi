package dev.cbyrne.indi.listener

import dev.cbyrne.indi.listener.impl.BotReadyListener
import dev.cbyrne.indi.listener.impl.MessageEventListener
import net.dv8tion.jda.api.JDABuilder

object ListenerHandler {
    fun registerListeners(builder: JDABuilder) {
        builder.addEventListeners(BotReadyListener())
        builder.addEventListeners(MessageEventListener())
    }
}
