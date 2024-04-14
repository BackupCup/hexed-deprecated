package net.backupcup.mixin.client;

import net.backupcup.hexed.Hexed;
import net.backupcup.hexed.enchantments.AbstractHex;
import net.backupcup.hexed.register.RegisterStatusEffects;
import net.backupcup.hexed.util.HexHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

import static java.util.Map.entry;

@Environment(EnvType.CLIENT)
@Mixin(value = InGameHud.class, priority = 10)
public abstract class InGameHUDMixin {
    @Shadow
    private ItemStack currentStack;

    @Unique
    private static final Map<StatusEffect, Identifier> TEXTURES_MAP = Map.ofEntries(
            entry(RegisterStatusEffects.INSTANCE.getSMOULDERING(), new Identifier(Hexed.MOD_ID, "textures/gui/icons_smouldering.png")),
            entry(RegisterStatusEffects.INSTANCE.getAFLAME(), new Identifier(Hexed.MOD_ID, "textures/gui/icons_aflame.png")),
            entry(RegisterStatusEffects.INSTANCE.getABLAZE(), new Identifier(Hexed.MOD_ID, "textures/gui/icons_aflame.png")), // Assuming aflame and ablaze share the same texture
            entry(RegisterStatusEffects.INSTANCE.getTRAITOROUS(), new Identifier(Hexed.MOD_ID, "textures/gui/icons_traitorous.png"))
    );

    @Inject(method = "drawHeart", at = @At("HEAD"), cancellable = true)
    private void hexed$drawEffectHearts(DrawContext context, InGameHud.HeartType type, int x, int y, int v, boolean blinking, boolean isHalfHeart, CallbackInfo ci) {
        if (!blinking && type.equals(InGameHud.HeartType.NORMAL) && MinecraftClient.getInstance().cameraEntity instanceof PlayerEntity player) {
            Identifier texture;

            for (StatusEffect effect : TEXTURES_MAP.keySet()) {
                if (player.hasStatusEffect(effect)) {
                    texture = TEXTURES_MAP.get(effect);
                    context.drawTexture(texture, x, y, isHalfHeart ? 9 : 0, v, 9, 9);

                    ci.cancel();
                    return;
                }
            }
        }
    }

    @ModifyVariable(method = "renderHeldItemTooltip", at = @At("STORE"), ordinal = 0)
    private MutableText hexed$ModifyHeldItemTooltip(MutableText text) {
        if (!HexHelper.INSTANCE.getEnchantments(currentStack).stream().filter(it -> it instanceof AbstractHex).toList().isEmpty()) {
            text = text.formatted(Formatting.BOLD, Formatting.DARK_RED);
        }
        return text;
    }
}
