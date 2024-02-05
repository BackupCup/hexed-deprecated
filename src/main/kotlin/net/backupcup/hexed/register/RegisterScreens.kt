package net.backupcup.hexed.register

import net.backupcup.hexed.altar.AccursedAltarScreen
import net.minecraft.client.gui.screen.ingame.HandledScreens

object RegisterScreens {
    val ACCURSED_ALTAR_SCREEN = HandledScreens.register(RegisterScreenHandlers.ACCURSED_ALTAR_SCREEN_HANDLER, ::AccursedAltarScreen)

    fun registerScreens() {
        ACCURSED_ALTAR_SCREEN
    }
}