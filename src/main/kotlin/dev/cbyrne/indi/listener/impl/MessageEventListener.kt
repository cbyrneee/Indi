package dev.cbyrne.indi.listener.impl

import dev.cbyrne.indi.Indi
import dev.cbyrne.indi.command.CommandHandler
import dev.cbyrne.indi.command.exception.*
import dev.cbyrne.indi.database.Database
import dev.cbyrne.indi.embed.errorEmbed
import dev.cbyrne.indi.extension.reply
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class MessageEventListener : ListenerAdapter() {
    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        val message = event.message
        if (message.author.isBot || message.member == null) return

        val guild = Database.getOrInsertGuild(event.guild)
        val prefix = guild.prefix

        if (!message.contentRaw.startsWith(prefix)) return

        try {
            CommandHandler.execute(message, prefix)
        } catch (ex: BotRequiresPermissionException) {
            commandError(ex.message ?: "I do not have permission to do that", message)
        } catch (ex: CommandRequiresAdministratorException) {
            Indi.logger.warn("A non-administrator (${message.member!!.id}) has attempted to run a command which requires administrator privileges!")
            noPermissionError(message)
        } catch (ex: CommandRequiresSuperuserException) {
            Indi.logger.warn("A non-superuser (${message.member!!.id}) has attempted to run a command which requires superuser privileges!")
            noPermissionError(message)
        } catch (ex: CommandRequiresArgumentsException) {
            commandError("This command requires arguments and you have provided none!", message)
        } catch (ex: CommandExecutionException) {
            executeError(ex, message)
        } catch (t: Throwable) {
            unknownError(message)
            t.printStackTrace()
        }
    }

    private fun noPermissionError(message: Message) =
        commandError("You do not have permission to run this command", message)


    private fun commandError(reason: String, message: Message) =
        message.reply(errorEmbed(reason, message.author), false)

    private fun executeError(ex: CommandExecutionException, message: Message) =
        if (ex.message.isNullOrEmpty()) unknownError(message) else commandError(ex.message!!, message)

    private fun unknownError(message: Message) =
        commandError("An unknown error occurred when executing that command! Please try again later.", message)
}
