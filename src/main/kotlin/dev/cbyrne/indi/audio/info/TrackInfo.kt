package dev.cbyrne.indi.audio.info

import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.TextChannel

data class TrackInfo(
    val track: AudioTrack,
    val author: Member,
    val textChannel: TextChannel
)
