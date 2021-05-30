package dev.cbyrne.indi.command.impl.admin

import dev.cbyrne.indi.Indi
import dev.cbyrne.indi.command.CommandCategory
import dev.cbyrne.indi.command.IndiCommand
import dev.cbyrne.indi.command.exception.CommandExecutionException
import dev.cbyrne.indi.embed.successEmbed
import dev.cbyrne.indi.extension.asReference
import dev.cbyrne.indi.extension.reply
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import kotlin.system.exitProcess

class ShutdownCommand : IndiCommand(
    name = "shutdown",
    description = "Kills the current bot instance",
    category = CommandCategory.ADMINISTRATOR,
    requiresAdministrator = true
) {
    override fun execute(sender: Member, guild: Guild, message: Message, arguments: List<String>) {
        Indi.logger.warn("${sender.user.asReference} (${sender.user.id}) has requested the bot to shutdown")
        message.reply(successEmbed("Shutdown", "Shutting down...", sender.user), false)

        try {
            sender.jda.shutdownNow()
            exitProcess(-1)
        } catch (t: Throwable) {
            throw CommandExecutionException("Failed to shutdown! Reason: ${t.message}")
        }
    }
}