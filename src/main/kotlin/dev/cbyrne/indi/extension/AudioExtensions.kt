package dev.cbyrne.indi.extension

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.api.entities.User
import org.apache.commons.lang3.time.DurationFormatUtils

fun AudioTrack.embed(title: String, requester: User) = dev.cbyrne.indi.embed.embed {
    this.title = title

    field("Title", info.title)
    field("Artist", info.author, true)
    field(
        "Duration",
        DurationFormatUtils.formatDuration(info.length, "HH:mm:ss"),
        true
    )

    requester(requester)
    timestamp()
}

fun AudioPlaylist.embed(title: String, requester: User) = dev.cbyrne.indi.embed.embed {
    this.title = title
    description = "This playlist has been added to the queue"

    field("Tracks", "${tracks.size}")

    requester(requester)
    timestamp()
}