package dev.cbyrne.indi.command

import dev.cbyrne.indi.command.exception.CommandExecutionException
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent

abstract class IndiCommand(
    val name: String,
    val description: String,
    val category: CommandCategory = CommandCategory.OTHER,
    val requiresSuperuser: Boolean = false,
    val requiresAdministrator: Boolean = false,
    val requiresArguments: Boolean = false,
    val hidden: Boolean = false,
    val aliases: List<String> = listOf()
) {
    @Throws(CommandExecutionException::class)
    abstract fun execute(sender: Member, guild: Guild, message: Message, arguments: List<String> = listOf())

    @Throws(CommandExecutionException::class)
    protected fun getTarget(message: Message, arguments: List<String>) =
        runCatching {
            message.jda.retrieveUserById(arguments[0]).complete()
        }.getOrElse {
            message.mentionedUsers.getOrNull(0)
        } ?: throw CommandExecutionException("You must mention a valid user or give their ID")

    open fun onButtonClick(event: ButtonClickEvent): Boolean = false
}