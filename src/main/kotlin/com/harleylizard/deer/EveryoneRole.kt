package com.harleylizard.deer

import net.dv8tion.jda.api.entities.Guild

class EveryoneRole private constructor() : Role {

    override fun apply(t: Guild) = t.publicRole

    companion object {
        val everyone: Role = EveryoneRole()

    }
}