package net.backupcup.hexed.altar

import com.mojang.blaze3d.systems.RenderSystem
import net.backupcup.hexed.Hexed
import net.backupcup.hexed.util.HexData
import net.backupcup.hexed.util.ScreenHelper
import net.backupcup.hexed.util.TextWrapUtils
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.gui.widget.TexturedButtonWidget
import net.minecraft.client.render.GameRenderer
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import net.minecraft.util.Colors
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import kotlin.properties.Delegates


class AccursedAltarScreen(
    handler: AccursedAltarScreenHandler,
    inventory: PlayerInventory,
    title: Text
) : HandledScreen<AccursedAltarScreenHandler>(handler, inventory, title) {
    private var TEXTURE = Identifier(Hexed.MOD_ID, "textures/gui/accursed_altar.png")
    private var isAltarActive by Delegates.notNull<Boolean>()
    private lateinit var currentHex: String
    private lateinit var availableHexList: MutableList<String>

    val buttonHex = TexturedButtonWidget(
        0, 0,
        24, 29,
        231, 32,
        31, TEXTURE
    ) { _ ->
        client?.interactionManager?.clickButton(handler.syncId, handler.BUTTON_HEX)
        this.close()
    }

    val buttonScrollUp = TexturedButtonWidget(
        0, 0,
        74, 9,
        1, 236,
        10, TEXTURE
    ) { _ ->
        client?.interactionManager?.clickButton(handler.syncId, handler.BUTTON_SCROLL_UP)
    }

    val buttonScrollDown = TexturedButtonWidget(
        0, 0,
        74, 9,
        77, 236,
        10, TEXTURE
    ) { _ ->
        client?.interactionManager?.clickButton(handler.syncId, handler.BUTTON_SCROLL_DOWN)
    }

    override fun init() {
        backgroundWidth = 196
        backgroundHeight = 200

        x = (width - backgroundWidth) / 2
        y = (height - backgroundHeight) / 2

        buttonHex.x = x + 14
        buttonHex.y = y + 45

        buttonScrollUp.x = x + 85
        buttonScrollUp.y = y + 73

        buttonScrollDown.x = x + 85
        buttonScrollDown.y = y + 98

        super.init()
        titleY = 1000
        playerInventoryTitleY = 1000

        addDrawableChild(buttonHex)
        addDrawableChild(buttonScrollUp)
        addDrawableChild(buttonScrollDown)
    }

    override fun drawBackground(context: DrawContext?, delta: Float, mouseX: Int, mouseY: Int) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram)
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        RenderSystem.setShaderTexture(0, TEXTURE)

        context?.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight)
    }

    override fun render(context: DrawContext?, mouseX: Int, mouseY: Int, delta: Float) {
        renderBackground(context)
        super.render(context, mouseX, mouseY, delta)
        RenderSystem.setShaderTexture(0, TEXTURE)

        //Arrows
        if (canUse()) {
            context?.drawTexture(TEXTURE, x + 44, y + 23, 230, 94, 15, 10)
            context?.drawTexture(TEXTURE, x + 43, y + 85, 214, 94, 15, 10)
        }

        //Hex button
        buttonBasicBehaviour(buttonHex, mouseX, mouseY,
            14, 45, 24, 29)

        //Scroll Up button
        buttonBasicBehaviour(buttonScrollUp, mouseX, mouseY,
            85, 73, 74, 9)

        //Scroll Down button
        buttonBasicBehaviour(buttonScrollDown, mouseX, mouseY,
            85, 98, 74, 9)


        //Available Hex List
        if (canUse() && this.availableHexList.isNotEmpty()) {
            val renderingHexList: List<String> = getNeighbouringHexes(this.availableHexList, this.currentHex)
            renderingHexList.forEachIndexed { index, text ->
                if (text == currentHex)
                {context?.drawTexture(TEXTURE, x + 70, y + 21 + 17 * index, 152, 239, 104, 16)}
                else
                {context?.drawTexture(TEXTURE, x + 70, y + 21 + 17 * index, 152, 222, 104, 16)}
                context?.drawTexture(TEXTURE, x + 73, y + 25 + 17 * index, 246, 94, 9, 8)
                context?.drawText(
                    textRenderer,
                    if (text == this.currentHex) Text.literal("▶ ").append(Text.translatable(text)).append(Text.literal(" ◀"))
                    else Text.translatable(text),
                    x + 85, y + 25 + 17 * index, Colors.WHITE, true)
            }


            context?.drawTexture(TEXTURE, x + 72, y + 86, 246, 94, 9, 8)
            context?.drawText(textRenderer, Text.translatable(this.currentHex), x + 84, y + 86, Colors.WHITE, true)

            context?.drawTexture(
                HexData.getHexTexture(this.currentHex),
                x + 47, y + 51, 0F, 0F,
                16, 16, 16, 16)

            HexData.getHexDescription(this.currentHex).let { this.renderTooltip(it, mouseX, mouseY, context) }
        }

        drawMouseoverTooltip(context, mouseX, mouseY)
    }

    private fun getNeighbouringHexes(availableList: List<String>, current: String): List<String> {
        val neighbouringList: List<String> = mutableListOf()
        val currentHexIndex = availableList.indexOf(current)

        neighbouringList.addLast(
            if (currentHexIndex - 1 >= 0)
                { availableList[currentHexIndex - 1] }
            else
                { availableList[availableList.size - 1] })

        neighbouringList.addLast(current)

        neighbouringList.addLast(
            if (currentHexIndex + 1 < availableList.size)
                { availableList[currentHexIndex + 1] }
            else
                { availableList[0] })

        return neighbouringList
    }

    fun updateScreenHexData(hex: String, hexList: MutableList<String>) {
        this.currentHex = hex
        this.availableHexList = hexList
    }

    fun updateScreenActiveData(isActive: Int) {
        this.isAltarActive = isActive == 1
    }

    private fun canUse(): Boolean {
        return !handler.getGearSlot().isEmpty && !handler.getMaterialSlot().isEmpty && this.isAltarActive
    }

    private fun buttonBasicBehaviour(
        buttonWidget: TexturedButtonWidget,
        mouseX: Int, mouseY: Int,
        startX: Int, startY: Int,
        width: Int, height: Int) {
            buttonWidget.active = canUse()
            buttonWidget.visible = canUse()
            buttonWidget.isFocused = ScreenHelper.isFocused(mouseX, mouseY,
            x + startX, y + startY, width, height, buttonWidget)
    }

    private fun renderTooltip(text: String, mouseX: Int, mouseY: Int, context: DrawContext?) {
        val tooltipLines: MutableList<Text> = mutableListOf()
        val descTooltip: Collection<Text> = TextWrapUtils.wrapText(width, text, Formatting.GRAY) as Collection<Text>

        tooltipLines.addFirst(Text.translatable(this.currentHex).formatted(Formatting.RED).formatted(Formatting.BOLD))
        tooltipLines.addAll(descTooltip)

        if (ScreenHelper.isInButtonBounds(mouseX, mouseY, x + 47, y + 51, 16, 16)) {
            context?.drawTooltip(textRenderer, tooltipLines, mouseX, mouseY)
        }
    }
}