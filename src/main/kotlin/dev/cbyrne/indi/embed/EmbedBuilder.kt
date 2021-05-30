package dev.cbyrne.indi.embed

import dev.cbyrne.indi.extension.asReference
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.User
import java.awt.Color
import java.time.Instant

class IndiEmbedBuilder {
    private val embedBuilder = EmbedBuilder()

    var title: String? = null
    var description: String? = null
    var color: Color? = null

    fun image(url: String) = embedBuilder.setImage(url)
    fun requester(user: User) = embedBuilder.setFooter("Requested by ${user.asReference}", user.avatarUrl)

    fun field(title: String, content: String, inline: Boolean = false) = embedBuilder.addField(title, content, inline)

    fun timestamp() = embedBuilder.setTimestamp(Instant.now())
    fun build() = embedBuilder.setTitle(title).setDescription(description).setColor(color).build()
}

fun embed(init: IndiEmbedBuilder.() -> Unit): MessageEmbed {
    return IndiEmbedBuilder().apply(init).build()
}

fun successEmbed(title: String, description: String?, author: User?): MessageEmbed {
    return embed {
        this.title = title
        this.description = description
        this.color = Color.GREEN

        timestamp()
        if (author != null) requester(author)
    }
}

fun neutralEmbed(title: String, description: String?, author: User?): MessageEmbed {
    return embed {
        this.title = title
        this.description = description

        timestamp()
        if (author != null) requester(author)
    }
}

fun errorEmbed(reason: String, author: User?): MessageEmbed {
    return embed {
        this.title = "Error"
        this.description = reason
        this.color = Color.RED

        timestamp()
        if (author != null) requester(author)
    }
}
