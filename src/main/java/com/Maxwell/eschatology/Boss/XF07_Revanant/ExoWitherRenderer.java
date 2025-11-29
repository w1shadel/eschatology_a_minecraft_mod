package com.Maxwell.eschatology.Boss.XF07_Revanant;import com.Maxwell.eschatology.Eschatology;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;@SuppressWarnings("removal")
@OnlyIn(Dist.CLIENT)
public class ExoWitherRenderer extends MobRenderer<ExoWither, ExoWitherModel> {
    private static final ResourceLocation NORMAL_TEXTURE =
            new ResourceLocation(Eschatology.MODID, "textures/entity/exo_wither.png");
    private static final ResourceLocation ENRAGED_TEXTURE =
            new ResourceLocation(Eschatology.MODID, "textures/entity/exo_wither_enraged.png");    public ExoWitherRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new ExoWitherModel(renderManagerIn.bakeLayer(ExoWitherModel.LAYER_LOCATION)), 0.5F);
    }    @Override
    public ResourceLocation getTextureLocation(ExoWither entity) {
        return entity.isEnraged() ? ENRAGED_TEXTURE : NORMAL_TEXTURE;
    }    @Override
    protected RenderType getRenderType(ExoWither entity, boolean bodyVisible, boolean translucent, boolean glowing) {
        if (entity.isEnraged()) {
            return RenderType.eyes(ENRAGED_TEXTURE);
        }
        return super.getRenderType(entity, bodyVisible, translucent, glowing);
    }
}