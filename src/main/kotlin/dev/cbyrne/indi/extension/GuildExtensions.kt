package dev.cbyrne.indi.extension

import dev.cbyrne.indi.audio.IndiMusicManager
import dev.cbyrne.indi.audio.guild.GuildMusicManager
import net.dv8tion.jda.api.entities.Guild

val Guild.musicManager: IndiMusicManager
    get() = GuildMusicManager.getMusicManager(this)