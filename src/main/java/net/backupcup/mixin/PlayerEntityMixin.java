package net.backupcup.mixin;


import net.backupcup.hexed.Hexed;
import net.backupcup.hexed.register.RegisterEnchantments;
import net.backupcup.hexed.register.RegisterStatusEffects;
import net.backupcup.hexed.util.HexHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PlayerEntity.class, priority = 10)
public abstract class PlayerEntityMixin extends Entity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

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
        if (HexHelper.INSTANCE.stackHasEnchantment(getEquippedStack(EquipmentSlot.HEAD), RegisterEnchantments.INSTANCE.getMETAMORPHOSIS_HEX()) &&
                !HexHelper.INSTANCE.hasFullRobes(getArmorItems())) {
            cir.setReturnValue(false);
        }
    }

    @ModifyVariable(method = "getBlockBreakingSpeed", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;hasStatusEffect(Lnet/minecraft/entity/effect/StatusEffect;)Z", shift = At.Shift.BEFORE), ordinal = 0)
    private float hexed$OverburdenBreakSpeed(float f) {
        PlayerEntity entity = (PlayerEntity) (Object) this;
        if (entity.hasStatusEffect(RegisterStatusEffects.INSTANCE.getOVERBURDEN())) {
            return f *= 1.0f + entity.getStatusEffect(RegisterStatusEffects.INSTANCE.getOVERBURDEN()).getAmplifier() *
                    (Hexed.INSTANCE.getConfig() != null ? Hexed.INSTANCE.getConfig().getOverburdenHex().getMovementSpeedModifier() : 0.025f);
        }
        return f;
    }
}
