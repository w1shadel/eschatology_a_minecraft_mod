package com.Maxwell.eschatology.Boss.BlackBool.Entities;import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;public class EmptyRenderer extends EntityRenderer<Entity> {
    public EmptyRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }    @Override
    public ResourceLocation getTextureLocation(Entity pEntity) {
        return null;
    }
}
