package com.Maxwell.eschatology.Boss.XF07_Revanant;import com.Maxwell.eschatology.Boss.XF07_Revanant.Animation.ExoWitherAnimation;
import com.Maxwell.eschatology.Eschatology;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;@SuppressWarnings("removal")
@OnlyIn(Dist.CLIENT)
public class ExoWitherModel extends HierarchicalModel<ExoWither> {	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Eschatology.MODID, "exowithermodel"), "main");
	private final ModelPart root;
	private final ModelPart body;
	private final ModelPart right_ribs;
	private final ModelPart boosters;
	private final ModelPart right_booster_part;
	private final ModelPart right_booster;
	private final ModelPart right_booster_plate;
	private final ModelPart left_booster_part;
	private final ModelPart left_booster;
	private final ModelPart left_booster_plate;
	private final ModelPart body_subpart;
	private final ModelPart left_ribs;
	private final ModelPart face_right;
	private final ModelPart upper2;
	private final ModelPart right_gun;
	private final ModelPart under2;
	private final ModelPart face_left;
	private final ModelPart under;
	private final ModelPart upper;
	private final ModelPart right_gun2;
	private final ModelPart face_front;
	private final ModelPart upper3;
	private final ModelPart under3;
	private final ModelPart exo_heart;
	private final ModelPart under_body;
	private final ModelPart tale;
	private final ModelPart under_right_ribs;
	private final ModelPart under_left_ribs;	public ExoWitherModel(ModelPart root) {
		this.root = root.getChild("root");
		this.body = this.root.getChild("body");
		this.right_ribs = this.body.getChild("right_ribs");
		this.boosters = this.body.getChild("boosters");
		this.right_booster_part = this.boosters.getChild("right_booster_part");
		this.right_booster = this.right_booster_part.getChild("right_booster");
		this.right_booster_plate = this.right_booster.getChild("right_booster_plate");
		this.left_booster_part = this.boosters.getChild("left_booster_part");
		this.left_booster = this.left_booster_part.getChild("left_booster");
		this.left_booster_plate = this.left_booster.getChild("left_booster_plate");
		this.body_subpart = this.body.getChild("body_subpart");
		this.left_ribs = this.body.getChild("left_ribs");
		this.face_right = this.body.getChild("face_right");
		this.upper2 = this.face_right.getChild("upper2");
		this.right_gun = this.upper2.getChild("right_gun");
		this.under2 = this.face_right.getChild("under2");
		this.face_left = this.body.getChild("face_left");
		this.under = this.face_left.getChild("under");
		this.upper = this.face_left.getChild("upper");
		this.right_gun2 = this.upper.getChild("right_gun2");
		this.face_front = this.body.getChild("face_front");
		this.upper3 = this.face_front.getChild("upper3");
		this.under3 = this.face_front.getChild("under3");
		this.exo_heart = this.body.getChild("exo_heart");
		this.under_body = this.body.getChild("under_body");
		this.tale = this.under_body.getChild("tale");
		this.under_right_ribs = this.under_body.getChild("under_right_ribs");
		this.under_left_ribs = this.under_body.getChild("under_left_ribs");
	}	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(5.0F, 18.0F, 4.0F));		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(-4.7414F, -21.1276F, -2.7448F));		PartDefinition right_ribs = body.addOrReplaceChild("right_ribs", CubeListBuilder.create().texOffs(84, 195).addBox(0.75F, -1.0F, -11.5F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(174, 146).addBox(-1.25F, 3.0F, -11.5F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(148, 100).addBox(0.75F, 3.0F, -11.5F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(188, 71).addBox(0.75F, -5.0F, -11.5F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(178, 75).addBox(-1.25F, -1.0F, -11.5F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(178, 89).addBox(4.75F, -1.0F, -7.5F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(188, 61).addBox(2.75F, -1.0F, -7.5F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(174, 132).addBox(-1.25F, -5.0F, -11.5F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(36, 148).addBox(4.75F, -5.0F, -7.5F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(192, 15).addBox(2.75F, -5.0F, -7.5F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(188, 51).addBox(2.75F, 3.0F, -7.5F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(122, 164).addBox(4.75F, 3.0F, -7.5F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-9.0086F, -1.8724F, 4.2448F));		PartDefinition boosters = body.addOrReplaceChild("boosters", CubeListBuilder.create().texOffs(92, 164).addBox(-2.8814F, -5.0536F, -0.35F, 6.0F, 12.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(0, 160).addBox(-9.8814F, -5.0536F, -10.35F, 20.0F, 12.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(27, 160).addBox(-5.8814F, 6.9464F, -10.35F, 12.0F, 8.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.3773F, -1.8188F, 18.0949F));		PartDefinition right_booster_part = boosters.addOrReplaceChild("right_booster_part", CubeListBuilder.create().texOffs(114, 2).addBox(-1.4347F, -7.0063F, -1.3157F, 5.0F, 12.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.4467F, 3.1321F, -2.4143F, 0.48F, 0.0F, 0.0F));		PartDefinition right_booster = right_booster_part.addOrReplaceChild("right_booster", CubeListBuilder.create(), PartPose.offset(-7.0653F, 1.5652F, 17.7558F));		PartDefinition cube_r1 = right_booster.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(66, 1).addBox(-6.0F, -18.0F, -6.0F, 11.0F, 30.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0693F, -5.5714F, 0.1285F, 0.0F, -0.7854F, 0.0F));		PartDefinition right_booster_plate = right_booster.addOrReplaceChild("right_booster_plate", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));		PartDefinition cube_r2 = right_booster_plate.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(64, 104).addBox(6.0F, -16.0F, -8.0F, 2.0F, 32.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(142, 136).addBox(-8.0F, -16.0F, 6.0F, 14.0F, 32.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(112, 31).addBox(-8.0F, -16.0F, -8.0F, 2.0F, 32.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(64, 152).addBox(-6.0F, -16.0F, -8.0F, 12.0F, 32.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0693F, -4.5714F, 0.1285F, 0.0F, -0.7854F, 0.0F));		PartDefinition left_booster_part = boosters.addOrReplaceChild("left_booster_part", CubeListBuilder.create().texOffs(99, 135).addBox(-3.0908F, -6.8824F, -1.4936F, 5.0F, 12.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.2094F, 2.875F, -2.4857F, 0.48F, 0.0F, 0.0F));		PartDefinition left_booster = left_booster_part.addOrReplaceChild("left_booster", CubeListBuilder.create(), PartPose.offset(7.2908F, 0.9748F, 17.4351F));		PartDefinition cube_r3 = left_booster.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(66, 43).addBox(-6.0F, -15.0F, -6.0F, 11.0F, 30.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.3184F, -8.8571F, 0.2714F, 0.0F, -0.7854F, 0.0F));		PartDefinition left_booster_plate = left_booster.addOrReplaceChild("left_booster_plate", CubeListBuilder.create(), PartPose.offset(0.3184F, -5.8571F, 0.2714F));		PartDefinition cube_r4 = left_booster_plate.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(36, 105).addBox(-8.0F, -15.0F, -6.0F, 2.0F, 31.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(32, 152).addBox(-8.0F, -15.0F, 6.0F, 14.0F, 31.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 105).addBox(6.0F, -15.0F, -8.0F, 2.0F, 31.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 152).addBox(-8.0F, -15.0F, -8.0F, 14.0F, 31.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));		PartDefinition body_subpart = body.addOrReplaceChild("body_subpart", CubeListBuilder.create().texOffs(160, 0).addBox(-9.75F, -2.0F, 0.5F, 20.0F, 12.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(100, 123).addBox(-11.75F, -6.0F, -6.5F, 23.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5086F, -4.8724F, 4.2448F));		PartDefinition left_ribs = body.addOrReplaceChild("left_ribs", CubeListBuilder.create().texOffs(178, 89).addBox(-9.75F, -1.0F, -8.5F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(112, 77).addBox(-7.75F, 3.0F, -12.5F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(184, 160).addBox(-1.75F, 3.0F, -12.5F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(182, 183).addBox(-1.75F, -1.0F, -12.5F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(192, 25).addBox(-8.75F, -1.0F, -12.5F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(112, 186).addBox(-5.75F, 3.0F, -8.5F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(192, 25).addBox(-8.75F, -1.0F, -10.5F, 7.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(154, 183).addBox(-1.75F, -5.0F, -12.5F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(128, 100).addBox(-9.75F, -5.0F, -12.5F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(36, 148).addBox(-9.75F, -5.0F, -8.5F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(92, 185).addBox(-5.75F, -5.0F, -8.5F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(64, 186).addBox(-5.75F, -1.0F, -8.5F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(122, 164).addBox(-9.75F, 3.0F, -8.5F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(9.4914F, -1.8724F, 5.2448F));		PartDefinition face_right = body.addOrReplaceChild("face_right", CubeListBuilder.create(), PartPose.offset(-19.2586F, -5.3724F, 8.7448F));		PartDefinition upper2 = face_right.addOrReplaceChild("upper2", CubeListBuilder.create().texOffs(0, 57).addBox(-8.0F, -11.0F, -13.0F, 16.0F, 12.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.5F, -1.0F));		PartDefinition right_gun = upper2.addOrReplaceChild("right_gun", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -1.0F));		PartDefinition cube_r5 = right_gun.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(144, 31).addBox(-2.0F, -3.0F, -17.0F, 4.0F, 4.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.0036F, 0.0F, 0.0F));		PartDefinition under2 = face_right.addOrReplaceChild("under2", CubeListBuilder.create().texOffs(64, 84).addBox(-8.0F, -2.0F, -15.0F, 16.0F, 4.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.5F, 1.0F));		PartDefinition face_left = body.addOrReplaceChild("face_left", CubeListBuilder.create(), PartPose.offset(18.7414F, -5.3724F, 8.2448F));		PartDefinition under = face_left.addOrReplaceChild("under", CubeListBuilder.create().texOffs(0, 85).addBox(-8.0F, -2.0F, -14.0F, 16.0F, 4.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.5F, 0.5F));		PartDefinition upper = face_left.addOrReplaceChild("upper", CubeListBuilder.create().texOffs(0, 29).addBox(-8.0F, -11.0F, -13.0F, 16.0F, 12.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.5F, -0.5F));		PartDefinition right_gun2 = upper.addOrReplaceChild("right_gun2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -1.0F));		PartDefinition cube_r6 = right_gun2.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(144, 53).addBox(-2.0F, -3.0F, -17.0F, 4.0F, 4.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.0036F, 0.0F, 0.0F));		PartDefinition face_front = body.addOrReplaceChild("face_front", CubeListBuilder.create(), PartPose.offset(-1.2586F, -10.6224F, 1.7448F));		PartDefinition upper3 = face_front.addOrReplaceChild("upper3", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -13.5F, -14.0F, 16.0F, 13.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -2.75F, 5.0F));		PartDefinition under3 = face_front.addOrReplaceChild("under3", CubeListBuilder.create().texOffs(100, 104).addBox(-32.0F, -26.0F, -8.0F, 16.0F, 3.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(25.0F, 22.75F, -1.0F));		PartDefinition exo_heart = body.addOrReplaceChild("exo_heart", CubeListBuilder.create().texOffs(0, 185).addBox(-3.0F, -5.0F, -3.0F, 6.0F, 10.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.2586F, -1.8724F, 1.7448F));		PartDefinition under_body = body.addOrReplaceChild("under_body", CubeListBuilder.create().texOffs(154, 170).addBox(-5.0F, 0.7F, -1.9F, 12.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.2586F, 4.4276F, 6.6448F));		PartDefinition tale = under_body.addOrReplaceChild("tale", CubeListBuilder.create(), PartPose.offset(1.0F, 10.0805F, -0.1321F));		PartDefinition cube_r7 = tale.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(184, 174).addBox(-3.0F, -5.5F, -1.0F, 6.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.1195F, 7.6321F, 0.7854F, 0.0F, 0.0F));		PartDefinition cube_r8 = tale.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(128, 94).addBox(-6.0F, -1.0F, -2.0F, 12.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.0195F, 4.1321F, 0.6981F, 0.0F, 0.0F));		PartDefinition cube_r9 = tale.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(164, 126).addBox(-8.0F, -1.0F, -2.0F, 16.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.6195F, 1.5321F, 0.48F, 0.0F, 0.0F));		PartDefinition cube_r10 = tale.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(44, 195).addBox(-3.0F, -6.5F, -2.0F, 6.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.0195F, 3.9321F, 0.5236F, 0.0F, 0.0F));		PartDefinition under_right_ribs = under_body.addOrReplaceChild("under_right_ribs", CubeListBuilder.create().texOffs(92, 160).addBox(0.9167F, 3.0F, -8.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(24, 185).addBox(-1.0833F, 3.0F, -8.5F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(92, 156).addBox(0.9167F, -1.0F, -8.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(174, 160).addBox(0.9167F, -5.0F, -8.5F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(188, 41).addBox(-1.0833F, -5.0F, -8.5F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(24, 195).addBox(-1.0833F, -1.0F, -8.5F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.9167F, 5.7F, -1.4F));		PartDefinition under_left_ribs = under_body.addOrReplaceChild("under_left_ribs", CubeListBuilder.create().texOffs(164, 132).addBox(-3.9167F, -5.0F, -8.5F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(132, 186).addBox(-0.9167F, -5.0F, -8.5F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(56, 148).addBox(-2.9167F, 3.0F, -8.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(44, 185).addBox(-0.9167F, 3.0F, -8.5F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(188, 31).addBox(-0.9167F, -1.0F, -8.5F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(92, 152).addBox(-2.9167F, -1.0F, -8.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(5.9167F, 5.7F, -1.4F));		return LayerDefinition.create(meshdefinition, 256, 256);
	}
    @Override
    public void setupAnim(ExoWither entity, float limbSwing, float limbSwingAmount, float ageInTicks, float pNetHeadYaw, float pHeadPitch) {        this.root().getAllParts().forEach(ModelPart::resetPose);        this.animate(entity.idleAnimationState, ExoWitherAnimation.IDLE, ageInTicks);
        this.animate(entity.walkAnimationState, ExoWitherAnimation.WALK, ageInTicks);
        this.animate(entity.chargeAnimationState, ExoWitherAnimation.CHARGE_ATTACK, ageInTicks);
        this.animate(entity.missileattackAnimationState, ExoWitherAnimation.MISSILE_SHOOT_ATTACK, ageInTicks);
        this.animate(entity.simplelaserattackAnimationState, ExoWitherAnimation.SIMPLE_LASER_ATTACK, ageInTicks);
        this.animate(entity.laserbeamattackAnimationState, ExoWitherAnimation.LASERBEAM_ATTACK, ageInTicks);
        this.animate(entity.spawnAnimationState, ExoWitherAnimation.SPAWN, ageInTicks);
        this.animate(entity.deathAnimationState, ExoWitherAnimation.DEATH, ageInTicks);
        this.animateHeadLookTarget(pNetHeadYaw, pHeadPitch);
        this.right_gun.visible = false;
        this.right_gun2.visible = false;
    }
    private void animateHeadLookTarget(float yRot, float xRot) {
        this.face_front.xRot += xRot * Mth.DEG_TO_RAD;
        this.face_front.yRot += yRot * Mth.DEG_TO_RAD;
        this.face_left.xRot += xRot * Mth.DEG_TO_RAD;
        this.face_left.yRot += yRot * Mth.DEG_TO_RAD;
        this.face_right.xRot += xRot * Mth.DEG_TO_RAD;
        this.face_right.yRot += yRot * Mth.DEG_TO_RAD;
    }
    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
    @Override
    public ModelPart root() {
        return root;
    }
}