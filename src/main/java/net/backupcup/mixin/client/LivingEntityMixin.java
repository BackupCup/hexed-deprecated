package net.backupcup.mixin.client;

import io.netty.buffer.Unpooled;
import net.backupcup.hexed.packets.HexNetworkingConstants;
import net.backupcup.hexed.register.RegisterEnchantments;
import net.backupcup.hexed.util.HexHelper;
import net.backupcup.hexed.util.ItemUseCooldown;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Unique
    private boolean holdingCharged = false;

    @Unique
    private float storedPredicate = 1f;

    @Inject(method = "tickItemStackUsage", at = @At("HEAD"))
    private void hexed$CrossbowAutoFire(ItemStack stack, CallbackInfo ci) {
        var entity = (LivingEntity) (Object) this;
        var charged = false;

        if (entity.getWorld().isClient()) {
            if (entity == MinecraftClient.getInstance().player) {
                var player = MinecraftClient.getInstance().player;
                List<ItemStack> handStacks = new ArrayList<>(); handStacks.add(player.getMainHandStack()); handStacks.add(player.getOffHandStack());

                List<Enchantment> autofireHexes = new ArrayList<>();
                    autofireHexes.add(RegisterEnchantments.INSTANCE.getCELEBRATION_HEX());
                    autofireHexes.add(RegisterEnchantments.INSTANCE.getOVERCLOCK_HEX());

                if(HexHelper.INSTANCE.stackHasEnchantment(handStacks, autofireHexes)) {

                    var usedStack = ModelPredicateProviderRegistry.get(handStacks.get(0).getItem(), new Identifier("pull")) != null ?
                            handStacks.get(0) : handStacks.get(1);
                    var predicate = ModelPredicateProviderRegistry.get(usedStack.getItem(), new Identifier("pull"));

                    if (predicate != null) {
                        if (predicate.call(usedStack, (ClientWorld) entity.getWorld(), player, 1234) >= 1) {
                            charged = true;
                            if (holdingCharged && MinecraftClient.getInstance().interactionManager != null) {
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

    @Inject(method = "tickActiveItemStack", at = @At("HEAD"))
    private void hexed$predicateC2SPacket(CallbackInfo ci) {
        var entity = (LivingEntity) (Object) this;
        if (entity.getWorld().isClient()) {
            if (entity == MinecraftClient.getInstance().player) {
                var player = MinecraftClient.getInstance().player;

                List<ItemStack> handStacks = new ArrayList<>(); handStacks.add(player.getMainHandStack()); handStacks.add(player.getOffHandStack());

                List<Enchantment> chargeHexes = new ArrayList<>();
                    chargeHexes.add(RegisterEnchantments.INSTANCE.getAGGRAVATE_HEX());
                    chargeHexes.add(RegisterEnchantments.INSTANCE.getOVERCLOCK_HEX());
                    chargeHexes.add(RegisterEnchantments.INSTANCE.getPHASED_HEX());
                    chargeHexes.add(RegisterEnchantments.INSTANCE.getPROVISION_HEX());
                    chargeHexes.add(RegisterEnchantments.INSTANCE.getVOLATILITY_HEX());

                if (!HexHelper.INSTANCE.stackHasEnchantment(handStacks.stream().toList(), chargeHexes)) {
                    return;
                }

                var usedStack = ModelPredicateProviderRegistry.get(handStacks.get(0).getItem(), new Identifier("pull")) != null ?
                        handStacks.get(0) : handStacks.get(1);
                var predicate = ModelPredicateProviderRegistry.get(usedStack.getItem(), new Identifier("pull"));

                if (player.getEntityWorld().getTime() % 5 == 0 && storedPredicate != (predicate != null ? predicate.call(usedStack, (ClientWorld) entity.getWorld(), player, 1234) : 0f)) {
                    var pullStrength = predicate != null ? predicate.call(usedStack, (ClientWorld) entity.getWorld(), player, 1234) : 0f;
                    storedPredicate = pullStrength;

                    PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                    buf.writeFloat(pullStrength);

                    ClientPlayNetworking.send(HexNetworkingConstants.INSTANCE.getPREDICATE_GETTER_PACKET(), buf);
                }
            }
        }
    }
}
