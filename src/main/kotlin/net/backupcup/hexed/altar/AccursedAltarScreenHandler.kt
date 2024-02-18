package net.backupcup.hexed.altar

import net.backupcup.hexed.enchantments.AbstractHex
import net.backupcup.hexed.packets.AltarNetworkingConstants
import net.backupcup.hexed.register.RegisterItems
import net.backupcup.hexed.register.RegisterScreenHandlers
import net.backupcup.hexed.register.RegisterSounds
import net.backupcup.hexed.util.HexHelper
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.Property
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.screen.ScreenHandlerListener
import net.minecraft.screen.slot.Slot
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class AccursedAltarScreenHandler(
    syncId: Int,
    playerInventory: PlayerInventory,
    player: PlayerEntity,
    handlerContext: ScreenHandlerContext,
    val blockEntity: AccursedAltarBlockEntity?
) : ScreenHandler(RegisterScreenHandlers.ACCURSED_ALTAR_SCREEN_HANDLER, syncId), ScreenHandlerListener {
    private val playerEntity: PlayerEntity
    private var inventory: Inventory = SimpleInventory(2)
    private val context: ScreenHandlerContext


    private var availableHexList: List<Enchantment>
    fun getAvailableHexList(): List<Enchantment> { return this.availableHexList }
    fun setAvailableHexList(list: List<Enchantment>) { this.availableHexList = list }


    private lateinit var currentHex: Enchantment
    fun getCurrentHex(): Enchantment { return this.currentHex }
    fun setCurrentHex(enchantment: Enchantment) { this.currentHex = enchantment }

    val isActive: Property = Property.create()

    val BUTTON_HEX: Int = 0
    val BUTTON_SCROLL_UP: Int = 1
    val BUTTON_SCROLL_DOWN: Int = 2

    constructor(syncId: Int, playerInventory: PlayerInventory) :
            this(syncId, playerInventory, playerInventory.player, ScreenHandlerContext.EMPTY, null)

    init {
        this.isActive.set(if (this.blockEntity?.getActiveState() == true) 1 else 0)

        checkSize(inventory, 1)

        this.context = handlerContext
        this.availableHexList = listOf()
        this.playerEntity = player

        inventory.onOpen(player)

        this.addSlot(object: Slot(inventory, 0, 18, 20) {
            override fun canInsert(stack: ItemStack): Boolean {
                return stack.isOf(RegisterItems.BRIMSTONE_CRYSTAL)
            }
        })

        this.addSlot(object: Slot(inventory, 1, 18, 82) {
            override fun canInsert(stack: ItemStack): Boolean {
                return HexHelper.getAvailableHexList(stack).isNotEmpty()
            }
        })

        for (m in 0 until 3) {
            for (l in 0 until 9) {
                this.addSlot(Slot(playerInventory, l + m * 9 + 9, 18 + l * 18, 118 + m * 18))
            }
        }
        for (m in 0 until 9) {
            this.addSlot(Slot(playerInventory, m, 18 + m * 18, 176))
        }

        addListener(HexHelper.generatorListener(handlerContext, player))
        addListener(this)
        addProperty(this.isActive)
        sendContentUpdates()
    }

    override fun onButtonClick(player: PlayerEntity, id: Int): Boolean {
        val item = getGearSlot()
        val currentHexIndex = availableHexList.indexOf(currentHex)
        when(id) {
            BUTTON_HEX -> {
                if (HexHelper.getEnchantments(item).filterIsInstance<AbstractHex>().isNotEmpty()) {
                    val enchantmentMap = EnchantmentHelper.get(item).filterKeys { enchantment: Enchantment? ->
                        enchantment !is AbstractHex
                    }.toMutableMap()
                    EnchantmentHelper.set(enchantmentMap, item)
                }

                if (!player.isCreative) {
                    getMaterialSlot().decrement(1)
                }
                item.addEnchantment(this.currentHex, 1)

                this.playerEntity.playSound(
                    RegisterSounds.ACCURSED_ALTAR_HEX,
                    SoundCategory.BLOCKS, 1.25f, 1f)

                blockEntity?.changeCandleState(false)

                playerEntity.sendMessage(Text.translatable("message.hexed.altar_used")
                    .formatted(Formatting.RED).formatted(Formatting.BOLD).formatted(Formatting.ITALIC), true)

                sendHexPacket()
                sendActivePacket()
            }
            BUTTON_SCROLL_UP -> {
                this.isActive.set(if (this.blockEntity?.getActiveState() == true) 1 else 0)

                currentHex = if (currentHexIndex - 1 >= 0)
                    { availableHexList[currentHexIndex - 1] }
                else
                    { availableHexList[availableHexList.size - 1] }

                sendActivePacket()
                sendHexPacket()
            }
            BUTTON_SCROLL_DOWN -> {
                this.isActive.set(if (this.blockEntity?.getActiveState() == true) 1 else 0)

                currentHex = if (currentHexIndex + 1 < availableHexList.size)
                    { availableHexList[currentHexIndex + 1] }
                else
                    { availableHexList[0] }

                sendActivePacket()
                sendHexPacket()
            }
        }

        return super.onButtonClick(player, id)
    }

    override fun onSlotUpdate(handler: ScreenHandler?, slotId: Int, stack: ItemStack?) {
        val item = getGearSlot()
        this.isActive.set(if (this.blockEntity?.getActiveState() == true) 1 else 0)
        if (HexHelper.getAvailableHexList(item).isNotEmpty()) {
            this.availableHexList = HexHelper.getAvailableHexList(item)
            this.currentHex = availableHexList[0]

            sendActivePacket()
            sendHexPacket()
        }
    }

    override fun onPropertyUpdate(handler: ScreenHandler?, property: Int, value: Int) {
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

    private fun sendHexPacket() {
        val buf = PacketByteBufs.create()
        buf.writeInt(this.availableHexList.size)
        this.availableHexList.forEach { hex -> buf.writeString(hex.translationKey) }
        buf.writeString(this.currentHex.translationKey)

        ServerPlayNetworking.send(this.playerEntity.world.server?.playerManager?.getPlayer(this.playerEntity.uuid), AltarNetworkingConstants.AVAILABLE_HEX_PACKET, buf)
    }

    private fun sendActivePacket() {
        val buf = PacketByteBufs.create()
        buf.writeInt(this.isActive.get())

        ServerPlayNetworking.send(getServerPlayer(), AltarNetworkingConstants.ACTIVE_ALTAR_PACKET, buf)
    }

    fun getMaterialSlot(): ItemStack {
        return this.inventory.getStack(0)
    }

    fun getGearSlot(): ItemStack {
        return this.inventory.getStack(1)
    }

    private fun getServerPlayer(): ServerPlayerEntity? {
        return this.playerEntity.world.server?.playerManager?.getPlayer(playerEntity.uuid)
    }


    /*
    TODO LIST:
    2. make the scroll thing that i described to chronos [MAYBE? Im not sure really, could be more difficult to do that in the end]
    8. more hexes
     */
}