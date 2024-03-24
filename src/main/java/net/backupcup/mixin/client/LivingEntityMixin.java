package net.backupcup.mixin.client;

import net.backupcup.hexed.register.RegisterEnchantments;
import net.backupcup.hexed.util.HexHelper;
import net.backupcup.hexed.util.ItemUseCooldown;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Unique
    private boolean holdingCharged = false;

    @Inject(method = "tickItemStackUsage", at = @At("HEAD"))
    private void hexed$CelebrationAutoFire(ItemStack stack, CallbackInfo ci) {
        var entity = (LivingEntity) (Object) this;
        var charged = false;

        if (entity.getWorld().isClient()) {
            if (entity == MinecraftClient.getInstance().player) {
                var player = MinecraftClient.getInstance().player;
                var mainHandStack = player.getMainHandStack();

                if(HexHelper.INSTANCE.hasEnchantmentInSlot(mainHandStack, RegisterEnchantments.INSTANCE.getCELEBRATION_HEX())) {
                    var predicate = ModelPredicateProviderRegistry.get(mainHandStack.getItem(), new Identifier("pull"));
                    if (predicate != null) {
                        if (predicate.call(mainHandStack, (ClientWorld) entity.getWorld(), player, 1234) >= 1) {
                            charged = true;
                            if (holdingCharged) {
                                MinecraftClient.getInstance().interactionManager.stopUsingItem(player);
                                ((ItemUseCooldown) MinecraftClient.getInstance()).imposeItemUseCooldown(2);
                            }
                        }
                    }
                }
            }
        }
        holdingCharged = charged || holdingCharged;
    }
}
