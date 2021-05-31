package dev.cbyrne.indi.audio

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import net.dv8tion.jda.api.managers.AudioManager

class IndiMusicManager(playerManager: DefaultAudioPlayerManager, audioManager: AudioManager) {
    val player: AudioPlayer = playerManager.createPlayer()
    val eventAdapter: IndiAudioEventAdapter = IndiAudioEventAdapter(player, audioManager)
    val sendHandler: IndiAudioSendHandler
        get() = IndiAudioSendHandler(player)

    init {
        player.addListener(eventAdapter)
    }
}