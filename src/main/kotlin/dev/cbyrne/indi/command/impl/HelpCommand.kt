package dev.cbyrne.indi.command.impl

import dev.cbyrne.indi.command.CommandHandler
import dev.cbyrne.indi.command.IndiCommand
import dev.cbyrne.indi.embed.embed
import dev.cbyrne.indi.extension.reply
import dev.cbyrne.indi.extension.stringSuffix
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message

class HelpCommand : IndiCommand(
    name = "help",
    description = "View all the commands that Indi has",
    hidden = true
) {
    override fun execute(sender: Member, guild: Guild, message: Message, arguments: List<String>) {
        message.reply(embed {
            title = "Help"
            description = "👋 Hey, I'm Indi!\nI have a lot of commands, here's a list of them!"

            val commandsByCategory = CommandHandler.getVisibleCommands(message.author)
                .sortedBy { it.name }
                .groupBy { it.category }

            commandsByCategory.entries
                .sortedBy { it.key.displayName }
                .forEach { (category, commands) ->
                    val content = commands.joinToString("\n") {
                        var description =
                            if (it.requiresSuperuser)
                                "${it.description} **(superuser)**"
                            else if (it.requiresAdministrator)
                                "${it.description} **(administrator)**"
                            else it.description

                        if (it.aliases.isNotEmpty())
                            description += " (aliases: ${it.aliases.joinToString(", ")})"

                        "**${it.name}**: $description"
                    }

                    field(
                        "${category.icon} ${category.displayName} (${commands.size} command${commands.stringSuffix})",
                        content
                    )
                }

            timestamp()
            requester(message.author)
        }, false)
    }
}