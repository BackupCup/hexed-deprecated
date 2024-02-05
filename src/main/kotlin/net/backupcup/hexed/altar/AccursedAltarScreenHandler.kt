package net.backupcup.hexed.altar

import net.backupcup.hexed.register.RegisterScreenHandlers
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.ArrayPropertyDelegate
import net.minecraft.screen.PropertyDelegate
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.slot.Slot

class AccursedAltarScreenHandler(
    syncId: Int,
    playerInventory: PlayerInventory,
    player: PlayerEntity?,
    propertyDelegate: PropertyDelegate
) : ScreenHandler(RegisterScreenHandlers.ACCURSED_ALTAR_SCREEN_HANDLER, syncId) {
    private var inventory: Inventory = SimpleInventory(2)
    private var propertyDelegate: PropertyDelegate

    constructor(syncId: Int, playerInventory: PlayerInventory) :
            this(syncId, playerInventory, playerInventory.player, ArrayPropertyDelegate(1))

    init {
        checkSize(inventory, 1)

        this.propertyDelegate = propertyDelegate

        inventory.onOpen(player)

        this.addSlot(Slot(inventory, 0, 18, 20))
        this.addSlot(Slot(inventory, 1, 18, 82))

        for (m in 0 until 3) {
            for (l in 0 until 9) {
                this.addSlot(Slot(playerInventory, l + m * 9 + 9, 18 + l * 18, 118 + m * 18))
            }
        }
        for (m in 0 until 9) {
            this.addSlot(Slot(playerInventory, m, 18 + m * 18, 176))
        }

        this.addProperties(propertyDelegate)
    }

    override fun quickMove(player: PlayerEntity?, invSlot: Int): ItemStack {
        var newStack: ItemStack = ItemStack.EMPTY
        val slot: Slot = this.slots[invSlot]

        if (slot.hasStack()) {
            val originalStack: ItemStack = slot.stack
            newStack = originalStack.copy()
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size, true)) {
                    return ItemStack.EMPTY
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY
            }

            if (originalStack.isEmpty) {
                slot.stack = ItemStack.EMPTY
            } else {
                slot.markDirty()
            }
        }

        return newStack
    }

    override fun onClosed(player: PlayerEntity?) {
        super.onClosed(player)
        dropInventory(player, inventory)
    }

    override fun canUse(player: PlayerEntity?): Boolean {
        return inventory.canPlayerUse(player)
    }
}