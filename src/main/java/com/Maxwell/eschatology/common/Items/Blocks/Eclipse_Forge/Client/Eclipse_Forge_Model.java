package com.Maxwell.eschatology.common.Items.Blocks.Eclipse_Forge.Client;import com.Maxwell.eschatology.Eschatology;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;@SuppressWarnings("removal")
public class Eclipse_Forge_Model<T extends Entity> extends EntityModel<T> {	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Eschatology.MODID, "eclipse_forge_entity"), "main");
	public final ModelPart root;
    public final ModelPart black_hoal;
    public final ModelPart table;	public Eclipse_Forge_Model(ModelPart root) {
		this.root = root.getChild("root");
		this.black_hoal = this.root.getChild("black_hoal");
		this.table = this.root.getChild("table");
	}	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));		PartDefinition black_hoal = root.addOrReplaceChild("black_hoal", CubeListBuilder.create().texOffs(24, 61).addBox(-2.0F, -2.0625F, -4.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(56, 48).addBox(-4.0F, -2.0625F, -2.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(24, 55).addBox(-2.0F, -4.0625F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(56, 40).addBox(3.0F, -2.0625F, -2.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(40, 55).addBox(-2.0F, 1.9375F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(56, 56).addBox(-2.0F, -2.0625F, 3.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 55).addBox(-3.0F, -3.0625F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(0, 40).addBox(-7.0F, -0.0625F, -7.0F, 14.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -13.8375F, 0.0F));		PartDefinition table = root.addOrReplaceChild("table", CubeListBuilder.create().texOffs(0, 0).addBox(-16.0F, -6.0F, -0.1F, 16.0F, 7.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 23).addBox(-16.0F, -6.8F, -0.1F, 16.0F, 1.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, -1.0F, -8.0F));		return LayerDefinition.create(meshdefinition, 128, 128);
	}	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {	}	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}