package dev.cbyrne.indi.command.impl.utilities

import dev.cbyrne.indi.command.CommandCategory
import dev.cbyrne.indi.command.IndiCommand
import dev.cbyrne.indi.embed.embed
import dev.cbyrne.indi.extension.reply
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import org.apache.commons.lang3.time.DurationFormatUtils
import java.lang.management.ManagementFactory

class InfoCommand :
    IndiCommand(
        name = "info",
        description = "See some information about Indi",
        category = CommandCategory.UTILITIES
    ) {
    override fun execute(sender: Member, guild: Guild, message: Message, arguments: List<String>) {
        val uptime = DurationFormatUtils.formatDurationWords(
            ManagementFactory.getRuntimeMXBean().uptime,
            false,
            true
        )

        message.reply(
            embed {
                title = "Information"

                field("Uptime", uptime, false)
                field("Guilds", "${guild.jda.guilds.size}", true)
                field("Users", "${guild.jda.users.size}", true)

                timestamp()
                requester(sender.user)
            },
            false
        )
    }
}
