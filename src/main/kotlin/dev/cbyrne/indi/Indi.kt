package dev.cbyrne.indi

import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import dev.cbyrne.indi.command.CommandHandler
import dev.cbyrne.indi.config.Configuration
import dev.cbyrne.indi.database.Database
import dev.cbyrne.indi.listener.ListenerHandler
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Activity
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger


class Indi {
    companion object {
        val logger: Logger = LogManager.getLogger("Indi")
        val defaultPermissions =
            listOf(
                Permission.MESSAGE_WRITE,
                Permission.MESSAGE_ATTACH_FILES,
                Permission.MESSAGE_MANAGE,
                Permission.VOICE_CONNECT,
                Permission.VOICE_SPEAK,
                Permission.KICK_MEMBERS
            )

        lateinit var playerManager: DefaultAudioPlayerManager
    }

    fun start() {
        logger.info("Starting")

        val builder = JDABuilder
            .createDefault(Configuration.token)
            .setActivity(Activity.playing("some waiting games while I start..."))

        ListenerHandler.registerListeners(builder)
        CommandHandler.registerCommands()
        Database.initialise()

        builder.build().awaitReady()

        playerManager = DefaultAudioPlayerManager()
        AudioSourceManagers.registerRemoteSources(playerManager)
        AudioSourceManagers.registerLocalSource(playerManager)
    }
}
