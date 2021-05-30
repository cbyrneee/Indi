package dev.cbyrne.indi.command.impl.moderation

import dev.cbyrne.indi.command.CommandCategory
import dev.cbyrne.indi.command.IndiCommand
import dev.cbyrne.indi.command.exception.CommandRequiresPermissionException
import dev.cbyrne.indi.database.Database
import dev.cbyrne.indi.embed.neutralEmbed
import dev.cbyrne.indi.embed.successEmbed
import dev.cbyrne.indi.extension.isSuperuser
import dev.cbyrne.indi.extension.reply
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message

class PrefixCommand : IndiCommand(
    name = "prefix",
    description = "Sets the prefix for this guild",
    category = CommandCategory.MODERATION,
    requiresArguments = false
) {
    override fun execute(sender: Member, guild: Guild, message: Message, arguments: List<String>) {
        if (!sender.hasPermission(Permission.MANAGE_SERVER) || !message.author.isSuperuser)
            throw CommandRequiresPermissionException(Permission.MANAGE_SERVER)

        val guildDocument = Database.getOrInsertGuild(guild)
        val newPrefix = arguments.getOrNull(0)

        if (newPrefix != null) {
            guildDocument.prefix = newPrefix
            Database.updateGuild(guildDocument)

            message.reply(successEmbed("Prefix", "Changed the server prefix to ``$newPrefix``", sender.user), false)
        } else {
            message.reply(
                neutralEmbed("Prefix", "This server's prefix is ``${guildDocument.prefix}``", sender.user),
                false
            )
        }
    }
}