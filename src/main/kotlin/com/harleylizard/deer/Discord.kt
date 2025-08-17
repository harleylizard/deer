package com.harleylizard.deer

import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap
import it.unimi.dsi.fastutil.longs.Long2ObjectMap
import net.dv8tion.jda.api.entities.Guild
import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty

class Discord(objects: ObjectFactory) {
    private val servers: Long2ObjectMap<DiscordServer> = Long2ObjectArrayMap()

    val token: Property<String> = objects.property(String::class.java)
    val changes: SetProperty<String> = objects.setProperty(String::class.java)

    fun server(server: Long, action: Action<DiscordServer>) {
        servers.putIfAbsent(server, DiscordServer().also(action::execute))
    }

    fun get(guild: Guild): DiscordServer? = servers.get(guild.idLong)

}