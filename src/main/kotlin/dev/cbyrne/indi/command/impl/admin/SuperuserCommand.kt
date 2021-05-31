package dev.cbyrne.indi.command.impl.admin

import dev.cbyrne.indi.command.CommandCategory
import dev.cbyrne.indi.command.IndiCommand
import dev.cbyrne.indi.command.exception.CommandExecutionException
import dev.cbyrne.indi.command.exception.CommandIncorrectArgumentsException
import dev.cbyrne.indi.database.Database
import dev.cbyrne.indi.embed.embed
import dev.cbyrne.indi.embed.successEmbed
import dev.cbyrne.indi.extension.reply
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message

class SuperuserCommand : IndiCommand(
    "superuser",
    "Add / remove a superuser",
    category = CommandCategory.ADMINISTRATOR,
    requiresAdministrator = true
) {
    override fun execute(sender: Member, guild: Guild, message: Message, arguments: List<String>) {
        val action = arguments.getOrNull(0)
        if (action.isNullOrEmpty() || action == "list") {
            val users = Database.getAllUsers()

            val superusers = users.filter { it.isSuperuser }.joinToString(", ") {
                guild.jda.getUserById(it._id)?.asMention ?: it._id.toString()
            }

            val administrators = users.filter { it.isAdministrator }.joinToString(", ") {
                guild.jda.getUserById(it._id)?.asMention ?: it._id.toString()
            }

            return message.reply(embed {
                title = "Superusers"
                description = "These people have access to special commands, which can be seen in the help menu"

                field("Administrators", administrators.ifEmpty { "None" })
                field("Superusers", superusers.ifEmpty { "None" })

                timestamp()
                requester(message.author)
            }, false)
        }

        if (arguments.size < 2)
            throw CommandIncorrectArgumentsException(arguments.size, 2)

        val target = getTarget(message, arguments)
        if (target.isBot || target.isSystem)
            throw CommandExecutionException("This user is exempt from superuser modifications as they are a bot")

        val targetDocument = Database.getOrInsertUser(target.idLong)
        if (targetDocument.isAdministrator)
            throw CommandExecutionException("This user is exempt from superuser modifications as they are an administrator")

        when (action) {
            "add" -> {
                targetDocument.isSuperuser = true
                Database.updateUser(targetDocument)

                message.reply(
                    successEmbed(
                        "Superuser Modification",
                        "Added ${target.asMention} to the list of superusers",
                        sender.user
                    ), false
                )
            }
            "remove" -> {
                targetDocument.isSuperuser = false
                Database.updateUser(targetDocument)

                message.reply(
                    successEmbed(
                        "Superuser Modification",
                        "Removed ${target.asMention} from the list of superusers",
                        sender.user
                    ), false
                )
            }
            else ->
                throw CommandExecutionException("Invalid action.\nExpected: \"add\" or \"remove\", received: \"${action}\"")
        }
    }
}