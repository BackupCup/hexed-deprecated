package net.backupcup.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    @Unique
    private ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)(Object)this;

    public ServerPlayerEntity getServerPlayerEntity() { return this.serverPlayerEntity; }
}