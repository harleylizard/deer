package com.harleylizard.deer

import it.unimi.dsi.fastutil.longs.LongArraySet
import it.unimi.dsi.fastutil.longs.LongSet
import net.dv8tion.jda.api.entities.Guild

class DiscordServer {
    private val channels: LongSet = LongArraySet()
    private val notifying = mutableSetOf<Role>()

    fun channel(channel: Long) {
        channels += channel
    }

    fun notify(role: Role) {
        notifying += role
    }

    fun channels(guild: Guild) = guild.textChannels.stream().filter { channels.contains(it.idLong) }.toList()

    fun notifying(guild: Guild): String {
        val builder = StringBuilder()
        for (role in notifying) {
            val applied = role.apply(guild)
            builder.append(if (applied.isPublicRole) "@everyone" else "<@&${applied.idLong}>")
        }
        return builder.toString()
    }

}