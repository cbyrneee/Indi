package dev.cbyrne.indi.audio

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import dev.cbyrne.indi.Indi
import dev.cbyrne.indi.audio.info.TrackInfo
import dev.cbyrne.indi.embed.embed
import dev.cbyrne.indi.embed.errorEmbed
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.managers.AudioManager
import org.apache.commons.lang3.time.DurationFormatUtils
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
        Indi.logger.info("An error occurred when playing track for guild ${audioManager.guild.id}: ${exception.message}")

        return trackError(
            exception.message ?: "An unknown error has occurred",
            if (queue.isEmpty()) null else queue.element()
        )
    }

    private fun clearQueue() = queue.clear()

    fun endSession() {
        Indi.logger.info("Ending session for guild ${audioManager.guild.id}")

        audioPlayer.stopTrack()
        audioManager.closeAudioConnection()

        clearQueue()
    }

    private fun trackError(message: String, track: TrackInfo?) {
        track?.textChannel?.sendMessage(errorEmbed(message, track.author.user))?.queue()
        endSession()
    }

    fun nextTrack(): TrackInfo? {
        val trackInfo = queue.poll() ?: run {
            endSession()
            return null
        }

        val track = trackInfo.track
        audioPlayer.playTrack(track)

        trackInfo.textChannel.sendMessage(embed {
            title = "Now Playing"

            field("Title", track.info.title)
            field("Artist", track.info.author, true)
            field("Duration", DurationFormatUtils.formatDuration(track.info.length, "HH:mm:ss"), true)

            requester(trackInfo.author.user)
        }).queue()

        Indi.logger.info("Playing track '${track.info.title}' by ${track.info.author} in guild ${audioManager.guild.id}")
        return trackInfo
    }
}