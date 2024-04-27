package net.backupcup.hexed.item.labrys

import com.mojang.blaze3d.systems.RenderSystem
import net.backupcup.hexed.Hexed
import net.backupcup.hexed.packets.HexNetworkingConstants
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.Identifier
import org.joml.Vector2i

@Environment(value = EnvType.CLIENT)
object BishopLabrysUI {
    val TEXTURE = Identifier(Hexed.MOD_ID, "textures/gui/bishop_labrys_ui.png")
    val textureSize = Vector2i(43, 17)

    var electricCharge: Int = 0

    private fun draw(drawContext: DrawContext, tickDelta: Float) {
        val drawPos = Vector2i((drawContext.scaledWindowWidth - textureSize.x)/2, (drawContext.scaledWindowHeight + textureSize.y * 2)/2)

        drawContext.drawTexture(TEXTURE, drawPos.x, drawPos.y, 0f, 0f, textureSize.x, textureSize.y, 256, 256)
        drawContext.drawTexture(TEXTURE, drawPos.x, drawPos.y, 0f, textureSize.y * electricCharge.toFloat(), textureSize.x, textureSize.y, 256, 256)
    }

    fun registerClient() {
        HudRenderCallback.EVENT.register { drawContext, tickDelta ->
            val player = MinecraftClient.getInstance().player ?: return@register

            val itemStack = if (player.mainHandStack.item is BishopLabrysItem) player.mainHandStack
            else if (player.offHandStack.item is BishopLabrysItem) player.offHandStack else return@register

            electricCharge = if (itemStack.nbt?.contains("electricCharge") == true) itemStack.nbt?.getInt("electricCharge")?: 0 else 0

            if (MinecraftClient.getInstance().isPaused || !player.isPartOfGame) {
                return@register
            }

            RenderSystem.enableBlend()
            draw(drawContext, tickDelta)
        }
    }
}