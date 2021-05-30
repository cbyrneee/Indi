package dev.cbyrne.indi.extension

import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageEmbed

fun Message.reply(content: CharSequence, mention: Boolean, success: ((Message) -> Unit) = {}) =
    reply(content).mentionRepliedUser(mention).queue(success)

fun Message.reply(embed: MessageEmbed, mention: Boolean, success: ((Message) -> Unit) = {}) =
    reply(embed).mentionRepliedUser(mention).queue(success)

fun Message.editMessage(embed: MessageEmbed, mention: Boolean) =
    editMessage(embed).mentionRepliedUser(mention).queue()