package dev.cbyrne.indi.command.impl.moderation

import dev.cbyrne.indi.command.CommandCategory
import dev.cbyrne.indi.command.IndiCommand
import dev.cbyrne.indi.command.exception.BotRequiresPermissionException
import dev.cbyrne.indi.command.exception.CommandRequiresPermissionException
import dev.cbyrne.indi.embed.errorEmbed
import dev.cbyrne.indi.embed.neutralEmbed
import dev.cbyrne.indi.embed.successEmbed
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.Button
import java.util.*

class KickCommand : IndiCommand(
    name = "kick",
    description = "Kicks a user from this guild",
    category = CommandCategory.MODERATION,
    requiresArguments = true
) {
    private val kickUserForMessageId = mutableMapOf<Long, User>()

    override fun execute(sender: Member, guild: Guild, message: Message, arguments: List<String>) {
        if (!sender.hasPermission(Permission.KICK_MEMBERS))
            throw CommandRequiresPermissionException(Permission.KICK_MEMBERS)

        if (!guild.selfMember.hasPermission(Permission.KICK_MEMBERS))
            throw BotRequiresPermissionException(Permission.KICK_MEMBERS)

        val target = getTarget(message, arguments)
        message.reply(
            neutralEmbed(
                "Kick Confirmation",
                "Are you sure you want to kick ${target.asMention}?",
                sender.user
            )
        ).setActionRows(
            ActionRow.of(Button.success("kick", "Kick user"), Button.danger("cancel", "Cancel"))
        ).mentionRepliedUser(false).queue {
            kickUserForMessageId[it.idLong] = target
        }
    }

    override fun onButtonClick(event: ButtonClickEvent): Boolean {
        val user = kickUserForMessageId[event.messageIdLong] ?: return false

        return when (event.componentId) {
            "kick" -> {
                try {
                    event.guild?.kick(user.id)?.queue()

                    event.editMessageEmbeds(
                        successEmbed(
                            "Kick",
                            "${user.asMention} has been kicked!",
                            null
                        )
                    ).setActionRows(Collections.emptyList()).queue()
                } catch (t: Throwable) {
                    event.editMessageEmbeds(
                        errorEmbed(
                            "I can't kick that user, are they higher in the permission hierarchy than me?",
                            null
                        )
                    ).setActionRows(Collections.emptyList()).queue()
                }
                true
            }

            "cancel" -> {
                event.message?.delete()?.queue()
                true
            }

            else -> {
                false
            }
        }
    }
}