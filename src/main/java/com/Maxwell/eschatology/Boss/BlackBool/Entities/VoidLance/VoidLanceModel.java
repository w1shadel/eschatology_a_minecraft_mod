package com.Maxwell.eschatology.Boss.BlackBool.Entities.VoidLance;import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
@SuppressWarnings("removal")
public class VoidLanceModel<T extends Entity> extends EntityModel<T> {	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "voidlancemodel"), "main");
	private final ModelPart root;	public VoidLanceModel(ModelPart root) {
		this.root = root.getChild("root");
	}	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(8, 32).addBox(4.0F, -16.0F, 3.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(24, 37).addBox(4.0F, -4.0F, 3.0F, 1.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(24, 0).addBox(2.0F, -25.0F, 3.0F, 2.0F, 13.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(32, 14).addBox(-1.0F, -16.0F, -2.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(24, 15).addBox(-4.0F, -25.0F, 3.0F, 2.0F, 13.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(16, 17).addBox(-1.0F, -25.1F, 6.0F, 2.0F, 13.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(40, 0).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(36, 37).addBox(-1.0F, -4.0F, 8.0F, 2.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 32).addBox(-1.0F, -16.0F, 8.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-1.0F, -65.0F, 3.0F, 2.0F, 70.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(24, 30).addBox(-2.0F, -66.9F, 2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(8, 0).addBox(-2.0F, -32.4F, 2.0F, 4.0F, 13.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(8, 17).addBox(-1.0F, -25.1F, 0.0F, 2.0F, 13.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(30, 37).addBox(-5.0F, -4.0F, 3.0F, 1.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(32, 0).addBox(-6.0F, -16.0F, 3.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 17.0F, -4.0F));		return LayerDefinition.create(meshdefinition, 128, 128);
	}	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {	}	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}