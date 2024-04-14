package net.backupcup.mixin;

import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import net.backupcup.hexed.packets.HexNetworkingConstants;
import net.backupcup.hexed.register.RegisterEnchantments;
import net.backupcup.hexed.register.RegisterSounds;
import net.backupcup.hexed.util.*;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin
        extends PlayerEntity
        implements PredicateInterface, ProvisionInterface, AggravateInterface, OverclockInterface, PhasedInterface {

    @Shadow protected abstract void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition);

    @Shadow public abstract void playSound(SoundEvent event, SoundCategory category, float volume, float pitch);

    @Shadow public abstract void sendMessage(Text message);

    @Unique private float pullPredicate = 0;
    @Override
    public void setPredicate(float value) { this.pullPredicate = value; }
    @Override
    public float getPredicate() { return this.pullPredicate; }

    @Unique private ProvisionData provisionData = new ProvisionData(false, 0, 1, 0);
    @Override
    public void setProvisionData(ProvisionData data) {
        this.provisionData = data;
    }
    @Override
    public ProvisionData getProvisionData() {
        return this.provisionData;
    }

    @Unique private AggravateData aggravateData = new AggravateData(Hand.MAIN_HAND, 0, 0);
    @Override
    public void setAggravateData(AggravateData data) {
        this.aggravateData = data;
    }
    @Override
    public AggravateData getAggravateData() {
        return this.aggravateData;
    }

    @Unique private OverclockData overclockData = new OverclockData(0, 0);
    @Override
    public void setOverclockData(OverclockData value) { this.overclockData = value; }
    @Override
    public OverclockData getOverclockData() { return this.overclockData; }

    @Unique private int phasedCharge = 0;
    @Override
    public void setPhasedData(int value) { this.phasedCharge = value; }
    @Override
    public int getPhasedData() { return this.phasedCharge; }

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;readCustomDataFromNbt(Lnet/minecraft/nbt/NbtCompound;)V"))
    private void hexed$readCustomData(NbtCompound nbt, CallbackInfo ci) {
        this.overclockData.setCooldown(nbt.getInt("OverclockCooldown"));
        this.overclockData.setOverheat(nbt.getInt("OverclockOverheat"));
    }

    @Inject(method = "writeCustomDataToNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;writeCustomDataToNbt(Lnet/minecraft/nbt/NbtCompound;)V"))
    private void hexed$writeCustomData(NbtCompound nbt, CallbackInfo ci) {
        NbtCompound overclockNbt = new NbtCompound();
        overclockNbt.putInt("OverclockCooldown", this.overclockData.getCooldown());
        overclockNbt.putInt("OverclockOverheat", this.overclockData.getOverheat());

        nbt.put("OverclockData", overclockNbt);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void hexed$RecievePredicatePacket(CallbackInfo ci) {
        ServerSidePacketRegistry.INSTANCE.register(HexNetworkingConstants.INSTANCE.getPREDICATE_GETTER_PACKET(), (packetContext, attachedData) -> {
            float predicate = attachedData.readFloat();
            packetContext.getTaskQueue().execute(() -> {
                this.pullPredicate = predicate;
            });
        });
    }


    @Inject(method = "tick", at = @At("HEAD"))
    private void hexed$PlayerTick(CallbackInfo ci) {
        //PROVISION LOGIC
        if(this.provisionData.isUIActive()) {
            int indicatorLimit = 78;

            if(this.provisionData.getIndicatorPos() >= indicatorLimit) {
                this.provisionData.setUIActive(false);
                if(!HexHelper.INSTANCE.hasFullRobes(((ServerPlayerEntity)(Object)this))) getItemCooldownManager().set(Items.CROSSBOW, 80);
            } else { this.provisionData.setIndicatorPos(this.provisionData.getIndicatorPos() + this.provisionData.getReloadSpeed()); }

            if ((this.provisionData.getIndicatorPos() >= 10 && this.provisionData.getIndicatorPos() <= 16) &&
                    this.provisionData.getReloadsPlayed() < 1) {
                ((ServerPlayerEntity)(Object)this)
                        .playSound(RegisterSounds.INSTANCE.getPROVISION_IN_RANGE(), SoundCategory.PLAYERS, 0.5f, 1f);
                this.provisionData.setReloadsPlayed(1);
            }

            if ((this.provisionData.getIndicatorPos() >= 36 && this.provisionData.getIndicatorPos() <= 43) &&
                    this.provisionData.getReloadsPlayed() < 2) {
                ((ServerPlayerEntity)(Object)this)
                        .playSound(RegisterSounds.INSTANCE.getPROVISION_IN_RANGE(), SoundCategory.PLAYERS, 0.5f, 1f);
                this.provisionData.setReloadsPlayed(2);
            }

            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            buf.writeBoolean(this.provisionData.getIndicatorPos() <= indicatorLimit && this.provisionData.isUIActive());
            buf.writeInt(this.provisionData.getIndicatorPos());

            ServerPlayNetworking.send(
                    (ServerPlayerEntity) (Object) this,
                    HexNetworkingConstants.INSTANCE.getPROVISION_UPDATE_PACKET(), buf);

            if(this.provisionData.getIndicatorPos() > indicatorLimit) {
                this.provisionData.setUIActive(false);
                this.provisionData.setIndicatorPos(0);
                this.provisionData.setReloadSpeed(1);
                this.provisionData.setReloadsPlayed(0);
                ((ServerPlayerEntity)(Object)this)
                        .playSound(RegisterSounds.INSTANCE.getPROVISION_FAIL(), SoundCategory.PLAYERS, 1f, 1f);
            }
        }

        //OVERCLOCK LOGIC
        if(this.overclockData.getCooldown() > 0) {
            this.overclockData.setCooldown(this.overclockData.getCooldown() - 1);
        } else if (getEntityWorld().getTime() % 10L == 0L && this.overclockData.getOverheat() > 0) {
            this.overclockData.setOverheat(this.overclockData.getOverheat() - 1);

            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            buf.writeInt(this.overclockData.getOverheat());
            ServerPlayNetworking.send((ServerPlayerEntity)(Object)this, HexNetworkingConstants.INSTANCE.getOVERCLOCK_UPDATE_PACKET(), buf);
        }

        //AGGRAVATE LOGIC
        if(HexHelper.INSTANCE.stackHasEnchantment(getStackInHand(this.aggravateData.getBowHand()), RegisterEnchantments.INSTANCE.getAGGRAVATE_HEX())) {
            if (this.pullPredicate >= 1) {
                if(this.aggravateData.getChargeAmount() < 7) this.aggravateData.setChargedTicks(this.aggravateData.getChargedTicks() + 1L);

                if(this.aggravateData.getChargedTicks() >= 10 && this.aggravateData.getChargeAmount() < 6) {
                    this.aggravateData.setChargedTicks(0);
                    this.aggravateData.setChargeAmount(this.aggravateData.getChargeAmount() + 1);

                    PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                    buf.writeInt(this.aggravateData.getChargeAmount());
                    ServerPlayNetworking.send((ServerPlayerEntity)(Object)this, HexNetworkingConstants.INSTANCE.getAGGRAVATE_UPDATE_PACKET(), buf);

                    ((ServerPlayerEntity)(Object)this).playSound(
                            RegisterSounds.INSTANCE.getAGGRAVATE_TIER(), SoundCategory.PLAYERS,
                            1f, 1f + this.aggravateData.getChargeAmount() / 6f
                    );
                    System.out.println(this.aggravateData.getChargeAmount());

                } else if (this.aggravateData.getChargedTicks() >= 100 && this.aggravateData.getChargeAmount() == 6) {
                    this.aggravateData.setChargedTicks(0);
                    this.aggravateData.setChargeAmount(7);

                    PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                    buf.writeInt(this.aggravateData.getChargeAmount());
                    ServerPlayNetworking.send((ServerPlayerEntity)(Object)this, HexNetworkingConstants.INSTANCE.getAGGRAVATE_UPDATE_PACKET(), buf);

                    ((ServerPlayerEntity)(Object)this).playSound(
                            RegisterSounds.INSTANCE.getACCURSED_ALTAR_TAINT(), SoundCategory.PLAYERS,
                            0.5f, 2f
                    );
                    System.out.println(this.aggravateData.getChargeAmount());
                }
            }
        }
        if (!HexHelper.INSTANCE.stackHasEnchantment(getStackInHand(this.aggravateData.getBowHand()), RegisterEnchantments.INSTANCE.getAGGRAVATE_HEX()) && this.aggravateData.getChargeAmount() > 0){
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            buf.writeInt(0);
            ServerPlayNetworking.send((ServerPlayerEntity)(Object)this, HexNetworkingConstants.INSTANCE.getAGGRAVATE_UPDATE_PACKET(), buf);
        }
    }
}