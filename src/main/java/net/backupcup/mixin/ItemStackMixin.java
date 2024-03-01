package net.backupcup.mixin;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.backupcup.hexed.enchantments.AbstractHex;
import net.backupcup.hexed.util.AttributeProviding;
import net.backupcup.hexed.util.HexHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ItemStack.class, priority = 10)
public abstract class ItemStackMixin {
    @Shadow public abstract Item getItem();

    @Shadow public abstract NbtList getEnchantments();

    @Shadow public abstract Text getName();

    @ModifyReturnValue(method = "getAttributeModifiers", at = @At("RETURN"))
    private Multimap<EntityAttribute, EntityAttributeModifier> hexed$DisplacedSwapAttributes(
            Multimap<EntityAttribute, EntityAttributeModifier> original, EquipmentSlot slot) {
        if (getItem() instanceof EnchantedBookItem) return original;
        if (getItem() instanceof Equipment equipment && equipment.getSlotType() != slot) return original;

        Multimap<EntityAttribute, EntityAttributeModifier> newMap = null;
        boolean modified = false;

        for (var entry: EnchantmentHelper.get((ItemStack) (Object) this).entrySet()) {
            if (entry.getKey() instanceof AttributeProviding attributeProvidingEnchant) {
                if (newMap == null) newMap = ArrayListMultimap.create(original);
                attributeProvidingEnchant.modifyAttributeMap(newMap, slot, entry.getValue());
                modified = true;
            }
        }

        return modified ? newMap : original;
    }

    @ModifyVariable(method = "getTooltip", at = @At("STORE"), ordinal = 0)
    private MutableText hexed$modifyTooltipFormat(MutableText text) {
        if (getEnchantments().toString().contains("id:\"hexed:")) {
            text = Text.empty().append(getName()).formatted(Formatting.BOLD, Formatting.DARK_RED);
        }
        return text;
    }
}
