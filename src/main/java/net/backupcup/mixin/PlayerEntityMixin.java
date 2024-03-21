package net.backupcup.mixin;


import io.netty.buffer.Unpooled;
import net.backupcup.hexed.Hexed;
import net.backupcup.hexed.register.RegisterEnchantments;
import net.backupcup.hexed.register.RegisterStatusEffects;
import net.backupcup.hexed.util.HexHelper;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Mixin(value = PlayerEntity.class, priority = 10)
public abstract class PlayerEntityMixin extends Entity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow public abstract Iterable<ItemStack> getHandItems();

    @Shadow @Final private ItemCooldownManager itemCooldownManager;

    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Shadow public abstract Iterable<ItemStack> getArmorItems();

    @Shadow protected abstract void takeShieldHit(LivingEntity attacker);

    @Unique private boolean provisionUI = true;
    @Unique private int provisionIndicatorPos = 0;
    @Unique private int provisionBuffAmplifier = 0;
    @Unique private int provisionBuffTime = 0;

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

    @ModifyVariable(method = "getBlockBreakingSpeed", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;hasStatusEffect(Lnet/minecraft/entity/effect/StatusEffect;)Z", shift = At.Shift.BEFORE), ordinal = 0)
    private float hexed$OverburdenBreakSpeed(float f) {
        PlayerEntity entity = (PlayerEntity) (Object) this;
        if (entity.hasStatusEffect(RegisterStatusEffects.INSTANCE.getOVERBURDEN())) {
            return f *= 1.0f + entity.getStatusEffect(RegisterStatusEffects.INSTANCE.getOVERBURDEN()).getAmplifier() * 0.025;
        }
        return f;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void hexed$ProvisionTick(CallbackInfo ci) {
        if (this.provisionUI) {
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            buf.writeBoolean(this.provisionUI);
            buf.writeInt(this.provisionIndicatorPos);

            ServerPlayerEntity player = ((PlayerEntity)(Object)this).getWorld().getServer().getPlayerManager().getPlayer(getUuid());

            System.out.println(player);
            /*
            ServerPlayNetworking.send(
                    player,
                    new Identifier(Hexed.MOD_ID, "provision_update_packet"), buf);

             */
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getLastDeathPos()Ljava/util/Optional;", shift = At.Shift.AFTER))
    private void hexed$ProvisionWriteData(NbtCompound nbt, CallbackInfo ci) {
        NbtCompound provisionNbt = new NbtCompound();
        provisionNbt.putBoolean("isUIOpen", provisionUI);
        provisionNbt.putInt("IndicatorPos", provisionIndicatorPos);
        provisionNbt.putInt("BuffAmplifier", provisionBuffAmplifier);
        provisionNbt.putInt("BuffTime", provisionBuffTime);

        nbt.put("HexedProvision", provisionNbt);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeInstance(Lnet/minecraft/entity/attribute/EntityAttribute;)Lnet/minecraft/entity/attribute/EntityAttributeInstance;", shift = At.Shift.AFTER))
    private void hexed$ProvisionReadData(NbtCompound nbt, CallbackInfo ci) {
        NbtCompound provisionNbt = nbt.getCompound("HexedProvision");

        provisionUI = provisionNbt.getBoolean("isUIOpen");
        provisionIndicatorPos = provisionNbt.getInt("IndicatorPos");
        provisionBuffAmplifier = provisionNbt.getInt("BuffAmplifier");
        provisionBuffTime = provisionNbt.getInt("BuffTime");
    }
}
