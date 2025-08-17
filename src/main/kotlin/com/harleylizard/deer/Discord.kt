package com.harleylizard.deer

import it.unimi.dsi.fastutil.longs.Long2LongArrayMap
import it.unimi.dsi.fastutil.longs.Long2LongMap
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty

class Discord(objects: ObjectFactory) {
    private val channels: Long2LongMap = Long2LongArrayMap()

    val token: Property<String> = objects.property(String::class.java)
    val changes: SetProperty<String> = objects.setProperty(String::class.java)

    fun channel(id: Long, channel: Long) {
        channels.put(id, channel)
    }

    fun getChannel(guild: Guild): GuildMessageChannel? {
        val key = guild.idLong
        val system = guild.systemChannel
        return if (channels.containsKey(key)) guild.getTextChannelById(channels.get(key)) ?: system else system
    }
}