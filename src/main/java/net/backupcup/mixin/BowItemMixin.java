package net.backupcup.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.backupcup.hexed.register.RegisterEnchantments;
import net.backupcup.hexed.util.HexHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.world.World;
import org.apache.commons.codec.binary.Hex;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BowItem.class)
public class BowItemMixin {
    @Inject(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getProjectileType(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;", shift = At.Shift.AFTER))
    private void hexed$TransmutationArrows(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci, @Local LocalRef<ItemStack> arrowItem) {
        if (HexHelper.INSTANCE.hasEnchantmentInSlot(stack, RegisterEnchantments.INSTANCE.getTRANSMUTATION_HEX())) {
            System.out.println(arrowItem.get());
        }
    }
}
