package net.backupcup.mixin;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import kotlin.random.Random;
import net.backupcup.hexed.Hexed;
import net.backupcup.hexed.enchantments.AbstractHex;
import net.backupcup.hexed.register.RegisterEnchantments;
import net.backupcup.hexed.register.RegisterStatusEffects;
import net.backupcup.hexed.util.AttributeProviding;
import net.backupcup.hexed.util.HexHelper;
import net.backupcup.hexed.util.HexRandom;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.item.ItemStack.ENCHANTMENTS_KEY;

@Mixin(value = ItemStack.class, priority = 10)
public abstract class ItemStackMixin {
    @Shadow public abstract Item getItem();

    @Shadow public abstract NbtList getEnchantments();

    @Shadow public abstract Text getName();

    @Shadow private @Nullable NbtCompound nbt;

    @Unique
    private void iterateOverBox(Box box, World world, PlayerEntity player, ItemStack tool, BlockState state) {
        BlockPos minPos = new BlockPos((int) box.minX, (int) box.minY, (int) box.minZ);
        BlockPos maxPos = new BlockPos((int) box.maxX, (int) box.maxY, (int) box.maxZ);

        for (int x = minPos.getX(); x <= maxPos.getX(); x++) {
            for (int y = minPos.getY(); y <= maxPos.getY(); y++) {
                for (int z = minPos.getZ(); z <= maxPos.getZ(); z++) {
                    BlockPos pos = new BlockPos(x, y, z);

                    if (tool.isSuitableFor(world.getBlockState(pos)) &&
                            !world.getBlockState(pos).isAir() &&
                            world.getBlockState(pos).getBlock().getHardness() <= state.getBlock().getHardness()+2) {

                        boolean shouldDrop = (Hexed.INSTANCE.getConfig() != null) ?
                                HexRandom.INSTANCE.nextDouble(0, 1) >= Hexed.INSTANCE.getConfig().getAmplifyHex().getDropChance() :
                                HexRandom.INSTANCE.nextDouble(0, 1) >= 0.5;
                        if (HexHelper.INSTANCE.hasFullRobes(player.getArmorItems())) {
                            shouldDrop = true;
                        }

                        player.incrementStat(Stats.MINED.getOrCreateStat(world.getBlockState(pos).getBlock()));
                        world.breakBlock(pos, shouldDrop, player);

                        player.incrementStat(Stats.USED.getOrCreateStat(tool.getItem()));
                        if (!world.isClient) tool.damage(
                                (Hexed.INSTANCE.getConfig() != null) ? Hexed.INSTANCE.getConfig().getAmplifyHex().getToolDamage() : 1,
                                player, entity -> entity.sendToolBreakStatus(player.getActiveHand()));

                        if(Hexed.INSTANCE.getConfig() != null)
                            player.addExhaustion((float)Hexed.INSTANCE.getConfig().getAmplifyHex().getExhaustionAmount());
                    }
                }
            }
        }
    }

    @ModifyReturnValue(method = "hasEnchantments", at = @At("RETURN"))
    private boolean hexed$EnchantableHexes(boolean original) {
        if (nbt != null && nbt.contains(ENCHANTMENTS_KEY, NbtElement.LIST_TYPE)) {
            return !HexHelper.INSTANCE.getEnchantments((ItemStack) (Object) this).stream().filter(it -> !(it instanceof AbstractHex)).toList().isEmpty();
        }
        return false;
    }

    @ModifyReturnValue(method = "getAttributeModifiers", at = @At("RETURN"))
    private Multimap<EntityAttribute, EntityAttributeModifier> hexed$DisplacedSwapAttributes(
            Multimap<EntityAttribute, EntityAttributeModifier> original, EquipmentSlot slot) {
        if (getItem() instanceof EnchantedBookItem) return original;
        if (getItem() instanceof Equipment equipment && equipment.getSlotType() != slot) return original;

        Multimap<EntityAttribute, EntityAttributeModifier> newMap = null;
        boolean modified = false;

        for (var entry: EnchantmentHelper.get((ItemStack) (Object) this).entrySet()) {
            if (entry.getKey() instanceof AttributeProviding attributeProvidingEnchant) {
                if (newMap == null) newMap = ArrayListMultimap.create(original);
                attributeProvidingEnchant.modifyAttributeMap(newMap, slot, entry.getValue());
                modified = true;
            }
        }

        return modified ? newMap : original;
    }

    @ModifyVariable(method = "getTooltip", at = @At("STORE"), ordinal = 0)
    private MutableText hexed$modifyTooltipFormat(MutableText text) {
        ItemStack itemStack = (ItemStack) (Object) this;
        if (!HexHelper.INSTANCE.getEnchantments(itemStack).stream().filter(it -> it instanceof AbstractHex).toList().isEmpty()) {
            text = text.formatted(Formatting.BOLD, Formatting.DARK_RED);
        }
        return text;
    }

