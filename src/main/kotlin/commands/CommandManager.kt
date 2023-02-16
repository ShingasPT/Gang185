package commands

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.awt.Color
import java.util.*

class CommandManager : ListenerAdapter() {

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        val command = event.name
        if (command == "anuncio") {
            val title = event.getOption("titlo")
            val description = event.getOption("descricao")
            val builder = EmbedBuilder()
                .setColor(Color.WHITE)
                .setTitle(title!!.asString.uppercase(Locale.getDefault()))
                .setDescription(description!!.asString + "\n\n||@everyone||\n")
                .setFooter(event.user.name)
            val embed = builder.build()
            event.replyEmbeds(embed).queue()
            val channel: MessageChannel = event.channel
            val messageID = event.channel.latestMessageId
            channel.addReactionById(messageID, Emoji.fromUnicode("U+1F49A")).queue()
        }
    }

}