package dev.cbyrne.indi.command.impl.music

import dev.cbyrne.indi.command.CommandCategory
import dev.cbyrne.indi.command.IndiCommand
import dev.cbyrne.indi.command.exception.CommandExecutionException
import dev.cbyrne.indi.embed.neutralEmbed
import dev.cbyrne.indi.extension.inVoiceChannelWith
import dev.cbyrne.indi.extension.musicManager
import dev.cbyrne.indi.extension.reply
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message

class TogglePlaybackCommand :
    IndiCommand(
        name = "toggle",
        description = "Toggle playback of the song in the voice channel that you are in",
        category = CommandCategory.MUSIC,
        aliases = listOf("p", "pause")
    ) {
    override fun execute(sender: Member, guild: Guild, message: Message, arguments: List<String>) {
        if (sender.voiceState?.channel == null)
            throw CommandExecutionException("You are not in a voice channel")

        if (guild.selfMember.voiceState?.channel == null)
            throw CommandExecutionException("I am not in a voice channel")

        if (!guild.selfMember.inVoiceChannelWith(sender))
            throw CommandExecutionException("We are not in the same voice channel")

        guild.musicManager.player.isPaused = !guild.musicManager.player.isPaused

        return message.reply(
            neutralEmbed(
                "Playback",
                "Playback has been ${if (guild.musicManager.player.isPaused) "paused" else "resumed"}",
                sender.user
            ),
            false
        )
    }
}
