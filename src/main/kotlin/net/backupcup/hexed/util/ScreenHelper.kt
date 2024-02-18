package net.backupcup.hexed.util

import net.minecraft.client.gui.widget.TexturedButtonWidget

object ScreenHelper {
    fun isInButtonBounds(mouseX: Int, mouseY: Int, startX: Int, startY: Int, width: Int, height: Int): Boolean {
        return mouseX in startX..startX+width && mouseY in startY..startY+height
    }

    fun isFocused(mouseX: Int, mouseY: Int, x: Int, y: Int, width: Int, height: Int, buttonWidget: TexturedButtonWidget): Boolean {
        return isInButtonBounds(mouseX, mouseY, x, y, width, height) && buttonWidget.active
    }
}