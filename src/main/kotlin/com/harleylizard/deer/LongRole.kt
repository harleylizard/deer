package com.harleylizard.deer

import net.dv8tion.jda.api.entities.Guild

class LongRole private constructor(private val role: Long) : Role {

    override fun apply(t: Guild) = t.getRoleById(role) ?: throw IllegalArgumentException()

    companion object {

        fun of(role: Long): Role = LongRole(role)
    }
}