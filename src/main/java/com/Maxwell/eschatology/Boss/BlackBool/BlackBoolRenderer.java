package com.Maxwell.eschatology.Boss.BlackBool;import com.Maxwell.eschatology.Eschatology;
import com.Maxwell.eschatology.client.GUI.CustomRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;@SuppressWarnings("removal")
public class BlackBoolRenderer extends MobRenderer<BlackBool, BlackBoolModel> {    private static final ResourceLocation TEXTURE = new ResourceLocation(Eschatology.MODID, "textures/entity/blackbole.png");
    private static final ResourceLocation RING_TEXTURE = new ResourceLocation(Eschatology.MODID, "textures/entity/ring.png");
    private static final ResourceLocation AURA_TEXTURE = new ResourceLocation(Eschatology.MODID, "textures/entity/effect/aura.png");    private static final Vector3f COLOR_NORMAL = new Vector3f(0.0F, 0.0F, 0.0F);
    private static final Vector3f COLOR_ENRAGED = new Vector3f(1.0F, 0.0F, 0.15F);
    private static final Vector3f COLOR_CHARGE = new Vector3f(1.0F, 0.5F, 0.0F);
    private static final Vector3f COLOR_GRAVITY = new Vector3f(0.6F, 0.0F, 1.0F);
    private static final Vector3f COLOR_BARRAGE = new Vector3f(1.0F, 1.0F, 0.0F);
    private static final Vector3f COLOR_VOID_WAVE = new Vector3f(0.0F, 1.0F, 0.8F);
    private static final Vector3f COLOR_PULSE = new Vector3f(0.9F, 0.9F, 1.0F);
    private static final Vector3f COLOR_SINGULARITY = new Vector3f(0.4F, 0.0F, 0.8F);
    private static final Vector3f COLOR_VOID_LANCE = new Vector3f(0.0F, 0.8F, 1.0F);
    private static final Vector3f COLOR_LASER = new Vector3f(1.0F, 0.1F, 0.0F);
    private static final Vector3f COLOR_PHASE_TWO = new Vector3f(0.6F, 0.0F, 1.0F);    public BlackBoolRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new BlackBoolModel(renderManagerIn.bakeLayer(BlackBoolModel.LAYER_LOCATION)), 0.5F);
    }
    @Override
    protected void scale(BlackBool pLivingEntity, PoseStack pMatrixStack, float pPartialTickTime) {
        pMatrixStack.scale(2.5F, 2.5F, 2.5F);
    }
    @Override
    public void render(BlackBool pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {        if (pEntity.getDeathAnimationTicks() > 0) {            renderDeathSequence(pEntity, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
        } else {            super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
            float ageInTicks = pEntity.tickCount + pPartialTicks;
            if (pEntity.getCurrentAttackPhase() == BlackBool.AttackPhase.OUTER_HORIZON) {
                renderOuterHorizonEffects(pEntity, ageInTicks, pPoseStack, pBuffer);
            } else {
                renderNormalEffects(pEntity, ageInTicks, pPoseStack, pBuffer, pPackedLight);
            }
        }
    }
    private void drawSuctionDebris(BlackBool entity, PoseStack poseStack, VertexConsumer consumer, int packedLight, float ageInTicks, int index, float distance, Vector3f color) {
        poseStack.pushPose();
        RandomSource random = RandomSource.create(entity.getId() + index);        float yaw = random.nextFloat() * 360.0F;
        float pitch = random.nextFloat() * 180.0F - 90.0F;
        poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(pitch));        poseStack.translate(distance, 0, 0);        poseStack.mulPose(Axis.YP.rotationDegrees(-yaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(-pitch));        poseStack.mulPose(Axis.YP.rotationDegrees(ageInTicks * random.nextFloat() * 5.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(ageInTicks * random.nextFloat() * 5.0F));        drawThickLineShape(poseStack, consumer, packedLight, 3, 0.2F + random.nextFloat() * 0.2F, 0.03F, color);        poseStack.popPose();
    }    private void drawSpreadingDebris(BlackBool entity, PoseStack poseStack, VertexConsumer consumer, int packedLight, float ageInTicks, int index, float distance, Vector3f color) {
        poseStack.pushPose();
        RandomSource random = RandomSource.create(entity.getId() + index);        float yaw = random.nextFloat() * 360.0F;
        float pitch = random.nextFloat() * 180.0F - 90.0F;
        poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(pitch));        poseStack.translate(distance, 0, 0);        poseStack.mulPose(Axis.YP.rotationDegrees(ageInTicks * (1.0f + random.nextFloat())));
        poseStack.mulPose(Axis.XP.rotationDegrees(ageInTicks * (1.0f + random.nextFloat())));        drawThickLineShape(poseStack, consumer, packedLight, 4, 0.15F, 0.015F, color);
        poseStack.popPose();
    }    private void renderLightSphere(PoseStack poseStack, MultiBufferSource buffer, float progress) {        float intensity = Mth.sin(progress * Mth.PI);        float scale = Mth.lerp(progress, 0.1F, 8.0F);
        float alpha = intensity * 0.8F;         if (alpha <= 0.0F) return;        poseStack.pushPose();
        poseStack.scale(scale, scale, scale);        VertexConsumer consumer = buffer.getBuffer(RenderType.energySwirl(AURA_TEXTURE, progress * 2.0F % 1.0F, progress * 2.0F % 1.0F));        Matrix4f matrix = poseStack.last().pose();
        int color = ((int)(alpha * 255) << 24) | 0xAA88FF;         for (int i = 0; i < 3; i++) {
            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees(i * 60.0F));
            consumer.vertex(matrix, -0.5F, -0.5F, 0).color(color).uv(0, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(0, 0, 1).endVertex();
            consumer.vertex(matrix, 0.5F, -0.5F, 0).color(color).uv(1, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(0, 0, 1).endVertex();
            consumer.vertex(matrix, 0.5F, 0.5F, 0).color(color).uv(1, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(0, 0, 1).endVertex();
            consumer.vertex(matrix, -0.5F, 0.5F, 0).color(color).uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(0, 0, 1).endVertex();
            poseStack.popPose();
        }
        poseStack.popPose();
    }
    private void renderDeathSequence(BlackBool pEntity, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        float deathTicks = pEntity.getDeathAnimationTicks() + pPartialTicks;
        float ageInTicks = pEntity.tickCount + pPartialTicks;        final int OUTBURST_END_TICK = 40;
        final int COLLAPSE_END_TICK = OUTBURST_END_TICK + 80;
        final int IMPLOSION_END_TICK = COLLAPSE_END_TICK + 60;        if (deathTicks > OUTBURST_END_TICK && deathTicks <= COLLAPSE_END_TICK) {            float collapseProgress = (deathTicks - OUTBURST_END_TICK) / (float)(COLLAPSE_END_TICK - OUTBURST_END_TICK);            float scale = Mth.lerp(collapseProgress, 1.0F, 0.0F);            pPoseStack.pushPose();
            pPoseStack.scale(scale, scale, scale);            super.render(pEntity, pEntity.yBodyRot, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
            pPoseStack.popPose();
        }        else if (deathTicks <= OUTBURST_END_TICK) {
            super.render(pEntity, pEntity.yBodyRot, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
        }        pPoseStack.pushPose();        pPoseStack.translate(0, pEntity.getBbHeight() * 1.25, 0);        if (deathTicks <= OUTBURST_END_TICK) {            RandomSource random = RandomSource.create(pEntity.getId());
            VertexConsumer consumer = pBuffer.getBuffer(CustomRenderTypes.DEBRIS_TYPE);
            for (int i = 0; i < 15; i++) {
                pPoseStack.pushPose();                pPoseStack.mulPose(Axis.YP.rotationDegrees(random.nextFloat() * 360));
                pPoseStack.mulPose(Axis.XP.rotationDegrees(random.nextFloat() * 180 - 90));                float length = 1.0F + random.nextFloat() * (deathTicks / (float)OUTBURST_END_TICK) * 3.0F;
                pPoseStack.scale(length, length, length);                drawThickLine(pPoseStack, consumer, pPackedLight, new Vector3f(0,0,0), new Vector3f(1,0,0), 0.02F, COLOR_ENRAGED);
                pPoseStack.popPose();
            }
        }        else if (deathTicks <= COLLAPSE_END_TICK) {
            float collapseProgress = (deathTicks - OUTBURST_END_TICK) / (float)(COLLAPSE_END_TICK - OUTBURST_END_TICK);
            VertexConsumer consumer = pBuffer.getBuffer(CustomRenderTypes.DEBRIS_TYPE);            for (int i = 0; i < 100; i++) {                float initialDistance = 15.0F;                float currentDistance = Mth.lerp(collapseProgress, initialDistance, 0.0F);                drawSuctionDebris(pEntity, pPoseStack, consumer, pPackedLight, ageInTicks, i, currentDistance, COLOR_NORMAL);
            }
        }        else if (deathTicks <= IMPLOSION_END_TICK) {
            float implosionProgress = (deathTicks - COLLAPSE_END_TICK) / (float)(IMPLOSION_END_TICK - COLLAPSE_END_TICK);
            VertexConsumer consumer = pBuffer.getBuffer(CustomRenderTypes.DEBRIS_TYPE);            renderLightSphere(pPoseStack, pBuffer, implosionProgress);            for (int i = 0; i < 150; i++) {                float currentDistance = Mth.lerp(implosionProgress, 0.0F, 20.0F);                Vector3f cosmicColor = new Vector3f(0.6f, 0.8f, 1.0f);                drawSpreadingDebris(pEntity, pPoseStack, consumer, pPackedLight, ageInTicks, i, currentDistance, cosmicColor);
            }
        }        pPoseStack.popPose();
    }
    private void renderOuterHorizonEffects(BlackBool pEntity, float pAgeInTicks, PoseStack pPoseStack, MultiBufferSource pBuffer) {
        pPoseStack.pushPose();
        pPoseStack.translate(0, pEntity.getBbHeight() * 1.25, 0);
        drawSuctionAura(pPoseStack, pBuffer, pAgeInTicks);
        pPoseStack.popPose();
    }
    private void renderNormalEffects(BlackBool pEntity, float pAgeInTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {        pPoseStack.pushPose();
        pPoseStack.translate(0.0, pEntity.getBbHeight() * 1.25, 0.0);
        pPoseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        pPoseStack.scale(6.0F, 6.0F, 6.0F);
        drawRingPlane(pPoseStack, pBuffer.getBuffer(RenderType.entityTranslucent(RING_TEXTURE)), pPackedLight);
        pPoseStack.popPose();        float healthPercent = pEntity.getHealthPercent();
        boolean isPhaseTwo = pEntity.isPhaseTwo();
        boolean isEnraged = healthPercent <= 0.25F;
        float speedMultiplier = isEnraged ? 2.5F : 1.0F;
        Vector3f currentColor;        if (isPhaseTwo) {
            speedMultiplier *= 1.5f;
            currentColor = new Vector3f(COLOR_PHASE_TWO);
            renderPhaseTwoAura(pEntity, pAgeInTicks, pPoseStack, pBuffer, pPackedLight);
        } else {
            currentColor = determineParticleColor(pEntity, isEnraged);
        }        VertexConsumer consumer = pBuffer.getBuffer(CustomRenderTypes.DEBRIS_TYPE);
        int particleCount = determineParticleCount(healthPercent, isPhaseTwo);        for (int i = 0; i < particleCount; i++) {
            drawOrbitingDebris(pEntity, pPoseStack, consumer, pPackedLight, pAgeInTicks, i, (i % 5 == 0) ? 4 : 3, (i % 2 == 0) ? 45.0f : 0.0f, 1.8F, 1.2F, speedMultiplier, currentColor);
        }        if (isPhaseTwo || healthPercent <= 0.50f) {
            int outerParticleCount = isPhaseTwo ? 25 : 15;
            for (int i = 0; i < outerParticleCount; i++) {
                drawOrbitingDebris(pEntity, pPoseStack, consumer, pPackedLight, pAgeInTicks, i + 100, 4, 45.0F, 2.5F, 1.5F, speedMultiplier, currentColor);
            }
        }
    }    private void renderPhaseTwoAura(BlackBool pEntity, float pAgeInTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        pPoseStack.translate(0.0, pEntity.getBbHeight() * 1.25, 0.0);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(pAgeInTicks * 2.0f));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(pAgeInTicks * 20.0f));
        pPoseStack.mulPose(Axis.XP.rotationDegrees(20.0f));
        pPoseStack.scale(5.0f, 5.0f, 0.1f);
        VertexConsumer energyConsumer = pBuffer.getBuffer(RenderType.energySwirl(RING_TEXTURE, pAgeInTicks * 0.05f % 1.0f, pAgeInTicks * 0.05f % 1.0f));
        drawRingPlane(pPoseStack, energyConsumer, pPackedLight);
        pPoseStack.popPose();
    }    private Vector3f determineParticleColor(BlackBool pEntity, boolean isEnraged) {
        if (isEnraged) return COLOR_ENRAGED;
        return switch (pEntity.getCurrentAttackPhase()) {
            case CHARGE -> COLOR_CHARGE;
            case GRAVITY_ORB -> COLOR_GRAVITY;
            case BARRAGE -> COLOR_BARRAGE;
            case VOID_WAVE -> COLOR_VOID_WAVE;
            case VOID_PULSE -> COLOR_PULSE;
            case SINGULARITY -> COLOR_SINGULARITY;
            case VOID_LANCE -> COLOR_VOID_LANCE;
            case LASER -> COLOR_LASER;
            default -> COLOR_NORMAL;
        };
    }    private int determineParticleCount(float healthPercent, boolean isPhaseTwo) {
        if (isPhaseTwo) return 35;
        if (healthPercent <= 0.50F) return 20;
        if (healthPercent <= 0.75F) return 15;
        return 10;
    }    private void drawSuctionAura(PoseStack poseStack, MultiBufferSource buffer, float ageInTicks) {
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(AURA_TEXTURE));
        for (int i = 0; i < 8; i++) {
            poseStack.pushPose();
            float rotation = (ageInTicks * (2.0f + i * 0.5f)) % 360;
            poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
            poseStack.mulPose(Axis.XP.rotationDegrees(rotation / 2.0f + i * 20));
            float scale = 8.0f - (ageInTicks * 0.4f + i) % 8.0f;
            float alpha = Mth.clamp(1.0f - (scale / 8.0f), 0.0f, 1.0f);
            if (scale > 0) {
                poseStack.scale(scale, scale, scale);
                Matrix4f matrix = poseStack.last().pose();
                int color = ((int)(alpha * 255) << 24) | 0xFFFFFF;
                consumer.vertex(matrix, -0.5F, -0.5F, 0).color(color).uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(0, 0, 1).endVertex();
                consumer.vertex(matrix, 0.5F, -0.5F, 0).color(color).uv(1, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(0, 0, 1).endVertex();
                consumer.vertex(matrix, 0.5F, 0.5F, 0).color(color).uv(1, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(0, 0, 1).endVertex();
                consumer.vertex(matrix, -0.5F, 0.5F, 0).color(color).uv(0, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(0, 0, 1).endVertex();
            }
            poseStack.popPose();
        }
    }    private void drawOrbitingDebris(BlackBool entity, PoseStack poseStack, VertexConsumer consumer, int packedLight, float ageInTicks, int index, int vertices, float initialRotation, float orbitRadiusMin, float orbitRadiusRand, float speedMultiplier, Vector3f color) {
        poseStack.pushPose();
        RandomSource random = RandomSource.create(entity.getId() + index);
        float orbitSpeed = (0.5F + random.nextFloat()) * (random.nextBoolean() ? 1 : -1) * speedMultiplier;
        float orbitRadius = orbitRadiusMin + random.nextFloat() * orbitRadiusRand;
        float angleOffset = random.nextFloat() * 360.0F;
        float yOffset = (random.nextFloat() - 0.5F) * 0.5F;
        float horizontalAngle = (ageInTicks * orbitSpeed + angleOffset) * Mth.DEG_TO_RAD;
        double px = Mth.cos(horizontalAngle) * orbitRadius;
        double py = yOffset;
        double pz = Mth.sin(horizontalAngle) * orbitRadius;
        poseStack.translate(px, entity.getBbHeight() * 1.25 + py, pz);
        poseStack.mulPose(Axis.YP.rotationDegrees(initialRotation));
        poseStack.mulPose(Axis.YP.rotationDegrees(ageInTicks * 2.0F * orbitSpeed));
        poseStack.mulPose(Axis.XP.rotationDegrees(ageInTicks * 3.0F * orbitSpeed + angleOffset));
        drawThickLineShape(poseStack, consumer, packedLight, vertices, 0.3F, 0.05F, color);
        poseStack.popPose();
    }    private void drawThickLineShape(PoseStack poseStack, VertexConsumer consumer, int packedLight, int vertices, float scale, float thickness, Vector3f color) {
        Vector3f[] points = new Vector3f[vertices];
        for (int i = 0; i < vertices; i++) {
            float angle = (float) (i * 2.0 * Math.PI / vertices);
            points[i] = new Vector3f(Mth.cos(angle) * scale, 0, Mth.sin(angle) * scale);
        }
        for (int i = 0; i < vertices; i++) {
            drawThickLine(poseStack, consumer, packedLight, points[i], points[(i + 1) % vertices], thickness, color);
        }
    }    private void drawThickLine(PoseStack poseStack, VertexConsumer consumer, int packedLight, Vector3f start, Vector3f end, float thickness, Vector3f color) {
        Matrix4f matrix = poseStack.last().pose();
        Matrix3f normalMatrix = poseStack.last().normal();        Vector3f lineDir = new Vector3f(end).sub(start).normalize();
        Vector3f thicknessDir = new Vector3f(lineDir).cross(new Vector3f(0, 1, 0)).normalize();        if (thicknessDir.lengthSquared() < 0.1f) {
            thicknessDir = new Vector3f(lineDir).cross(new Vector3f(0, 0, 1)).normalize();
        }
        thicknessDir.mul(thickness / 2.0f);        Vector3f v1 = new Vector3f(start).add(thicknessDir);
        Vector3f v2 = new Vector3f(start).sub(thicknessDir);
        Vector3f v3 = new Vector3f(end).sub(thicknessDir);
        Vector3f v4 = new Vector3f(end).add(thicknessDir);        float r = color.x(), g = color.y(), b = color.z(), a = 0.7f;        Vector3f normal = new Vector3f(end).sub(start).cross(thicknessDir).normalize();        consumer.vertex(matrix, v1.x(), v1.y(), v1.z()).color(r, g, b, a).uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normalMatrix, normal.x(), normal.y(), normal.z()).endVertex();
        consumer.vertex(matrix, v2.x(), v2.y(), v2.z()).color(r, g, b, a).uv(0, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normalMatrix, normal.x(), normal.y(), normal.z()).endVertex();
        consumer.vertex(matrix, v3.x(), v3.y(), v3.z()).color(r, g, b, a).uv(1, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normalMatrix, normal.x(), normal.y(), normal.z()).endVertex();        consumer.vertex(matrix, v3.x(), v3.y(), v3.z()).color(r, g, b, a).uv(1, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normalMatrix, normal.x(), normal.y(), normal.z()).endVertex();
        consumer.vertex(matrix, v4.x(), v4.y(), v4.z()).color(r, g, b, a).uv(1, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normalMatrix, normal.x(), normal.y(), normal.z()).endVertex();
        consumer.vertex(matrix, v1.x(), v1.y(), v1.z()).color(r, g, b, a).uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normalMatrix, normal.x(), normal.y(), normal.z()).endVertex();
    }    private void drawRingPlane(PoseStack poseStack, VertexConsumer consumer, int packedLight) {
        Matrix4f matrix = poseStack.last().pose();
        consumer.vertex(matrix, -0.5F, -0.5F, 0).color(255, 255, 255, 255).uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(0, 0, 1).endVertex();
        consumer.vertex(matrix, 0.5F, -0.5F, 0).color(255, 255, 255, 255).uv(1, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(0, 0, 1).endVertex();
        consumer.vertex(matrix, 0.5F, 0.5F, 0).color(255, 255, 255, 255).uv(1, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(0, 0, 1).endVertex();
        consumer.vertex(matrix, -0.5F, 0.5F, 0).color(255, 255, 255, 255).uv(0, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(0, 0, 1).endVertex();
    }    @Override
    public ResourceLocation getTextureLocation(BlackBool entity) {
        return TEXTURE;
    }
}