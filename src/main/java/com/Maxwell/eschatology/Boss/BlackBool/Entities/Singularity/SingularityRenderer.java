package com.Maxwell.eschatology.Boss.BlackBool.Entities.Singularity;import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;public class SingularityRenderer extends EntityRenderer<SingularityEntity> {
    public SingularityRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }    @Override
    public ResourceLocation getTextureLocation(SingularityEntity pEntity) {
        return null;
    }
}
