package dev.cbyrne.indi.command.impl.utilities

import dev.cbyrne.indi.command.CommandCategory
import dev.cbyrne.indi.command.IndiCommand
import dev.cbyrne.indi.embed.neutralEmbed
import dev.cbyrne.indi.extension.reply
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message

class PingCommand :
    IndiCommand(
        name = "ping",
        description = "Get the amount of time between requests to the Discord API",
        category = CommandCategory.UTILITIES
    ) {
    override fun execute(sender: Member, guild: Guild, message: Message, arguments: List<String>) {
        message.reply(neutralEmbed("Ping", "Gateway ping: ${guild.jda.gatewayPing}ms", sender.user), false)
    }
}
