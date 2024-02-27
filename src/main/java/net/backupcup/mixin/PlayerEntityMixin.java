package net.backupcup.mixin;


import net.backupcup.hexed.register.RegisterEnchantments;
import net.backupcup.hexed.util.HexHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PlayerEntity.class, priority = 10)
public abstract class PlayerEntityMixin {

    @Shadow public abstract Iterable<ItemStack> getHandItems();

    @Shadow @Final private ItemCooldownManager itemCooldownManager;

    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Shadow public abstract Iterable<ItemStack> getArmorItems();

    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    private void hexed$TraitorousCancelAttack(Entity target, CallbackInfo ci) {
        for (ItemStack itemStack: getHandItems()) {
            if (itemCooldownManager.isCoolingDown(itemStack.getItem())) {
                ci.cancel();
                break;
            }
        }
    }

    @Inject(method = "canFoodHeal", at = @At("HEAD"), cancellable = true)
    private void hexed$MetamorphosisNaturalRegen(CallbackInfoReturnable<Boolean> cir) {
        if (HexHelper.INSTANCE.hasEnchantmentInSlot(getEquippedStack(EquipmentSlot.HEAD), RegisterEnchantments.INSTANCE.getMETAMORPHOSIS_HEX()) && !HexHelper.INSTANCE.hasFullRobes(getArmorItems())) {
            cir.setReturnValue(false);
        }
    }
}
