package dev.cbyrne.indi.command.impl.music

import dev.cbyrne.indi.command.CommandCategory
import dev.cbyrne.indi.command.IndiCommand
import dev.cbyrne.indi.command.exception.CommandExecutionException
import dev.cbyrne.indi.extension.completedReaction
import dev.cbyrne.indi.extension.inVoiceChannelWith
import dev.cbyrne.indi.extension.musicManager
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message

class SkipCommand :
    IndiCommand(
        name = "skip",
        description = "Skips the current track that is playing in your voice channel",
        category = CommandCategory.MUSIC,
        aliases = listOf("s", "next")
    ) {
    override fun execute(sender: Member, guild: Guild, message: Message, arguments: List<String>) {
        if (sender.voiceState?.channel == null)
            throw CommandExecutionException("You are not in a voice channel")

        if (guild.selfMember.voiceState?.channel == null)
            throw CommandExecutionException("I am not in a voice channel")

        if (!guild.selfMember.inVoiceChannelWith(sender))
            throw CommandExecutionException("We are not in the same voice channel")

        guild.musicManager.eventAdapter.nextTrack()
        message.completedReaction()
    }
}
