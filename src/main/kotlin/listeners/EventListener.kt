package listeners

import io.github.cdimascio.dotenv.Dotenv
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.awt.Color

class EventListener : ListenerAdapter() {

    private val config = Dotenv.configure().load()

    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {
        if (event.user.isBot) return
        val roleID = config["ROLE_MEMBER_ID"]
        val user = event.user
        val role = event.guild.getRoleById(roleID)
        if (role != null) {
            event.guild.addRoleToMember(user, role).queue()
        } else {
            println("ID do Role de Membro está incorrecto!")
        }
        val embed = EmbedBuilder()
            .setColor(Color.WHITE)
            .setTitle("Welcome")
            .setDescription("**" + event.user.asTag + "** acabou de entrar na nossa GANG 185!\n||@everyone||\n\n")
            .setImage(config["AVATAR_URL"])
        val channelID = config["WELCOME_CHANNEL_ID"]
        val channel = event.guild.getChannelById(MessageChannel::class.java, channelID)
        if (channel != null) {
            channel.sendMessageEmbeds(embed.build())
                .complete()
                .addReaction(Emoji.fromUnicode("U+1F49A"))
                .queue()
        } else {
            println("ID do Chat Bem-Vindo está incorreto!")
        }
    }
}