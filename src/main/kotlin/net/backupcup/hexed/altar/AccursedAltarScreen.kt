package net.backupcup.hexed.altar

import com.mojang.blaze3d.systems.RenderSystem
import net.backupcup.hexed.Hexed
import net.minecraft.client.font.MultilineText
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.gui.tooltip.Tooltip
import net.minecraft.client.render.GameRenderer
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.StringVisitable
import net.minecraft.text.Text
import net.minecraft.text.Texts
import net.minecraft.util.Colors
import net.minecraft.util.Identifier
import org.joml.Matrix4f

class AccursedAltarScreen(
    handler: AccursedAltarScreenHandler?,
    inventory: PlayerInventory?,
    title: Text?
) : HandledScreen<AccursedAltarScreenHandler>(handler, inventory, title) {
    private var TEXTURE = Identifier(Hexed.MOD_ID, "textures/gui/accursed_altar.png")

    override fun init() {
        super.init()
        titleY = 1000
        playerInventoryTitleY = 1000
    }

    override fun drawBackground(context: DrawContext?, delta: Float, mouseX: Int, mouseY: Int) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram)
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        RenderSystem.setShaderTexture(0, TEXTURE)

        backgroundWidth = 196
        backgroundHeight = 200

        x = (width - backgroundWidth) / 2
        y = (height - backgroundHeight) / 2

        context?.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight)
    }

    override fun render(context: DrawContext?, mouseX: Int, mouseY: Int, delta: Float) {
        renderBackground(context)
        super.render(context, mouseX, mouseY, delta)
        RenderSystem.setShaderTexture(0, TEXTURE)



        drawMouseoverTooltip(context, mouseX, mouseY)
    }
}