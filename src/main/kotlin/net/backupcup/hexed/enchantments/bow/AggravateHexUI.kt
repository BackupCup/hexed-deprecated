package net.backupcup.hexed.enchantments.bow

import com.mojang.blaze3d.systems.RenderSystem
import net.backupcup.hexed.Hexed
import net.backupcup.hexed.packets.HexNetworkingConstants
import net.backupcup.hexed.register.RegisterEnchantments
import net.backupcup.hexed.util.HexHelper
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.item.BowItem
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.Identifier
import org.joml.Vector2i

@Environment(value = EnvType.CLIENT)
object AggravateHexUI {
    val TEXTURE = Identifier(Hexed.MOD_ID, "textures/gui/aggravate_ui.png")
    val textureSize = Vector2i(39, 18)

    var aggravateCharge: Int = 0

    private fun draw(drawContext: DrawContext, tickDelta: Float) {
        val drawPos = Vector2i((drawContext.scaledWindowWidth - textureSize.x)/2, (drawContext.scaledWindowHeight + textureSize.y)/2)

        if(aggravateCharge < 7) {
            drawContext.drawTexture(TEXTURE, drawPos.x, drawPos.y, 0f, 0f, textureSize.x, textureSize.y, 256, 256)
            drawContext.drawTexture(TEXTURE, drawPos.x + 5, drawPos.y + 5, 5f, 24f, 5 * aggravateCharge, 8, 256, 256)
        } else {
            drawContext.drawTexture(TEXTURE, drawPos.x, drawPos.y-1, 0f, 18f, 44, 21, 256, 256)
        }
    }

    fun registerClient() {
        HudRenderCallback.EVENT.register { drawContext, tickDelta ->
            val player = MinecraftClient.getInstance().player ?: return@register

            val itemStack = if (player.mainHandStack.item is BowItem) player.mainHandStack
                        else if (player.offHandStack.item is BowItem) player.offHandStack else return@register

            ClientPlayNetworking.registerGlobalReceiver(
                HexNetworkingConstants.AGGRAVATE_UPDATE_PACKET,
                AggravateHexUI::updateData
            )

            if (!HexHelper.stackHasEnchantment(itemStack, RegisterEnchantments.AGGRAVATE_HEX) ||
                MinecraftClient.getInstance().isPaused ||
                !player.isPartOfGame) {
                return@register
            }

            RenderSystem.enableBlend()
            draw(drawContext, tickDelta)
        }
    }

    private fun updateData(
        client: MinecraftClient?,
        handler: ClientPlayNetworkHandler?,
        buf: PacketByteBuf,
        sender: PacketSender?
    ) {
        this.aggravateCharge = buf.readInt()
    }
}