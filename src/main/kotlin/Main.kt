import commands.CommandManager
import io.github.cdimascio.dotenv.Dotenv
import listeners.EventListener
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.Icon
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.api.requests.GatewayIntent
import java.io.IOException
import java.net.URL
import javax.security.auth.login.LoginException

fun bot() {

    val config: Dotenv = Dotenv.configure().load()
    val token = config.get("TOKEN")

    val builder = JDABuilder.createDefault(token)
    builder.setStatus(OnlineStatus.ONLINE)
    builder.setActivity(Activity.playing("FiveM Atlantic"))
    builder.enableIntents(GatewayIntent.getIntents(1026))

    val jda : JDA = builder.build().awaitReady()
    jda.addEventListener(EventListener(), CommandManager())

    val guild = jda.getGuildById(config.get("GUILD_ID"))
    if (guild != null) {
        guild.modifyNickname(guild.selfMember, config.get("BOT_NAME")).queue()
        val selfUser = jda.selfUser
        val avatarURL = config.get("AVATAR_URL")
        try {
            selfUser.manager.setAvatar(Icon.from(URL(avatarURL).openStream())).queue()
        } catch (e: IOException) {
            println("URL do Avatar está incorreto!")
        }
        val commands: MutableList<CommandData> = ArrayList()
        val options = listOf(
            OptionData(
                OptionType.STRING,
                "titlo",
                "Titlo do Anúncio"
            )
                .setRequired(true),
            OptionData(
                OptionType.STRING,
                "descricao",
                "Descricao do Anúncio"
            )
                .setRequired(true)
        )
        commands.add(Commands.slash("anuncio", "Envia um anúncio para todo o pessoal!").addOptions(options))
        guild.updateCommands().addCommands(commands).queue()
    } else {
        println("ID do Server está incorreto!")
    }

    ScheduledTask(jda, config).scheduledTask()

}

fun main(args: Array<String>) {
    try {
        bot()
    } catch (e: LoginException) {
        println("Erro ao conectar ao bot, verifica o Token!")
    } catch (e: InterruptedException) {
        println("Erro ao conectar ao bot, verifica o Token!")
    }
}