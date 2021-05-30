package dev.cbyrne.indi.extension

import net.dv8tion.jda.api.entities.Member

fun Member.inVoiceChannelWith(other: Member): Boolean {
    // If we are not in a voice channel, or the other member is not in a voice channel, return false
    if (other.voiceState?.channel == null) return false
    if (voiceState?.channel == null) return false

    return voiceState?.channel?.idLong == other.voiceState?.channel?.idLong
}