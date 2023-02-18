import io.github.cdimascio.dotenv.Dotenv
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel
import java.awt.Color
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class ScheduledTask(private var jda: JDA, private var config: Dotenv) {

    private lateinit var TARGET_TIME: LocalTime
    private lateinit var TARGET_TIME_RESET: LocalTime

    private var hasSent = false

    fun scheduledTask() {
        val TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm")
        TARGET_TIME = LocalTime.parse(config.get("RADIO_SEND"), TIME_FORMAT)
        TARGET_TIME_RESET = LocalTime.parse(config.get("RADIO_RESET"), TIME_FORMAT)
        val scheduler = Executors.newScheduledThreadPool(1)
        scheduler.scheduleAtFixedRate({ sendMessage() }, 0, 1, TimeUnit.MINUTES)
    }

    private fun sendMessage() {
        val currentTime = LocalTime.now()
        if (hasSent) {
            hasSent = if (currentTime.isAfter(TARGET_TIME_RESET) && currentTime.isBefore(TARGET_TIME)) {
                false
            } else return
        }

        if (!currentTime.isAfter(TARGET_TIME)) return
        val rand = Random()
        val freq = rand.nextInt(2000, 7000).toString()
        val channel = jda.getChannelById(MessageChannel::class.java, config["RADIO_CHANNEL_ID"])
        val embed = EmbedBuilder()
            .setColor(Color.WHITE)
            .setTitle("ʀᴀ́ᴅɪᴏ ᴅɪᴀ́ʀɪᴀ")
            .setDescription("**$freq**")

        if (channel != null) {

            channel.deleteMessageById(channel.latestMessageId).queue()

            channel.sendMessageEmbeds(embed.build()).queue()
            hasSent = true
        } else {
            println("Radio's ID is incorrect!")
        }

    }

}