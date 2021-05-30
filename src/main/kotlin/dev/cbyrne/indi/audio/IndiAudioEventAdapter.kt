package dev.cbyrne.indi.audio

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import dev.cbyrne.indi.Indi
import dev.cbyrne.indi.audio.info.TrackInfo
import dev.cbyrne.indi.embed.errorEmbed
import dev.cbyrne.indi.extension.embed
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.managers.AudioManager
import java.util.concurrent.LinkedBlockingQueue

class IndiAudioEventAdapter(
    private val audioPlayer: AudioPlayer,
    private val audioManager: AudioManager
) : AudioEventAdapter() {
    private val queue = LinkedBlockingQueue<TrackInfo>()

    fun queue(track: AudioTrack, author: Member, textChannel: TextChannel) {
        Indi.logger.info("Queuing track '${track.info.title}' by ${track.info.author} for guild ${audioManager.guild.id}")

        if (!audioPlayer.startTrack(track, true))
            queue.add(TrackInfo(track, author, textChannel))
    }

    override fun onTrackEnd(player: AudioPlayer, track: AudioTrack, endReason: AudioTrackEndReason) {
        if (endReason.mayStartNext)
            nextTrack()
    }

    override fun onTrackException(player: AudioPlayer, track: AudioTrack, exception: FriendlyException) {
        endSession()
        Indi.logger.info("An error occurred when playing track for guild ${audioManager.guild.id}: ${exception.message}")

        val trackInfo = queue.element() ?: return
        trackInfo.textChannel.sendMessage(
            errorEmbed(
                "I can't play ${track.info.title} by ${track.info.author}\nReason: ${exception.localizedMessage}",
                trackInfo.author.user
            )
        )
    }

    fun endSession() {
        Indi.logger.info("Ending session for guild ${audioManager.guild.id}")

        audioPlayer.stopTrack()
        audioManager.closeAudioConnection()
        queue.clear()
    }

    fun nextTrack(): TrackInfo? {
        val trackInfo = queue.poll() ?: run {
            endSession()
            return null
        }

        val track = trackInfo.track
        Indi.logger.info("Playing track '${track.info.title}' by ${track.info.author} in guild ${audioManager.guild.id}")

        audioPlayer.playTrack(track)
        trackInfo.textChannel.sendMessage(track.embed("Now playing", trackInfo.author.user)).queue()
        return trackInfo
    }
}