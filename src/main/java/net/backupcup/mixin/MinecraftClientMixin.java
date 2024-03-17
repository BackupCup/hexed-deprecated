package net.backupcup.mixin;

import net.backupcup.hexed.util.ItemUseCooldown;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin  implements ItemUseCooldown {
    @Shadow private int itemUseCooldown;

    @Override
    public void imposeItemUseCooldown(int ticks) {
        itemUseCooldown = ticks;
    }
}
