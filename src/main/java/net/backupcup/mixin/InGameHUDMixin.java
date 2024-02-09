package net.backupcup.mixin;

import net.backupcup.hexed.Hexed;
import net.backupcup.hexed.register.RegisterStatusEffects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHUDMixin {
    @Unique
    private static final Identifier HEARTS_IRRADIATED = new Identifier(Hexed.INSTANCE.getMOD_ID(), "textures/gui/icons_irradiated.png");
    @Unique
    private static final Identifier HEARTS_AFLAME = new Identifier(Hexed.INSTANCE.getMOD_ID(), "textures/gui/icons_aflame.png");
    @Unique
    private static final Identifier HEARTS_ETHEREAL = new Identifier(Hexed.INSTANCE.getMOD_ID(), "textures/gui/icons_ethereal.png");
    @Inject(method = "drawHeart", at = @At("HEAD"), cancellable = true)
    private void hexed$drawEffectHearts(DrawContext context, InGameHud.HeartType type, int x, int y, int v, boolean blinking, boolean halfHeart, CallbackInfo ci) {
        if (!blinking && type == InGameHud.HeartType.NORMAL &&
                MinecraftClient.getInstance().cameraEntity instanceof PlayerEntity player) {
            Identifier texture;
            if (player.hasStatusEffect(RegisterStatusEffects.INSTANCE.getIRRADIATED())) {
                texture = HEARTS_IRRADIATED;
            } else if (player.hasStatusEffect(RegisterStatusEffects.INSTANCE.getAFLAME()) ||
                       player.hasStatusEffect(RegisterStatusEffects.INSTANCE.getABLAZE())) {
                texture = HEARTS_AFLAME;
            } else if (player.hasStatusEffect(RegisterStatusEffects.INSTANCE.getETHEREAL())) {
                texture = HEARTS_ETHEREAL;
            } else {
                return;
            }
            context.drawTexture(texture, x, y, halfHeart ? 9 : 0, v, 9, 9);
            ci.cancel();
        }
    }
}
