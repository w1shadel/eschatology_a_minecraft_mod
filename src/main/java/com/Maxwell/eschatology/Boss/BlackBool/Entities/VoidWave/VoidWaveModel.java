package com.Maxwell.eschatology.Boss.BlackBool.Entities.VoidWave;import com.Maxwell.eschatology.Eschatology;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
@SuppressWarnings("removal")
public class VoidWaveModel<T extends Entity> extends EntityModel<T> {	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Eschatology.MODID, "void_wave"), "main");
	private final ModelPart root;
	private final ModelPart body;	public VoidWaveModel(ModelPart root) {
		this.root = root.getChild("root");
		this.body = this.root.getChild("body");
	}	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));		PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 22).addBox(-24.0F, -16.0F, -1.0F, 48.0F, 16.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(56, 47).addBox(-24.0F, -16.0F, -1.0F, 1.0F, 16.0F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(56, 76).addBox(23.0F, -16.0F, -1.0F, 1.0F, 16.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -18.9F, -5.2F, -0.0262F, 0.0F, 0.0F));		PartDefinition cube_r2 = body.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 47).addBox(-0.5F, -8.0F, -6.5F, 1.0F, 20.0F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(28, 47).addBox(46.5F, -8.0F, -6.5F, 1.0F, 20.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-23.5F, -12.6533F, 2.2075F, 0.2356F, 0.0F, 0.0F));		PartDefinition cube_r3 = body.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 0).addBox(-24.0F, -20.0F, -1.0F, 48.0F, 20.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));		PartDefinition cube_r4 = body.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 40).addBox(-24.0F, -5.0F, -1.0F, 48.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.9F, 0.0F, 1.5272F, 0.0F, 0.0F));		return LayerDefinition.create(meshdefinition, 128, 128);
	}	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {	}	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}