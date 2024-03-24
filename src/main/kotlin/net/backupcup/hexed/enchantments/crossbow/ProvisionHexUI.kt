package net.backupcup.hexed.enchantments.crossbow

import com.mojang.blaze3d.systems.RenderSystem
import net.backupcup.hexed.Hexed
import net.backupcup.hexed.packets.HexNetworkingConstants
import net.backupcup.hexed.register.RegisterEnchantments
import net.backupcup.hexed.register.RegisterPackets
import net.backupcup.hexed.util.HexHelper
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.CrossbowItem
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.Identifier
import org.joml.Vector2i

@Environment(value = EnvType.CLIENT)
object ProvisionHexUI {
    val TEXTURE = Identifier(Hexed.MOD_ID, "textures/gui/provision_reload.png")
    val textureSize = Vector2i(94, 19)

    var shouldRender: Boolean = false
    var indicatorPos: Int = 0

    private var tick: Long = 0L

    private fun tick(playerEntity: PlayerEntity) {
        tick++
        println("${this.shouldRender} | ${this.indicatorPos}")
    }

    private fun draw(drawContext: DrawContext, tickDelta: Float) {
        val drawPos = Vector2i((drawContext.scaledWindowWidth - textureSize.x)/2, (drawContext.scaledWindowHeight + textureSize.y)/2)

        drawContext.drawTexture(TEXTURE, drawPos.x, drawPos.y, 0f, 0f, textureSize.x, textureSize.y, 256, 256)
    }

    fun registerClient() {

        HudRenderCallback.EVENT.register { drawContext, tickDelta ->
            val player = MinecraftClient.getInstance().player ?: return@register

            val itemStack = if (player.mainHandStack.item is CrossbowItem) player.mainHandStack else {
                return@register
            }

            ClientPlayNetworking.registerGlobalReceiver(
                Identifier(Hexed.MOD_ID, "provision_update_packet"),
                ProvisionHexUI::updateData
            )

            if (!HexHelper.hasEnchantmentInSlot(itemStack, RegisterEnchantments.PROVISION_HEX) ||
                itemStack.nbt?.getBoolean("Charged") == true ||
                MinecraftClient.getInstance().isPaused ||
                !player.isPartOfGame /*  || !this.shouldRender */ ) {
                return@register
            }

            RenderSystem.enableBlend()

            tick(player)
            draw(drawContext, tickDelta)
        }
    }

    private fun updateData(
        client: MinecraftClient?,
        handler: ClientPlayNetworkHandler?,
        buf: PacketByteBuf,
        sender: PacketSender?
    ) {
        this.shouldRender = buf.readBoolean()
        this.indicatorPos = buf.readInt()
    }
}