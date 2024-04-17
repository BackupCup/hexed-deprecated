package net.backupcup.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import io.netty.buffer.Unpooled;
import net.backupcup.hexed.packets.HexNetworkingConstants;
import net.backupcup.hexed.register.RegisterDamageTypes;
import net.backupcup.hexed.register.RegisterEnchantments;
import net.backupcup.hexed.register.RegisterSounds;
import net.backupcup.hexed.util.*;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.apache.commons.codec.binary.Hex;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

@Mixin(BowItem.class)
public abstract class BowItemMixin {
    @Inject(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V", shift = At.Shift.BEFORE))
    private void hexed$PullModifier(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci, @Local PersistentProjectileEntity projectile) {
        if (HexHelper.INSTANCE.stackHasEnchantment(stack, RegisterEnchantments.INSTANCE.getVOLATILITY_HEX())) {
            if (!(user instanceof ServerPlayerEntity)) return;

            float pullStrength = ((PredicateInterface) user).getPredicate();

            if (pullStrength >= 1 || HexHelper.INSTANCE.hasFullRobes(user)) {
                ((VolatilityInterface) projectile).setVolatility(true);
            } else if (projectile.getOwner() != null) {
                projectile.getEntityWorld().createExplosion(
                        null,
                        projectile.getOwner().getX(),
                        projectile.getOwner().getBoundingBox().getCenter().getY(),
                        projectile.getOwner().getZ(),
                        2f - pullStrength,
                        World.ExplosionSourceType.NONE);
            }
        }
    }

    @Inject(method = "use", at = @At("HEAD"))
    private void hexed$AggravatePassStack(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (HexHelper.INSTANCE.stackHasEnchantment(user.getStackInHand(hand), RegisterEnchantments.INSTANCE.getAGGRAVATE_HEX())) {
            if (!(user instanceof ServerPlayerEntity)) return;
            ((AggravateInterface)user).setAggravateData(new AggravateData(hand, 0, 0));
        }
    }

    @Inject(method = "onStoppedUsing", at = @At(value = "TAIL"))
    private void hexed$AggravateShoot(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
        if (HexHelper.INSTANCE.stackHasEnchantment(stack, RegisterEnchantments.INSTANCE.getAGGRAVATE_HEX())) {
            if (!(user instanceof ServerPlayerEntity)) return;

            int charge = ((AggravateInterface)user).getAggravateData().getChargeAmount();

            if(charge > 0) {
                if(charge == 7 && !HexHelper.INSTANCE.hasFullRobes(user)) {
                    user.getEntityWorld().createExplosion(null,
                            user.getX(), user.getBoundingBox().getCenter().getY(), user.getZ(),
                            2f, World.ExplosionSourceType.NONE);
                }

                for (int arrow = 0; arrow < charge; arrow++) {
                    ArrowItem arrowItem = (ArrowItem)(Items.ARROW);
                    PersistentProjectileEntity persistentProjectileEntity = arrowItem.createArrow(world, new ItemStack(Items.ARROW), user);
                    persistentProjectileEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 3.0f, 20.0f);

                    persistentProjectileEntity.setCritical(true);

                    int powerLevel = EnchantmentHelper.getLevel(Enchantments.POWER, stack);
                    if (powerLevel > 0) persistentProjectileEntity.setDamage(persistentProjectileEntity.getDamage() + (double)powerLevel * 0.5 + 0.5);
                    int punchLevel = EnchantmentHelper.getLevel(Enchantments.PUNCH, stack);
                    if (punchLevel > 0) persistentProjectileEntity.setPunch(punchLevel);
                    if (EnchantmentHelper.getLevel(Enchantments.FLAME, stack) > 0) persistentProjectileEntity.setOnFireFor(100);

                    stack.damage(1, user, p -> p.sendToolBreakStatus(user.getActiveHand()));

                    persistentProjectileEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                    world.spawnEntity(persistentProjectileEntity);
                }

                world.playSound(null,
                        user.getX(), user.getY(), user.getZ(),
                        RegisterSounds.INSTANCE.getACCURSED_ALTAR_TAINT(),
                        SoundCategory.PLAYERS, 0.25f - (1f/charge)/8, 2f);
            }

            ((AggravateInterface)user).setAggravateData(new AggravateData(Hand.MAIN_HAND, 0, 0));

            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            buf.writeInt(0);
            ServerPlayNetworking.send((ServerPlayerEntity) user, HexNetworkingConstants.INSTANCE.getAGGRAVATE_UPDATE_PACKET(), buf);
        }
    }

