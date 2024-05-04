package net.backupcup.hexed.armor

import net.backupcup.hexed.Hexed
import net.backupcup.hexed.register.RegisterItems
import net.minecraft.item.ArmorItem
import net.minecraft.item.ArmorMaterial
import net.minecraft.recipe.Ingredient
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import java.util.function.Supplier

enum class HexedArmorMaterial(
    name: String,
    private val durabilityMultiplier: Int,
    private val protectionAmounts: IntArray,
    private val enchantability: Int,
    private val equipSound: SoundEvent,
    private val toughness: Float,
    private val knockbackResistance: Float,
    private val repairIngredient: Supplier<Ingredient>
) : ArmorMaterial {
    calamitous(
        "calamitous", 15, intArrayOf(1, 4, 5, 2), 25,
        SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0f, 0.0f, Supplier {
            Ingredient.ofItems(RegisterItems.CALAMITOUS_FABRIC)
        }
    );

    companion object {
        private val BASE_DURABILITY = intArrayOf(11, 16, 15, 13)
    }

    override fun getDurability(type: ArmorItem.Type?): Int {
        return BASE_DURABILITY[type!!.ordinal] * durabilityMultiplier
    }

    override fun getProtection(type: ArmorItem.Type?): Int{
        return protectionAmounts[type!!.ordinal]
    }

    override fun getEnchantability(): Int{
        return enchantability
    }

    override fun getEquipSound(): SoundEvent? {
        return equipSound
    }

    override fun getRepairIngredient(): Ingredient? {
        return repairIngredient.get()
    }

    override fun getName(): String? {
        return Hexed.MOD_ID + ":" + name
    }

    override fun getToughness(): Float {
        return toughness
    }

    override fun getKnockbackResistance(): Float {
        return knockbackResistance
    }
}