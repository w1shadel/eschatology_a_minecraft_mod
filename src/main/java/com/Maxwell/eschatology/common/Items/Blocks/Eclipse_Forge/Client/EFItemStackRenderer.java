package com.Maxwell.eschatology.common.Items.Blocks.Eclipse_Forge.Client;import com.Maxwell.eschatology.Eschatology;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
@SuppressWarnings("removal")
public class EFItemStackRenderer extends BlockEntityWithoutLevelRenderer {    private static final ResourceLocation TEXTURE =
            new ResourceLocation(Eschatology.MODID, "textures/entity/eclipse_forge_entity.png");    private Eclipse_Forge_Model<?> model;    public EFItemStackRenderer() {
        super(null, null);
    }    private void ensureModel() {
        if (model == null) {
            EntityModelSet set = Minecraft.getInstance().getEntityModels();
            model = new Eclipse_Forge_Model<>(set.bakeLayer(Eclipse_Forge_Model.LAYER_LOCATION));
        }
    }
    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext context,
                             PoseStack poseStack, MultiBufferSource buffer,
                             int packedLight, int packedOverlay) {        ensureModel();
        if (model == null) return;        poseStack.pushPose();        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
        BakedModel baked = renderer.getModel(stack, null, null, 0);
        baked.getTransforms().getTransform(context).apply(false, poseStack);        VertexConsumer vc;        if (context == ItemDisplayContext.GUI) {
            vc = buffer.getBuffer(RenderType.text(TEXTURE));
            poseStack.translate(0.6, -0.2, 0.0);
            poseStack.scale(1.0f, 1.0f, 1.0f);        } else {
            vc = buffer.getBuffer(RenderType.entityCutout(TEXTURE));
            poseStack.translate(0.5, 0.5, 0.5);
            poseStack.scale(1f, -1f, -1f);
        }        model.renderToBuffer(poseStack, vc, packedLight, packedOverlay, 1, 1, 1, 1);        poseStack.popPose();
    }
}