    @Inject(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getProjectileType(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;", shift = At.Shift.AFTER), cancellable = true)
    private void hexed$PhasedLogic(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
        if (HexHelper.INSTANCE.stackHasEnchantment(stack, RegisterEnchantments.INSTANCE.getPHASED_HEX())) {
            if (!(user instanceof ServerPlayerEntity)) return;

            float pullStrength = ((PredicateInterface) user).getPredicate();

            if (pullStrength >= 1) {
                int phasedCharge = ((PhasedInterface)user).getPhasedData();

                if (phasedCharge >= 4) {
                    phasedCharge = 0;
                    ((ServerPlayerEntity) user).playSound(
                            RegisterSounds.INSTANCE.getPHASED_SHOT(), SoundCategory.PLAYERS,
                            0.5f, 1f
                    );

                    if (!HexHelper.INSTANCE.hasFullRobes(user)) {
                        ((ServerPlayerEntity)user).getItemCooldownManager().set(stack.getItem(), 200);
                    }

                    //HITSCAN HERE
                    double distance = 64;
                    double stepSize = 0.5;

                    Vec3d start = user.getCameraPosVec(1.0f);
                    Vec3d direction = user.getRotationVec(1.0f);

                    List<LivingEntity> hitList = new ArrayList<>();

                    for (int i = 0; i < (int)(distance / stepSize); i++) {
                        double t = i * stepSize / distance;
                        Vec3d intermediatePoint = start.add(direction.multiply(distance * t));

                        if (!world.getBlockState(new BlockPos((int)intermediatePoint.getX(), (int)intermediatePoint.getY(), (int)intermediatePoint.getZ())).isAir() ||
                                intermediatePoint.getY() <= -64) break;

                        Box searchBox = new Box(new BlockPos((int)intermediatePoint.getX(), (int)intermediatePoint.getY(), (int)intermediatePoint.getZ())).expand(1);

                        List<LivingEntity> searchList = world.getNonSpectatingEntities(LivingEntity.class, searchBox);
                        if(!searchList.isEmpty()) for (LivingEntity livingEntity : searchList) {
                            if(!hitList.contains(livingEntity) && livingEntity != user) hitList.add(livingEntity);

                            float initialDamage = 5f + 1f * EnchantmentHelper.getLevel(Enchantments.FLAME, stack);

                            livingEntity.damage(livingEntity.getDamageSources().magic(), initialDamage * (1f + 0.4f * hitList.size()));
                            if (EnchantmentHelper.getLevel(Enchantments.FLAME, stack) > 0) livingEntity.setOnFireFor(5);
                        }

                        ((ServerPlayerEntity)user).getServerWorld().spawnParticles(
                                hitList.isEmpty() ? ParticleTypes.GLOW : ParticleTypes.SONIC_BOOM,
                                intermediatePoint.getX(), intermediatePoint.getY(), intermediatePoint.getZ(),
                                1, 0.0, 0.0, 0.0, 0
                        );
                    }

                    ci.cancel();
                } else {
                    phasedCharge += 1;
                    ((ServerPlayerEntity) user).playSound(
                            RegisterSounds.INSTANCE.getPHASED_TIER(), SoundCategory.PLAYERS,
                            0.5f, 1f + phasedCharge / 4f
                    );
                }

                ((PhasedInterface)user).setPhasedData(phasedCharge);

                PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                buf.writeInt(phasedCharge);

                ServerPlayNetworking.send((ServerPlayerEntity) user, HexNetworkingConstants.INSTANCE.getPHASED_UPDATE_PACKET(), buf);
            }
        }
    }
}