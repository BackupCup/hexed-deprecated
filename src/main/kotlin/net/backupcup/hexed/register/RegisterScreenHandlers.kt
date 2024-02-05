package net.backupcup.hexed.register

import net.backupcup.hexed.Hexed
import net.backupcup.hexed.altar.AccursedAltarScreenHandler
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.util.Identifier

object RegisterScreenHandlers {
    val ACCURSED_ALTAR_SCREEN_HANDLER: ScreenHandlerType<AccursedAltarScreenHandler> = ScreenHandlerRegistry.registerSimple(
        Identifier(Hexed.MOD_ID, "accursed_altar_sreen_handler"), ::AccursedAltarScreenHandler)

    fun registerScreenHandlers() {
        ACCURSED_ALTAR_SCREEN_HANDLER
    }
}