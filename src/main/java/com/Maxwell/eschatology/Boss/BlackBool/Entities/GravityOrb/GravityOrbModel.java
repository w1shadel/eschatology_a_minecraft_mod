package com.Maxwell.eschatology.Boss.BlackBool.Entities.GravityOrb;import com.Maxwell.eschatology.Eschatology;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;@SuppressWarnings("removal")
public class GravityOrbModel extends HierarchicalModel<GravityOrbEntity> {	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Eschatology.MODID, "gravity_orb"), "main");
	private final ModelPart bb_main;	public GravityOrbModel(ModelPart root) {
		this.bb_main = root.getChild("bb_main");
	}	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 36).addBox(-3.0F, -9.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(16, 36).addBox(-1.0F, -13.0F, 5.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(24, 28).addBox(-1.0F, -9.0F, -3.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(5.0F, -9.0F, -5.0F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(0, 12).addBox(-7.0F, -9.0F, -5.0F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(24, 12).addBox(-5.0F, -9.0F, -7.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(24, 16).addBox(-5.0F, -9.0F, 5.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(8, 40).addBox(-1.0F, -11.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(24, 36).addBox(-1.0F, -13.0F, -7.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(32, 36).addBox(-7.0F, -13.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 40).addBox(5.0F, -13.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 24).addBox(-1.0F, -3.0F, -5.0F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(24, 0).addBox(-1.0F, -15.0F, -5.0F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(24, 20).addBox(-5.0F, -3.0F, -1.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(24, 24).addBox(-5.0F, -15.0F, -1.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));		return LayerDefinition.create(meshdefinition, 64, 64);
	}	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}    @Override
    public ModelPart root() {
        return bb_main;
    }    @Override
    public void setupAnim(GravityOrbEntity pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {    }
}