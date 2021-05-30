package dev.cbyrne.indi.command.impl.music

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import dev.cbyrne.indi.Indi
import dev.cbyrne.indi.command.CommandCategory
import dev.cbyrne.indi.command.IndiCommand
import dev.cbyrne.indi.command.exception.CommandExecutionException
import dev.cbyrne.indi.embed.errorEmbed
import dev.cbyrne.indi.extension.embed
import dev.cbyrne.indi.extension.inVoiceChannelWith
import dev.cbyrne.indi.extension.musicManager
import dev.cbyrne.indi.extension.reply
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message

class PlayCommand :
    IndiCommand(
        name = "play",
        description = "Play a song in the voice channel that you are in",
        category = CommandCategory.MUSIC
    ) {
    override fun execute(sender: Member, guild: Guild, message: Message, arguments: List<String>) {
        if (sender.voiceState?.channel == null)
            throw CommandExecutionException("You are not in a voice channel")

        if (arguments.isEmpty()) {
            if (guild.musicManager.player.isPaused) {
                return TogglePlaybackCommand().execute(sender, guild, message, arguments)
            } else {
                throw CommandExecutionException("To add a track to the queue, you must provide one to play!")
            }
        }

        if (guild.selfMember.voiceState?.channel == null) {
            guild.audioManager.openAudioConnection(sender.voiceState?.channel)
        } else if (!guild.selfMember.inVoiceChannelWith(sender)) {
            throw CommandExecutionException("We are not in the same voice channel")
        }

        Indi.playerManager.loadItem(
            if (arguments.size >= 2) "ytsearch: ${arguments.joinToString(" ")}" else arguments[0],
            object : AudioLoadResultHandler {
                override fun trackLoaded(track: AudioTrack) {
                    message.reply(track.embed("Added to queue", message.author), false)
                    guild.musicManager.eventAdapter.queue(track, sender, message.textChannel)
                }

                override fun playlistLoaded(playlist: AudioPlaylist) {
                    // If the operation was a search, we only want to add the first result
                    if (arguments.size >= 2) {
                        val track = playlist.tracks[0]
                        guild.musicManager.eventAdapter.queue(track, sender, message.textChannel)

                        message.reply(track.embed("Added to queue", message.author), false)
                    } else {
                        playlist.tracks.forEach {
                            guild.musicManager.eventAdapter.queue(it, sender, message.textChannel)
                        }

                        message.reply(playlist.embed("Added to queue", message.author), false)
                    }
                }

                override fun noMatches() {
                    guild.audioManager.closeAudioConnection()
                    message.reply(errorEmbed("No matches found", sender.user), false)
                }

                override fun loadFailed(exception: FriendlyException) {
                    guild.audioManager.closeAudioConnection()

                    message.reply(
                        errorEmbed(
                            exception.message ?: "An unknown error occurred when loading that track",
                            sender.user
                        ), false
                    )
                }
            })
    }
}
