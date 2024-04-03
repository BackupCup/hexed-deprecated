package net.backupcup.mixin;

import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import net.backupcup.hexed.packets.HexNetworkingConstants;
import net.backupcup.hexed.register.RegisterSounds;
import net.backupcup.hexed.util.ProvisionData;
import net.backupcup.hexed.util.ProvisionInterface;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements ProvisionInterface {

    @Shadow protected abstract void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition);

    @Shadow public abstract void playSound(SoundEvent event, SoundCategory category, float volume, float pitch);

    @Unique private ProvisionData provisionData = new ProvisionData(false, 0, 1);
    @Override
    public void setProvisionData(ProvisionData data) {
        this.provisionData = data;
    }
    @Override
    public ProvisionData getProvisionData() {
        return this.provisionData;
    }

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void hexed$ProvisionTick(CallbackInfo ci) {
        if(this.provisionData.isUIActive()) {
            int indicatorLimit = 78;

            if(this.provisionData.getIndicatorPos() >= indicatorLimit) {
                this.provisionData.setUIActive(false);
                getItemCooldownManager().set(Items.CROSSBOW, 80);
            }
            else {
                this.provisionData.setIndicatorPos(this.provisionData.getIndicatorPos() + this.provisionData.getReloadSpeed());
            }

            switch (this.provisionData.getIndicatorPos()) {
                case 10:
                    ((ServerPlayerEntity)(Object)this)
                            .playSound(RegisterSounds.INSTANCE.getPROVISION_IN_RANGE(), SoundCategory.PLAYERS, 0.5f, 1f);
                case 16:
                    ((ServerPlayerEntity)(Object)this)
                            .playSound(RegisterSounds.INSTANCE.getPROVISION_IN_RANGE(), SoundCategory.PLAYERS, 0.5f, 2f);
                case 36:
                    ((ServerPlayerEntity)(Object)this)
                            .playSound(RegisterSounds.INSTANCE.getPROVISION_IN_RANGE(), SoundCategory.PLAYERS, 0.5f, 1f);
            }

            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            buf.writeBoolean(this.provisionData.isUIActive());
            buf.writeInt(this.provisionData.getIndicatorPos());

            ServerPlayNetworking.send(
                    (ServerPlayerEntity) (Object) this,
                    HexNetworkingConstants.INSTANCE.getPROVISION_UPDATE_PACKET(), buf);

            if(this.provisionData.getIndicatorPos() > indicatorLimit) {
                this.provisionData.setIndicatorPos(0);
                this.provisionData.setReloadSpeed(1);
            }
        }
    }
}