    @Inject(method = "postMine", at = @At("HEAD"))
    private void hexed$AmplifyMine(World world, BlockState state, BlockPos pos, PlayerEntity player, CallbackInfo ci) {
        ItemStack tool = (ItemStack) (Object) this;
        if (!tool.getItem().isSuitableFor(state)) return;

        if (!player.isSneaking() && HexHelper.INSTANCE.stackHasEnchantment(tool, RegisterEnchantments.INSTANCE.getAMPLIFY_HEX())) {
            Vec3d cameraPosVec = player.getCameraPosVec(1.0f);
            Vec3d blockCenter = new Vec3d(pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5);

            Vec3d dotProduct = new Vec3d(
                    (blockCenter.x - cameraPosVec.x) * player.getRotationVector().x,
                    (blockCenter.y - cameraPosVec.y) * player.getRotationVector().y,
                    (blockCenter.z - cameraPosVec.z) * player.getRotationVector().z);

            double maxDot = Math.max(Math.abs(dotProduct.x), Math.max(Math.abs(dotProduct.y), Math.abs(dotProduct.z)));

            Box breakBox = new Box(pos, pos);

            if (maxDot == Math.abs(dotProduct.x)) breakBox = breakBox.expand(0, 1, 1);
            else if (maxDot == Math.abs(dotProduct.y)) breakBox = breakBox.expand(1, 0, 1);
            else if (maxDot == Math.abs(dotProduct.z)) breakBox = breakBox.expand(1, 1, 0);

            iterateOverBox(breakBox, world, player, tool, state);
        }
    }

    @Inject(method = "postMine", at = @At("HEAD"))
    private void hexed$OverburdenStack(World world, BlockState state, BlockPos pos, PlayerEntity miner, CallbackInfo ci) {
        ItemStack tool = (ItemStack) (Object) this;
        if (!tool.getItem().isSuitableFor(state)) return;

        int decayLength = HexHelper.INSTANCE.hasFullRobes(miner) ? 0 : 1;

        if(HexHelper.INSTANCE.stackHasEnchantment(tool, RegisterEnchantments.INSTANCE.getOVERBURDEN_HEX())) {
            if (miner.hasStatusEffect(RegisterStatusEffects.INSTANCE.getOVERBURDEN())) {
                if (miner.getStatusEffect(RegisterStatusEffects.INSTANCE.getOVERBURDEN()).getAmplifier() < 256)
                    HexHelper.INSTANCE.entityMultiplyingEffect(miner, RegisterStatusEffects.INSTANCE.getOVERBURDEN(),
                            Hexed.INSTANCE.getConfig() != null ? Hexed.INSTANCE.getConfig().getOverburdenHex().getBuffDuration() : 200, decayLength);
            } else HexHelper.INSTANCE.entityMultiplyingEffect(miner, RegisterStatusEffects.INSTANCE.getOVERBURDEN(),
                    Hexed.INSTANCE.getConfig() != null ? Hexed.INSTANCE.getConfig().getOverburdenHex().getBuffDuration() : 200, decayLength);
        }
    }

    @Inject(method = "postMine", at = @At("HEAD"))
    private void hexed$RuinousExplode(World world, BlockState state, BlockPos pos, PlayerEntity miner, CallbackInfo ci) {
        ItemStack tool = (ItemStack) (Object) this;
        if (!tool.getItem().isSuitableFor(state)) return;

        if (HexHelper.INSTANCE.stackHasEnchantment(tool, RegisterEnchantments.INSTANCE.getRUINOUS_HEX())) {
            Entity entity = null;
            if (HexHelper.INSTANCE.hasFullRobes(miner)) entity = miner;

            world.createExplosion(entity,
                    pos.getX()+HexRandom.INSTANCE.nextDouble(-0.25,1.25),
                    pos.getY()+HexRandom.INSTANCE.nextDouble(-0.25,1.25),
                    pos.getZ()+HexRandom.INSTANCE.nextDouble(-0.25,1.25),
                    Hexed.INSTANCE.getConfig() != null ? Hexed.INSTANCE.getConfig().getRuinousHex().getExplosionPower() : 1.25f,
                    World.ExplosionSourceType.BLOCK);
        }
    }

    @WrapWithCondition(method = "damage(ILnet/minecraft/util/math/random/Random;Lnet/minecraft/server/network/ServerPlayerEntity;)Z",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;setDamage(I)V"))
    private boolean hexed$FamishmentShouldDamage(ItemStack stack, int amount, int damage, net.minecraft.util.math.random.Random random, ServerPlayerEntity player) {
        if (HexHelper.INSTANCE.stackHasEnchantment(stack, RegisterEnchantments.INSTANCE.getFAMISHMENT_HEX())) {
            if (player == null) return true;

            if (player.getHungerManager().getFoodLevel() >= damage) {
                player.getHungerManager().addExhaustion(damage);
            } else if (!HexHelper.INSTANCE.hasFullRobes(player)){
                player.damage(player.getDamageSources().dryOut(), damage);
            }
            return false;
        }
        return true;
    }
}
