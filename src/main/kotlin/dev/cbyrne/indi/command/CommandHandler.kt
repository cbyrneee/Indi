package dev.cbyrne.indi.command

import dev.cbyrne.indi.command.exception.CommandRequiresAdministratorException
import dev.cbyrne.indi.command.exception.CommandRequiresArgumentsException
import dev.cbyrne.indi.command.exception.CommandRequiresSuperuserException
import dev.cbyrne.indi.command.impl.HelpCommand
import dev.cbyrne.indi.command.impl.admin.ShutdownCommand
import dev.cbyrne.indi.command.impl.admin.SuperuserCommand
import dev.cbyrne.indi.command.impl.moderation.KickCommand
import dev.cbyrne.indi.command.impl.moderation.PrefixCommand
import dev.cbyrne.indi.command.impl.music.*
import dev.cbyrne.indi.command.impl.superuser.ClearCacheCommand
import dev.cbyrne.indi.command.impl.user.AvatarCommand
import dev.cbyrne.indi.command.impl.utilities.InfoCommand
import dev.cbyrne.indi.command.impl.utilities.InviteCommand
import dev.cbyrne.indi.command.impl.utilities.PingCommand
import dev.cbyrne.indi.extension.isAdministrator
import dev.cbyrne.indi.extension.isSuperuser
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.User

object CommandHandler {
    private val commands = mutableListOf<IndiCommand>()

    fun registerCommands() {
        registerCommand(PingCommand())
        registerCommand(ShutdownCommand())
        registerCommand(SuperuserCommand())
        registerCommand(PrefixCommand())
        registerCommand(ClearCacheCommand())
        registerCommand(InfoCommand())
        registerCommand(AvatarCommand())
        registerCommand(PlayCommand())
        registerCommand(DisconnectCommand())
        registerCommand(HelpCommand())
        registerCommand(TogglePlaybackCommand())
        registerCommand(SkipCommand())
        registerCommand(InviteCommand())
        registerCommand(NowPlayingCommand())
        registerCommand(KickCommand())
    }

    fun execute(message: Message, prefix: String) {
        val dissectedMessage = message.contentRaw.split(" ")
        val commandName = dissectedMessage[0].removePrefix(prefix)

        val command = commands.firstOrNull { cmd ->
            cmd.name == commandName || cmd.aliases.any { it == commandName }
        } ?: return

        if (!message.author.isSuperuser && command.requiresSuperuser) throw CommandRequiresSuperuserException()
        if (!message.author.isAdministrator && command.requiresAdministrator) throw CommandRequiresAdministratorException()

        val arguments = dissectedMessage.drop(1)
        if (arguments.isEmpty() && command.requiresArguments) throw CommandRequiresArgumentsException()
        command.execute(message.member!!, message.guild, message, arguments)
    }

    fun getVisibleCommands(user: User) = commands.filter {
        if (it.requiresSuperuser) user.isSuperuser
        if (it.requiresAdministrator) user.isAdministrator
        else !it.hidden
    }

    private fun registerCommand(command: IndiCommand) = commands.add(command)
}