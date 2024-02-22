package net.backupcup.mixin;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.backupcup.hexed.util.AttributeProviding;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow public abstract Item getItem();

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
}
