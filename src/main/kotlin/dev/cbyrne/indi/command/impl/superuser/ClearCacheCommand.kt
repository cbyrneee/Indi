package dev.cbyrne.indi.command.impl.superuser

import dev.cbyrne.indi.Indi
import dev.cbyrne.indi.command.CommandCategory
import dev.cbyrne.indi.command.IndiCommand
import dev.cbyrne.indi.database.Database
import dev.cbyrne.indi.embed.successEmbed
import dev.cbyrne.indi.extension.asReference
import dev.cbyrne.indi.extension.editMessage
import dev.cbyrne.indi.extension.reply
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message

class ClearCacheCommand : IndiCommand(
    name = "clearcache",
    description = "Clears any cached info from the database",
    category = CommandCategory.ADMINISTRATOR,
    requiresSuperuser = true
) {
    override fun execute(sender: Member, guild: Guild, message: Message, arguments: List<String>) {
        Indi.logger.warn("${sender.user.asReference} (${sender.user.id}) has requested to clear the database cache")

        message.reply(successEmbed("Cache", "Clearing database cache...", sender.user), false) {
            Database.clearCache()
            it.editMessage(successEmbed("Cache", "Cleared database cache", sender.user), false)
        }
    }
}