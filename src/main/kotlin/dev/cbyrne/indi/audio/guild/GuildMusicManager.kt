package dev.cbyrne.indi.audio.guild

import dev.cbyrne.indi.Indi
import dev.cbyrne.indi.audio.IndiMusicManager
import net.dv8tion.jda.api.entities.Guild

object GuildMusicManager {
    private val managers = mutableMapOf<Long, IndiMusicManager>()

    fun getMusicManager(guild: Guild): IndiMusicManager {
        val cached = managers[guild.idLong]
        if (cached != null) return cached

        val manager = IndiMusicManager(Indi.playerManager, guild.audioManager)
        managers[guild.idLong] = manager

        guild.audioManager.sendingHandler = manager.sendHandler
        return manager
    }
}