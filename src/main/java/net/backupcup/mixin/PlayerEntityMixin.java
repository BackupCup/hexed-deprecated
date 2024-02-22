package net.backupcup.mixin;


import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    @Shadow public abstract Iterable<ItemStack> getHandItems();

    @Shadow @Final private ItemCooldownManager itemCooldownManager;

    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    private void hexed$TraitorousCancelAttack(Entity target, CallbackInfo ci) {
        for (ItemStack itemStack: getHandItems()) {
            if (itemCooldownManager.isCoolingDown(itemStack.getItem())) {
                ci.cancel();
                break;
            }
        }
    }
}
