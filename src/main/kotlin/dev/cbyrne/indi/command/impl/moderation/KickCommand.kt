package dev.cbyrne.indi.command.impl.moderation

import dev.cbyrne.indi.command.CommandCategory
import dev.cbyrne.indi.command.IndiCommand
import dev.cbyrne.indi.command.exception.BotRequiresPermissionException
import dev.cbyrne.indi.command.exception.CommandExecutionException
import dev.cbyrne.indi.command.exception.CommandRequiresPermissionException
import dev.cbyrne.indi.extension.completedReaction
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message

class KickCommand : IndiCommand(
    name = "kick",
    description = "Kicks a user from this guild",
    category = CommandCategory.MODERATION,
    requiresArguments = true
) {
    override fun execute(sender: Member, guild: Guild, message: Message, arguments: List<String>) {
        if (!sender.hasPermission(Permission.KICK_MEMBERS))
            throw CommandRequiresPermissionException(Permission.KICK_MEMBERS)

        if (!guild.selfMember.hasPermission(Permission.KICK_MEMBERS))
            throw BotRequiresPermissionException(Permission.KICK_MEMBERS)

        val target = try {
            guild.jda.getUserById(arguments[0])
        } catch (t: Throwable) {
            message.mentionedUsers.getOrNull(0)
        } ?: throw CommandExecutionException("You must mention a valid user or give their ID")

        try {
            guild.kick(
                target.id,
                if (arguments.size >= 2) arguments.drop(1).joinToString(" ") else "No reason provided"
            ).queue()

            message.completedReaction()
        } catch (t: Throwable) {
            throw CommandExecutionException("I can't kick that user, are they higher in the permission hierarchy than me?")
        }
    }
}