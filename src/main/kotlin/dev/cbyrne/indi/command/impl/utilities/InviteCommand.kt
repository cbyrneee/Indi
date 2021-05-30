package dev.cbyrne.indi.command.impl.utilities

import dev.cbyrne.indi.Indi
import dev.cbyrne.indi.command.CommandCategory
import dev.cbyrne.indi.command.IndiCommand
import dev.cbyrne.indi.command.exception.CommandExecutionException
import dev.cbyrne.indi.embed.neutralEmbed
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message

class InviteCommand :
    IndiCommand(
        name = "invite",
        description = "Get an invite link to invite Indi to your server",
        category = CommandCategory.UTILITIES
    ) {
    override fun execute(sender: Member, guild: Guild, message: Message, arguments: List<String>) {
        try {
            message.author.openPrivateChannel().queue {
                it.sendMessage(
                    neutralEmbed(
                        "Invite Link",
                        "[Click here to invite me to your server!](${guild.jda.getInviteUrl(Indi.defaultPermissions)})",
                        sender.user
                    )
                ).queue()
            }
        } catch (t: Throwable) {
            throw CommandExecutionException("I was unable to DM you, please make sure that you have DMs turned on for server members!")
        }
        
        message.addReaction("✅").queue()
    }
}
