package net.backupcup.mixin.client.model;

import net.backupcup.hexed.util.CustomHandTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @ModifyVariable(method = "renderItem", at = @At(value = "HEAD"), argsOnly = true)
    public BakedModel useHandModel(
            BakedModel value,
            ItemStack stack,
            ModelTransformationMode renderMode,
            boolean leftHanded,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light, int overlay) {
        if (stack.getItem() instanceof CustomHandTexture && (renderMode != ModelTransformationMode.GUI)) {
            return ((ItemRendererAccessor) this).hexed$getModels().getModelManager().getModel(((CustomHandTexture) stack.getItem()).getHandTexture());
        }
        return value;
    }
}
