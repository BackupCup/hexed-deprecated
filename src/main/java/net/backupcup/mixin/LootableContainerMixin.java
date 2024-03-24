package net.backupcup.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.backupcup.hexed.Hexed;
import net.backupcup.hexed.register.RegisterEnchantments;
import net.backupcup.hexed.register.RegisterStatusEffects;
import net.backupcup.hexed.register.RegisterTags;
import net.backupcup.hexed.util.HexHelper;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = LootableContainerBlockEntity.class, priority = 10)
public class LootableContainerMixin {
    @Shadow protected long lootTableSeed;

    @Inject(method = "checkLootInteraction", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/loot/context/LootContextParameterSet$Builder;luck(F)Lnet/minecraft/loot/context/LootContextParameterSet$Builder;",
        shift = At.Shift.AFTER))
    private void hexed$DivineLoot(PlayerEntity player, CallbackInfo ci, @Local LootContextParameterSet.Builder builder, @Local LootTable lootTable) {
        if (EnchantmentHelper.get(player.getEquippedStack(EquipmentSlot.HEAD)).containsKey(RegisterEnchantments.INSTANCE.getDIVINE_HEX())) {
            lootTable.supplyInventory((Inventory)this, builder.build(LootContextTypes.CHEST), lootTableSeed);

            List<StatusEffect> debuffList = new ArrayList<>();
            debuffList.add(RegisterStatusEffects.INSTANCE.getABLAZE());
            debuffList.add(RegisterStatusEffects.INSTANCE.getETHEREAL());
            debuffList.add(RegisterStatusEffects.INSTANCE.getEXHAUSTION());
            debuffList.add(RegisterStatusEffects.INSTANCE.getIRONCLAD());
            debuffList.add(RegisterStatusEffects.INSTANCE.getTRAITOROUS());

            StatusEffect randomEffect = debuffList.get((int)(Math.random() * debuffList.size()));
            int amplifier = Hexed.INSTANCE.getConfig() != null ? Hexed.INSTANCE.getConfig().getDivineHex().getDefaultAmplifier() : 0;
            int durationModifier = Hexed.INSTANCE.getConfig() != null ? Hexed.INSTANCE.getConfig().getDivineHex().getDefaultDuration() : 20;

            if (randomEffect.equals(RegisterStatusEffects.INSTANCE.getIRONCLAD())) {
                amplifier = Hexed.INSTANCE.getConfig() != null ? Hexed.INSTANCE.getConfig().getDivineHex().getIroncladAmplifier() : 3;
                durationModifier = Hexed.INSTANCE.getConfig() != null ? Hexed.INSTANCE.getConfig().getDivineHex().getIroncladDuration() : 40;}
            else if (randomEffect.equals(RegisterStatusEffects.INSTANCE.getEXHAUSTION())) {
                amplifier = Hexed.INSTANCE.getConfig() != null ? Hexed.INSTANCE.getConfig().getDivineHex().getExhaustionAmplifier() : 9;
                durationModifier = Hexed.INSTANCE.getConfig() != null ? Hexed.INSTANCE.getConfig().getDivineHex().getExhaustionDuration() : 60;}
            else if (randomEffect.equals(RegisterStatusEffects.INSTANCE.getETHEREAL())) {
                amplifier = Hexed.INSTANCE.getConfig() != null ? Hexed.INSTANCE.getConfig().getDivineHex().getEtherealAmplifier() : 5;
                durationModifier = Hexed.INSTANCE.getConfig() != null ? Hexed.INSTANCE.getConfig().getDivineHex().getEtherealDuration() : 80;}

            if (HexHelper.INSTANCE.hasFullRobes(player.getArmorItems())) durationModifier /= 2;

            player.addStatusEffect(new StatusEffectInstance(
                    randomEffect,
                    20 * durationModifier, amplifier,
                    true, false, true)
            );
        }
    }
}
