package com.Maxwell.eschatology.register;

import com.Maxwell.eschatology.Boss.BlackBool.BlackBool;
import com.Maxwell.eschatology.Boss.BlackBool.Entities.EndLaser.DamageField.EndLaserBeamDMGFieldEntity;
import com.Maxwell.eschatology.Boss.BlackBool.Entities.EndLaser.EndLaserBeamEntity;
import com.Maxwell.eschatology.Boss.BlackBool.Entities.EventHorizon.EventHorizonControllerEntity;
import com.Maxwell.eschatology.Boss.BlackBool.Entities.GravityOrb.GravityOrbEntity;
import com.Maxwell.eschatology.Boss.BlackBool.Entities.LightOrb.LightOrbEntity;
import com.Maxwell.eschatology.Boss.BlackBool.Entities.Singularity.SingularityEntity;
import com.Maxwell.eschatology.Boss.BlackBool.Entities.VoidLance.VoidLanceEntity;
import com.Maxwell.eschatology.Boss.BlackBool.Entities.VoidLance.VoidRift.VoidRiftEntity;
import com.Maxwell.eschatology.Boss.BlackBool.Entities.VoidWave.VoidWaveEntity;
import com.Maxwell.eschatology.Boss.XF07_Revanant.Entities.ExoMisslie.ExoMissile;
import com.Maxwell.eschatology.Boss.XF07_Revanant.Entities.ExoSkull.ExoSkull;
import com.Maxwell.eschatology.Boss.XF07_Revanant.Entities.FrostStrikeField.FrostFieldEntity;
import com.Maxwell.eschatology.Boss.XF07_Revanant.Entities.SmallBeam.SmallBeamEntity;
import com.Maxwell.eschatology.Boss.XF07_Revanant.ExoWither;
import com.Maxwell.eschatology.Eschatology;
import com.Maxwell.eschatology.common.Items.FellBullet.Entity.FellBulletEntity;
import com.Maxwell.eschatology.common.Items.FellBullet.Entity.FellBulletShardEntity;
import com.Maxwell.eschatology.common.Items.MagicBulletWeapon.MagicBullet;
import com.Maxwell.eschatology.common.Items.WitchsCrest.Entity.WitchsSorn;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("removal")
public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Eschatology.MODID);
    public static final RegistryObject<EntityType<BlackBool>> BLACK_BOOL = ENTITY_TYPES.register("blackbool",
            () -> EntityType.Builder.of(BlackBool::new, MobCategory.MONSTER)
                    .sized(1.9f, 1.9f)
                    .clientTrackingRange(10)
                    .build(new ResourceLocation(Eschatology.MODID, "blackbool").toString()));
    public static final RegistryObject<EntityType<ExoWither>> EXO_WITHER = ENTITY_TYPES.register("exo_wither",
            () -> EntityType.Builder.of(ExoWither::new, MobCategory.MONSTER)
                    .sized(1.8f, 2.5f)
                    .clientTrackingRange(10)
                    .build(new ResourceLocation(Eschatology.MODID, "exo_wither").toString()));    public static final RegistryObject<EntityType<GravityOrbEntity>> GRAVITY_ORB =
            ENTITY_TYPES.register("gravity_orb",
                    () -> EntityType.Builder.<GravityOrbEntity>of(GravityOrbEntity::new, MobCategory.MISC)
                            .sized(1f, 1f)
                            .clientTrackingRange(10)
                            .updateInterval(1)
                            .build(new ResourceLocation(Eschatology.MODID, "gravity_orb").toString()));    public static final RegistryObject<EntityType<LightOrbEntity>> LIGHT_ORB =
            ENTITY_TYPES.register("light_orb",
                    () -> EntityType.Builder.<LightOrbEntity>of(LightOrbEntity::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .clientTrackingRange(10)
                            .updateInterval(1)
                            .build(new ResourceLocation(Eschatology.MODID, "light_orb").toString()));    public static final RegistryObject<EntityType<VoidWaveEntity>> VOID_WAVE =
            ENTITY_TYPES.register("void_wave",
                    () -> EntityType.Builder.<VoidWaveEntity>of(VoidWaveEntity::new, MobCategory.MISC)
                            .sized(1.0f, 0.5f)
                            .clientTrackingRange(10)
                            .updateInterval(1)
                            .build(new ResourceLocation(Eschatology.MODID, "void_wave").toString()));    public static final RegistryObject<EntityType<VoidLanceEntity>> VOID_LANCE =
            ENTITY_TYPES.register("void_lance",
                    () -> EntityType.Builder.<VoidLanceEntity>of(VoidLanceEntity::new, MobCategory.MISC)
                            .sized(1.0f, 2.5f)
                            .clientTrackingRange(10)
                            .updateInterval(1)
                            .build(new ResourceLocation(Eschatology.MODID, "void_lance").toString()));    public static final RegistryObject<EntityType<VoidRiftEntity>> VOID_RIFT =
            ENTITY_TYPES.register("void_rift",
                    () -> EntityType.Builder.<VoidRiftEntity>of(VoidRiftEntity::new, MobCategory.MISC)
                            .sized(1.0f, 2.5f)
                            .clientTrackingRange(10)
                            .updateInterval(1)
                            .build(new ResourceLocation(Eschatology.MODID, "void_rift").toString()));    public static final RegistryObject<EntityType<SingularityEntity>> SINGULARITY =
            ENTITY_TYPES.register("singularity",
                    () -> EntityType.Builder.<SingularityEntity>of(SingularityEntity::new, MobCategory.MISC)
                            .sized(1.0f, 2.5f)
                            .clientTrackingRange(10)
                            .updateInterval(1)
                            .build(new ResourceLocation(Eschatology.MODID, "singularity").toString()));    public static final RegistryObject<EntityType<EndLaserBeamEntity>> END_LASER_BEAM =
            ENTITY_TYPES.register("end_laser_beam",
                    () -> EntityType.Builder.<EndLaserBeamEntity>of(EndLaserBeamEntity::new, MobCategory.MISC)
                            .sized(1.0f, 2.5f)
                            .clientTrackingRange(10)
                            .updateInterval(1)
                            .build(new ResourceLocation(Eschatology.MODID, "end_laser_beam").toString()));
    public static final RegistryObject<EntityType<EndLaserBeamDMGFieldEntity>> END_LASER_BEAM_DAMAGE_FILED =
            ENTITY_TYPES.register("end_laser_beam_dmg_filed",
                    () -> EntityType.Builder.<EndLaserBeamDMGFieldEntity>of(EndLaserBeamDMGFieldEntity::new, MobCategory.MISC)
                            .sized(1.0f, 2.5f)
                            .clientTrackingRange(10)
                            .updateInterval(1)
                            .build(new ResourceLocation(Eschatology.MODID, "end_laser_beam_segments").toString()));
    public static final RegistryObject<EntityType<EventHorizonControllerEntity>> EVENT_HORIZON_CONTROLLER =
            ENTITY_TYPES.register("event_horizon",
                    () -> EntityType.Builder.<EventHorizonControllerEntity>of(EventHorizonControllerEntity::new, MobCategory.MISC)
                            .sized(1.1f, 1.1f)
                            .clientTrackingRange(10)
                            .updateInterval(1)
                            .build(new ResourceLocation(Eschatology.MODID, "event_horizon").toString()));    public static final RegistryObject<EntityType<SmallBeamEntity>> SMALL_BEAM =
            ENTITY_TYPES.register("small_beam",
                    () -> EntityType.Builder.<SmallBeamEntity>of(SmallBeamEntity::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .clientTrackingRange(10)
                            .updateInterval(1)
                            .build(new ResourceLocation(Eschatology.MODID, "small_beam").toString()));    public static final RegistryObject<EntityType<ExoMissile>> EXO_MISSILE =
            ENTITY_TYPES.register("exo_missile",
                    () -> EntityType.Builder.<ExoMissile>of(ExoMissile::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .clientTrackingRange(10)
                            .updateInterval(1)
                            .build(new ResourceLocation(Eschatology.MODID, "exo_missile").toString()));    public static final RegistryObject<EntityType<FrostFieldEntity>> FROST_FIELD =
            ENTITY_TYPES.register("frost_field",
                    () -> EntityType.Builder.<FrostFieldEntity>of(FrostFieldEntity::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .clientTrackingRange(10)
                            .updateInterval(1)
                            .build(new ResourceLocation(Eschatology.MODID, "frost_field").toString()));    public static final RegistryObject<EntityType<ExoSkull>> EXO_SKULL =
            ENTITY_TYPES.register("exo_skull",
                    () -> EntityType.Builder.<ExoSkull>of(ExoSkull::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .clientTrackingRange(10)
                            .updateInterval(1)
                            .build(new ResourceLocation(Eschatology.MODID, "exo_skull").toString()));    public static final RegistryObject<EntityType<WitchsSorn>> WITCHS_SORN =
            ENTITY_TYPES.register("witchs_sorn",
                    () -> EntityType.Builder.<WitchsSorn>of(WitchsSorn::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .clientTrackingRange(10)
                            .updateInterval(1)
                            .build(new ResourceLocation(Eschatology.MODID, "witchs_sorn").toString()));    public static final RegistryObject<EntityType<MagicBullet>> PENETRATING_BULLET =
            ENTITY_TYPES.register("penetrating_bullet",
                    () -> EntityType.Builder.of(MagicBullet::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build(new ResourceLocation(Eschatology.MODID, "penetrating_bullet").toString()));    public static final RegistryObject<EntityType<FellBulletEntity>> FELL_BULLET =
            ENTITY_TYPES.register("fell_bullet",
                    () -> EntityType.Builder.<FellBulletEntity>of(FellBulletEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build(new ResourceLocation(Eschatology.MODID, "fell_bullet").toString()));    public static final RegistryObject<EntityType<FellBulletShardEntity>> FELL_SHARD =
            ENTITY_TYPES.register("fell_shard",
                    () -> EntityType.Builder.<FellBulletShardEntity>of(FellBulletShardEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build(new ResourceLocation(Eschatology.MODID, "fell_shard").toString()));
}