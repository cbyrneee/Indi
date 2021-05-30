package dev.cbyrne.indi.audio

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import net.dv8tion.jda.api.managers.AudioManager

class IndiMusicManager(playerManager: DefaultAudioPlayerManager, audioManager: AudioManager) {
    val player: AudioPlayer
    val eventAdapter: IndiAudioEventAdapter
    val sendHandler: IndiAudioSendHandler
        get() = IndiAudioSendHandler(player)

    init {
        player = playerManager.createPlayer()
        eventAdapter = IndiAudioEventAdapter(player, audioManager)

        player.addListener(eventAdapter)
    }
}