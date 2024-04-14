package net.backupcup.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.netty.buffer.Unpooled;
import kotlin.random.Random;
import net.backupcup.hexed.Hexed;
import net.backupcup.hexed.packets.HexNetworkingConstants;
import net.backupcup.hexed.register.RegisterEnchantments;
import net.backupcup.hexed.register.RegisterSounds;
import net.backupcup.hexed.register.RegisterStatusEffects;
import net.backupcup.hexed.util.*;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.advancement.Advancement;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin {
    @Shadow
    private static void putProjectile(ItemStack crossbow, ItemStack projectile) {}

    @Shadow
    private static boolean loadProjectile(LivingEntity shooter, ItemStack crossbow, ItemStack projectile, boolean simulated, boolean creative)
    {return true;}

    @Shadow
    public static void setCharged(ItemStack stack, boolean charged) {}

    @WrapOperation(method = "loadProjectile", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/CrossbowItem;putProjectile(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)V"))
    private static void hexed$CelebrationFirework(ItemStack crossbow, ItemStack projectile, Operation<Void> original) {
        if (HexHelper.INSTANCE.stackHasEnchantment(crossbow, RegisterEnchantments.INSTANCE.getCELEBRATION_HEX())) {
            ItemStack rocket = new ItemStack(Items.FIREWORK_ROCKET);

            NbtCompound tag = new NbtCompound();
            NbtCompound fireworks = new NbtCompound();
            NbtList explosionsList = new NbtList();
            NbtCompound explosion = new NbtCompound();

            for (int i = 0; i < Random.Default.nextInt(1, 7); i++) {
                List<Integer> fullColorList = new ArrayList<>(List.of(11743532, 14602026, 15435844, 1743258066, -510577374, -1222464768, -709004448));
                List<Integer> colorList = new ArrayList<>();

                Collections.shuffle(fullColorList);
                int itemsToSelect = Math.min(Random.Default.nextInt(1, 4), fullColorList.size());

                for (int colorIndex = 0; colorIndex < itemsToSelect; colorIndex++) {colorList.add(fullColorList.get(colorIndex));}

                explosion.putByte("Type", (byte) Random.Default.nextInt(1, 4));
                explosion.putIntArray("Colors", colorList);
                explosionsList.add(explosion);
            }

            fireworks.put("Explosions", explosionsList);
            fireworks.putByte("Flight", (byte) Random.Default.nextInt(0, 2));


            tag.put("Fireworks", fireworks);
            rocket.setNbt(tag);

            putProjectile(crossbow, rocket);
        } else {
            original.call(crossbow, projectile);
        }
    }

    @ModifyArgs(method = "shootAll", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/CrossbowItem;shoot(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;FZFFF)V"))
    private static void hexed$CelebrationAccuracy(Args args) {
        ItemStack crossbow = args.get(3);
        LivingEntity entity = args.get(1);
        if (HexHelper.INSTANCE.stackHasEnchantment(crossbow, RegisterEnchantments.INSTANCE.getCELEBRATION_HEX())) {
            float speed = args.get(7);
            float divergence = args.get(9);

            args.set(7,
                    Random.Default.nextDouble(0, 1) <= (Hexed.INSTANCE.getConfig() != null ? Hexed.INSTANCE.getConfig().getCelebrationHex().getFaulyChance() : 0.333f)
                            && !HexHelper.INSTANCE.hasFullRobes(entity) ?
                            (Hexed.INSTANCE.getConfig() != null ? Hexed.INSTANCE.getConfig().getCelebrationHex().getFaultySpeed() : 0.0625f) :
                            speed + (float) Random.Default.nextDouble(-0.75, 0));
            args.set(9, divergence + (float) Random.Default.nextDouble(
                    (Hexed.INSTANCE.getConfig() != null ? Hexed.INSTANCE.getConfig().getCelebrationHex().getMinimumDivergence() : -5),
                    (Hexed.INSTANCE.getConfig() != null ? Hexed.INSTANCE.getConfig().getCelebrationHex().getMaximumDivergence() : 5)
            ));
        }
    }

    @Inject(method = "postShoot", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/CrossbowItem;clearProjectiles(Lnet/minecraft/item/ItemStack;)V", shift = At.Shift.AFTER))
    private static void hexed$ProvisionTrigger(World world, LivingEntity entity, ItemStack stack, CallbackInfo ci) {
        if (HexHelper.INSTANCE.stackHasEnchantment(stack, RegisterEnchantments.INSTANCE.getPROVISION_HEX())) {
            if (!(entity instanceof ServerPlayerEntity)) return;

            ProvisionData data = ((ProvisionInterface)entity).getProvisionData();
            ((ProvisionInterface)entity).setProvisionData(new ProvisionData(true, 0, data.getReloadSpeed(), 0));
        }
    }

    @Inject(method = "onStoppedUsing", at = @At("HEAD"))
    private void hexed$ProvisionReload(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
        if (!(user instanceof ServerPlayerEntity)) return;
        ProvisionData data = ((ProvisionInterface)user).getProvisionData();

        if (HexHelper.INSTANCE.stackHasEnchantment(stack, RegisterEnchantments.INSTANCE.getPROVISION_HEX()) && data.isUIActive()) {
            ServerPlayerEntity player = (ServerPlayerEntity) user;

            if (data.getIndicatorPos() >= 17 && data.getIndicatorPos() <= 35) {
                loadProjectile(user, stack, new ItemStack(Items.ARROW), true, true);
                setCharged(stack, true);
                int newReloadSpeed = data.getReloadSpeed() < 5 ? data.getReloadSpeed() + 1 : data.getReloadSpeed();

                player.playSound(RegisterSounds.INSTANCE.getPROVISION_CHARGE(), SoundCategory.PLAYERS, 0.5f, 0.75f);
                ((ProvisionInterface)player).setProvisionData(new ProvisionData(false, 0, newReloadSpeed, 0));

            } else if ((data.getIndicatorPos() >= 10 && data.getIndicatorPos() <= 16) || (data.getIndicatorPos() >= 36 && data.getIndicatorPos() <= 43)) {
                loadProjectile(user, stack, new ItemStack(Items.ARROW), true, true);
                setCharged(stack, true);

                player.playSound(RegisterSounds.INSTANCE.getPROVISION_CHARGE(), SoundCategory.PLAYERS, 0.5f, 1.25f);
                ((ProvisionInterface)player).setProvisionData(new ProvisionData(false, 0, data.getReloadSpeed(), 0));
            } else {
                if(!HexHelper.INSTANCE.hasFullRobes(player)) {
                    player.getItemCooldownManager().set(Items.CROSSBOW, 80);
                }

                player.playSound(RegisterSounds.INSTANCE.getPROVISION_FAIL(), SoundCategory.PLAYERS, 1f, 1f);
                ((ProvisionInterface)player).setProvisionData(new ProvisionData(false, 0, 1, 0));
            }

            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            buf.writeBoolean(false);
            buf.writeInt(0);

            ServerPlayNetworking.send(player, HexNetworkingConstants.INSTANCE.getPROVISION_UPDATE_PACKET(), buf);
        }
    }

    @Inject(method = "postShoot", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/CrossbowItem;clearProjectiles(Lnet/minecraft/item/ItemStack;)V", shift = At.Shift.AFTER))
    private static void hexed$OverclockTrigger(World world, LivingEntity entity, ItemStack stack, CallbackInfo ci) {
        if (HexHelper.INSTANCE.stackHasEnchantment(stack, RegisterEnchantments.INSTANCE.getOVERCLOCK_HEX())) {
            if (!(entity instanceof ServerPlayerEntity)) return;

            int overclockCharge = ((OverclockInterface)entity).getOverclockData().getOverheat();
            NbtCompound stackNbt = stack.getNbt();

            if(overclockCharge < 20) overclockCharge++;
            ((OverclockInterface)entity).setOverclockData(new OverclockData(200, overclockCharge));

            stackNbt.putInt("Overheat", overclockCharge);
            stack.setNbt(stackNbt);

            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            buf.writeInt(overclockCharge);

            ServerPlayNetworking.send((ServerPlayerEntity) entity, HexNetworkingConstants.INSTANCE.getOVERCLOCK_UPDATE_PACKET(), buf);

            if (overclockCharge > 14) { entity.setOnFireFor(overclockCharge); }
            if (overclockCharge > 16) { entity.addStatusEffect(new StatusEffectInstance(
                    RegisterStatusEffects.INSTANCE.getABLAZE(), overclockCharge * 20, 0,
                            false, false, false)); }

            List<Integer> pingList = new ArrayList<>();
            pingList.add(4); pingList.add(8); pingList.add(11);pingList.add(14);
            pingList.add(16); pingList.add(18); pingList.add(19); pingList.add(20);

            if (pingList.contains(overclockCharge))
                ((ServerPlayerEntity)entity).playSound(RegisterSounds.INSTANCE.getOVERCLOCK_TIER(), SoundCategory.PLAYERS, 1f, 1f + overclockCharge / 20f);
        }
    }

    @ModifyArgs(method = "loadProjectiles", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/CrossbowItem;loadProjectile(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;ZZ)Z"))
    private static void hexed$OverclockFailedShot(Args args) {
        LivingEntity entity = args.get(0);
        ItemStack crossbow = args.get(1);
        ItemStack projectile = args.get(2);

        if (HexHelper.INSTANCE.stackHasEnchantment(crossbow, RegisterEnchantments.INSTANCE.getOVERCLOCK_HEX())) {
            if (!(entity instanceof ServerPlayerEntity)) return;

            if (((OverclockInterface)entity).getOverclockData().getOverheat() > 18) {
                projectile = Random.Default.nextDouble(0, 1) > 0.5 ? projectile : new ItemStack(Items.AIR);
                args.set(2, projectile);
            }
        }
    }

    @ModifyArgs(method = "shootAll", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/CrossbowItem;shoot(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;FZFFF)V"))
    private static void hexed$OverclockAccuracy(Args args) {
        LivingEntity entity = args.get(1);
        ItemStack crossbow = args.get(3);
        float simulated = args.get(9);

        if (HexHelper.INSTANCE.stackHasEnchantment(crossbow, RegisterEnchantments.INSTANCE.getOVERCLOCK_HEX())) {
            if (!(entity instanceof ServerPlayerEntity)) return;

            if (((OverclockInterface)entity).getOverclockData().getOverheat() > 16) {
                args.set(9, simulated + (float) Random.Default.nextDouble(-3, 3));
            }
        }
    }

    @Inject(method = "getPullTime", at = @At("RETURN"), cancellable = true)
    private static void hexed$OverclockPullTime(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        int quickChargeModifier = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack);
        int pullTime = 25 - 5 * quickChargeModifier;

        if (HexHelper.INSTANCE.stackHasEnchantment(stack, RegisterEnchantments.INSTANCE.getOVERCLOCK_HEX())) {
            pullTime -= stack.getNbt().contains("Overheat") ? stack.getNbt().getInt("Overheat") : 0;
            if (pullTime < 1) pullTime = 1;
        }

        cir.setReturnValue(pullTime);
    }
}