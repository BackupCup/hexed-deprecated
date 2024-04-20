package net.backupcup.hexed.enchantments.crossbow

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
import net.minecraft.item.CrossbowItem
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.Identifier
import org.joml.Vector2i

@Environment(value = EnvType.CLIENT)
object OverclockHexUI {
    val TEXTURE = Identifier(Hexed.MOD_ID, "textures/gui/overclock_ui.png")
    val textureSize = Vector2i(37, 37)

    var overclockCharge: Int = 0

    private fun draw(drawContext: DrawContext, tickDelta: Float) {
        val drawPos = Vector2i((drawContext.scaledWindowWidth - textureSize.x)/2, (drawContext.scaledWindowHeight - textureSize.y)/2)

        drawContext.drawTexture(TEXTURE, drawPos.x, drawPos.y, 0f, 0f, textureSize.x, textureSize.y, 256, 256)

        if (this.overclockCharge in 0 .. 3) {
            drawContext.drawTexture(TEXTURE, drawPos.x, drawPos.y, 37f, 0f, textureSize.x, textureSize.y, 256, 256); return } //4
        if (this.overclockCharge in 4 .. 7) {
            drawContext.drawTexture(TEXTURE, drawPos.x, drawPos.y, 74f, 0f, textureSize.x, textureSize.y, 256, 256); return } //4
        if (this.overclockCharge in 8 .. 10) {
            drawContext.drawTexture(TEXTURE, drawPos.x, drawPos.y, 111f, 0f, textureSize.x, textureSize.y, 256, 256); return } //3
        if (this.overclockCharge in 11 ..13) {
            drawContext.drawTexture(TEXTURE, drawPos.x, drawPos.y, 148f, 0f, textureSize.x, textureSize.y, 256, 256); return } //3
        if (this.overclockCharge in 14..15) {
            drawContext.drawTexture(TEXTURE, drawPos.x, drawPos.y, 37f, 37f, textureSize.x, textureSize.y, 256, 256); return } //2
        if (this.overclockCharge in 16..17) {
            drawContext.drawTexture(TEXTURE, drawPos.x, drawPos.y, 74f, 37f, textureSize.x, textureSize.y, 256, 256); return } //2
        if (this.overclockCharge == 18) {
            drawContext.drawTexture(TEXTURE, drawPos.x, drawPos.y, 111f, 37f, textureSize.x, textureSize.y, 256, 256); return } //1
        if (this.overclockCharge in 19..20) {
            drawContext.drawTexture(TEXTURE, drawPos.x, drawPos.y, 148f, 37f, textureSize.x, textureSize.y, 256, 256); return } //1
    }

    fun registerClient() {
        HudRenderCallback.EVENT.register { drawContext, tickDelta ->
            val player = MinecraftClient.getInstance().player ?: return@register

            ClientPlayNetworking.registerGlobalReceiver(
                HexNetworkingConstants.OVERCLOCK_UPDATE_PACKET,
                OverclockHexUI::updateData
            )

            val itemStack = if (player.mainHandStack.item is CrossbowItem) player.mainHandStack else {
                return@register
            }

            if (!HexHelper.stackHasEnchantment(itemStack, RegisterEnchantments.OVERCLOCK_HEX) ||
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
        this.overclockCharge = buf.readInt()
    }
}