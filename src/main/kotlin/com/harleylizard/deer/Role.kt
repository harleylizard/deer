package com.harleylizard.deer

import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Role
import java.util.function.Function

sealed interface Role : Function<Guild, Role> {

    companion object {
        val everyone get() = EveryoneRole.everyone

        fun of(role: Long) = LongRole.of(role)
    }
}