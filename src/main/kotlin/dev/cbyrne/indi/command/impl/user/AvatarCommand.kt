package dev.cbyrne.indi.command.impl.user

import dev.cbyrne.indi.command.CommandCategory
import dev.cbyrne.indi.command.IndiCommand
import dev.cbyrne.indi.embed.embed
import dev.cbyrne.indi.extension.asReference
import dev.cbyrne.indi.extension.reply
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message

class AvatarCommand :
    IndiCommand(
        name = "avatar",
        description = "Get the avatar of a user",
        category = CommandCategory.USER,
        aliases = listOf("av")
    ) {
    override fun execute(sender: Member, guild: Guild, message: Message, arguments: List<String>) {
        val target = runCatching { getTarget(message, arguments) }.getOrElse { sender.user }
        val avatarUrl = target.avatarUrl ?: target.defaultAvatarUrl

        return message.reply(
            embed {
                title = "Avatar for ${target.asReference}"
                description = "[Link](${avatarUrl})"

                image(avatarUrl)
                requester(sender.user)

                timestamp()
            },
            false
        )
    }
}
