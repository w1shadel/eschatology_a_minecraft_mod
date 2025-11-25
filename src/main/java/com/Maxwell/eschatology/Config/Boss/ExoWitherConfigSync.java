package com.Maxwell.eschatology.Config.Boss;

import com.Maxwell.eschatology.Balance.ExoWitherBalance;
import com.Maxwell.eschatology.Eschatology;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = Eschatology.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ExoWitherConfigSync {

    @SubscribeEvent
    public static void onModConfigEvent(ModConfigEvent event) {
        if (event.getConfig().getSpec() == ExoWitherConfig.SPEC) {
            bakeConfig();
        }
    }

    private static void bakeConfig() {

        ExoWitherBalance.MAX_HEALTH = ExoWitherConfig.MAX_HEALTH.get();
        ExoWitherBalance.ATTACK_DAMAGE = ExoWitherConfig.ATTACK_DAMAGE.get();
        ExoWitherBalance.MOVEMENT_SPEED = ExoWitherConfig.MOVEMENT_SPEED.get();
        ExoWitherBalance.FLYING_SPEED = ExoWitherConfig.FLYING_SPEED.get();
        ExoWitherBalance.FOLLOW_RANGE = ExoWitherConfig.FOLLOW_RANGE.get();
        ExoWitherBalance.BLACKBOOL_DETECT_RADIUS = ExoWitherConfig.BLACKBOOL_DETECT_RADIUS.get();
        ExoWitherBalance.ARMOR = ExoWitherConfig.ARMOR.get();
        ExoWitherBalance.ARMOR_TOUGHNESS = ExoWitherConfig.ARMOR_TOUGHNESS.get();
        ExoWitherBalance.CHARGE_COOLDOWN_BASE = ExoWitherConfig.CHARGE_COOLDOWN_BASE.get();
        ExoWitherBalance.MISSILE_ATTACK_COOLDOWN_BASE = ExoWitherConfig.MISSILE_ATTACK_COOLDOWN_BASE.get();
        ExoWitherBalance.SIMPLE_LASER_COOLDOWN_BASE = ExoWitherConfig.SIMPLE_LASER_COOLDOWN_BASE.get();
        ExoWitherBalance.LASER_BEAM_COOLDOWN_BASE = ExoWitherConfig.LASER_BEAM_COOLDOWN_BASE.get();
        ExoWitherBalance.MORTAR_BARRAGE_COOLDOWN_BASE = ExoWitherConfig.MORTAR_BARRAGE_COOLDOWN_BASE.get();
        ExoWitherBalance.EXO_SKULL_ATTACK_COOLDOWN = ExoWitherConfig.EXO_SKULL_ATTACK_COOLDOWN.get();

        ExoWitherBalance.SHIELD_INITIAL_HEALTH = ExoWitherConfig.SHIELD_INITIAL_HEALTH.get();
        ExoWitherBalance.SHIELD_DEPLOY_HP_THRESHOLD = ExoWitherConfig.SHIELD_DEPLOY_HP_THRESHOLD.get();
        ExoWitherBalance.ENRAGE_HP_THRESHOLD = ExoWitherConfig.ENRAGE_HP_THRESHOLD.get();

        ExoWitherBalance.PROJECTILE_SPEED = ExoWitherConfig.PROJECTILE_SPEED.get();
        ExoWitherBalance.SMALL_LASER_PROJECTILE_SPEED = ExoWitherConfig.SMALL_LASER_PROJECTILE_SPEED.get();
        ExoWitherBalance.SMALL_LASER_DAMAGE_RATIO = ExoWitherConfig.SMALL_LASER_DAMAGE_RATIO.get().floatValue();
        ExoWitherBalance.EXO_SKULL_DIRECT_DAMAGE_RATIO = ExoWitherConfig.EXO_SKULL_DIRECT_DAMAGE_RATIO.get().floatValue();
        ExoWitherBalance.EXO_SKULL_EXPLOSION_DAMAGE_RATIO = ExoWitherConfig.EXO_SKULL_EXPLOSION_DAMAGE_RATIO.get().floatValue();
        ExoWitherBalance.IDEAL_DISTANCE_TO_TARGET = ExoWitherConfig.IDEAL_DISTANCE_TO_TARGET.get();
        ExoWitherBalance.MOVE_SPEED_SCALE = ExoWitherConfig.MOVE_SPEED_SCALE.get();
        ExoWitherBalance.HOVER_Y_AMPLITUDE = ExoWitherConfig.HOVER_Y_AMPLITUDE.get();
        ExoWitherBalance.HOVER_Y_SPEED = ExoWitherConfig.HOVER_Y_SPEED.get();
        ExoWitherBalance.Y_ADJUST_SPEED = ExoWitherConfig.Y_ADJUST_SPEED.get();
        ExoWitherBalance.Y_CLAMP = ExoWitherConfig.Y_CLAMP.get();

        ExoWitherBalance.PASSIVE_ATTACK_INTERVAL_TICKS = ExoWitherConfig.PASSIVE_ATTACK_INTERVAL_TICKS.get();
        ExoWitherBalance.PASSIVE_SMALL_BEAM_DAMAGE = ExoWitherConfig.PASSIVE_SMALL_BEAM_DAMAGE.get().floatValue();
        ExoWitherBalance.PASSIVE_EXO_SKULL_DIRECT_DAMAGE = ExoWitherConfig.PASSIVE_EXO_SKULL_DIRECT_DAMAGE.get().floatValue();
        ExoWitherBalance.PASSIVE_EXO_SKULL_EXPLOSION_DAMAGE = ExoWitherConfig.PASSIVE_EXO_SKULL_EXPLOSION_DAMAGE.get().floatValue();
        ExoWitherBalance.PASSIVE_SKULL_SPAWN_CHANCE = ExoWitherConfig.PASSIVE_SKULL_SPAWN_CHANCE.get().floatValue();

        ExoWitherBalance.CHARGE_PREPARE_DURATION_TICKS = ExoWitherConfig.CHARGE_PREPARE_DURATION_TICKS.get();
        ExoWitherBalance.CHARGE_MAX_DURATION_TICKS = ExoWitherConfig.CHARGE_MAX_DURATION_TICKS.get();
        ExoWitherBalance.CHARGE_MAX_COLLISIONS = ExoWitherConfig.CHARGE_MAX_COLLISIONS.get();
        ExoWitherBalance.CHARGE_INITIAL_SPEED = ExoWitherConfig.CHARGE_INITIAL_SPEED.get();
        ExoWitherBalance.CHARGE_LEAD_PREDICTION_FACTOR = ExoWitherConfig.CHARGE_LEAD_PREDICTION_FACTOR.get();
        ExoWitherBalance.CHARGE_BLOCK_BREAK_RADIUS = ExoWitherConfig.CHARGE_BLOCK_BREAK_RADIUS.get();
        ExoWitherBalance.CHARGE_PENETRATION_DAMAGE_RATIO = ExoWitherConfig.CHARGE_PENETRATION_DAMAGE_RATIO.get().floatValue();
        ExoWitherBalance.CHARGE_SHOCKWAVE_DAMAGE_RATIO = ExoWitherConfig.CHARGE_SHOCKWAVE_DAMAGE_RATIO.get().floatValue();
        ExoWitherBalance.CHARGE_SHOCKWAVE_RADIUS = ExoWitherConfig.CHARGE_SHOCKWAVE_RADIUS.get();
        ExoWitherBalance.CHARGE_SHOCKWAVE_KNOCKBACK = ExoWitherConfig.CHARGE_SHOCKWAVE_KNOCKBACK.get().floatValue();
        ExoWitherBalance.CHARGE_SPEED_REDUCTION_ON_HIT = ExoWitherConfig.CHARGE_SPEED_REDUCTION_ON_HIT.get();
        ExoWitherBalance.CHARGE_HITBOX_INFLATE_XZ = ExoWitherConfig.CHARGE_HITBOX_INFLATE_XZ.get();
        ExoWitherBalance.CHARGE_HITBOX_INFLATE_Y = ExoWitherConfig.CHARGE_HITBOX_INFLATE_Y.get();
        ExoWitherBalance.CHARGE_MIN_DIST_SQ_NORMAL = ExoWitherConfig.CHARGE_MIN_DIST_SQ_NORMAL.get();
        ExoWitherBalance.CHARGE_MAX_DIST_SQ_NORMAL = ExoWitherConfig.CHARGE_MAX_DIST_SQ_NORMAL.get();
        ExoWitherBalance.CHARGE_MIN_DIST_SQ_ENRAGED = ExoWitherConfig.CHARGE_MIN_DIST_SQ_ENRAGED.get();
        ExoWitherBalance.CHARGE_MAX_DIST_SQ_ENRAGED = ExoWitherConfig.CHARGE_MAX_DIST_SQ_ENRAGED.get();
        ExoWitherBalance.CHARGE_CANCEL_DIST_SQ = ExoWitherConfig.CHARGE_CANCEL_DIST_SQ.get();

        ExoWitherBalance.LASER_BEAM_CHARGE_UP_TICKS = ExoWitherConfig.LASER_BEAM_CHARGE_UP_TICKS.get();
        ExoWitherBalance.LASER_BEAM_FIRE_DURATION_TICKS = ExoWitherConfig.LASER_BEAM_FIRE_DURATION_TICKS.get();
        ExoWitherBalance.LASER_BEAM_TOTAL_DURATION_TICKS = ExoWitherConfig.LASER_BEAM_TOTAL_DURATION_TICKS.get();
        ExoWitherBalance.LASER_BEAM_DAMAGE = ExoWitherConfig.LASER_BEAM_DAMAGE.get().floatValue();

        ExoWitherBalance.MISSILE_MIN_DIST_SQ = ExoWitherConfig.MISSILE_MIN_DIST_SQ.get();
        ExoWitherBalance.MISSILE_COUNT_PER_SIDE = ExoWitherConfig.MISSILE_COUNT_PER_SIDE.get();
        ExoWitherBalance.MISSILE_FIRE_INTERVAL_NORMAL = ExoWitherConfig.MISSILE_FIRE_INTERVAL_NORMAL.get();
        ExoWitherBalance.MISSILE_FIRE_INTERVAL_ENRAGED = ExoWitherConfig.MISSILE_FIRE_INTERVAL_ENRAGED.get();
        ExoWitherBalance.MISSILE_SPAWN_SIDE_OFFSET = ExoWitherConfig.MISSILE_SPAWN_SIDE_OFFSET.get();
        ExoWitherBalance.MISSILE_SPAWN_RANDOM_OFFSET = ExoWitherConfig.MISSILE_SPAWN_RANDOM_OFFSET.get();
        ExoWitherBalance.MISSILE_INITIAL_VERTICAL_SPEED = ExoWitherConfig.MISSILE_INITIAL_VERTICAL_SPEED.get();

        ExoWitherBalance.MORTAR_MIN_DIST_SQ = ExoWitherConfig.MORTAR_MIN_DIST_SQ.get();
        ExoWitherBalance.MORTAR_PREPARE_DURATION_TICKS = ExoWitherConfig.MORTAR_PREPARE_DURATION_TICKS.get();
        ExoWitherBalance.MORTAR_FIRE_INTERVAL_TICKS = ExoWitherConfig.MORTAR_FIRE_INTERVAL_TICKS.get();
        ExoWitherBalance.MORTAR_TOTAL_SHOTS = ExoWitherConfig.MORTAR_TOTAL_SHOTS.get();
        ExoWitherBalance.MORTAR_INDICATOR_RADIUS = ExoWitherConfig.MORTAR_INDICATOR_RADIUS.get();
        ExoWitherBalance.MORTAR_IMPACT_RANDOM_RADIUS = ExoWitherConfig.MORTAR_IMPACT_RANDOM_RADIUS.get();
        ExoWitherBalance.MORTAR_IMPACT_AREA_SIZE = ExoWitherConfig.MORTAR_IMPACT_AREA_SIZE.get();
        ExoWitherBalance.MORTAR_DAMAGE_RATIO = ExoWitherConfig.MORTAR_DAMAGE_RATIO.get().floatValue();

        ExoWitherBalance.SIMPLE_LASER_START_TICK_NORMAL = ExoWitherConfig.SIMPLE_LASER_START_TICK_NORMAL.get();
        ExoWitherBalance.SIMPLE_LASER_END_TICK_NORMAL = ExoWitherConfig.SIMPLE_LASER_END_TICK_NORMAL.get();
        ExoWitherBalance.SIMPLE_LASER_FIRE_INTERVAL_NORMAL = ExoWitherConfig.SIMPLE_LASER_FIRE_INTERVAL_NORMAL.get();
        ExoWitherBalance.SIMPLE_LASER_START_TICK_ENRAGED = ExoWitherConfig.SIMPLE_LASER_START_TICK_ENRAGED.get();
        ExoWitherBalance.SIMPLE_LASER_END_TICK_ENRAGED = ExoWitherConfig.SIMPLE_LASER_END_TICK_ENRAGED.get();
        ExoWitherBalance.SIMPLE_LASER_FIRE_INTERVAL_ENRAGED = ExoWitherConfig.SIMPLE_LASER_FIRE_INTERVAL_ENRAGED.get();

        ExoWitherBalance.EXO_MISSILE_BASE_DAMAGE = ExoWitherConfig.EXO_MISSILE_BASE_DAMAGE.get().floatValue();
        ExoWitherBalance.EXO_MISSILE_SPLIT_DAMAGE_RATIO = ExoWitherConfig.EXO_MISSILE_SPLIT_DAMAGE_RATIO.get().floatValue();
        ExoWitherBalance.EXO_MISSILE_SPLIT_MIN_DAMAGE = ExoWitherConfig.EXO_MISSILE_SPLIT_MIN_DAMAGE.get().floatValue();
        ExoWitherBalance.EXO_MISSILE_MAX_LIFE = ExoWitherConfig.EXO_MISSILE_MAX_LIFE.get();
        ExoWitherBalance.EXO_MISSILE_INITIAL_SPEED_SCALE = ExoWitherConfig.EXO_MISSILE_INITIAL_SPEED_SCALE.get();
        ExoWitherBalance.EXO_MISSILE_MIN_SPEED = ExoWitherConfig.EXO_MISSILE_MIN_SPEED.get();
        ExoWitherBalance.EXO_MISSILE_MAX_SPEED = ExoWitherConfig.EXO_MISSILE_MAX_SPEED.get();
        ExoWitherBalance.EXO_MISSILE_ACCELERATION_FACTOR = ExoWitherConfig.EXO_MISSILE_ACCELERATION_FACTOR.get();
        ExoWitherBalance.EXO_MISSILE_TURN_LERP_FACTOR = ExoWitherConfig.EXO_MISSILE_TURN_LERP_FACTOR.get();
        ExoWitherBalance.EXO_MISSILE_EXPLOSION_DAMAGE_MULTIPLIER = ExoWitherConfig.EXO_MISSILE_EXPLOSION_DAMAGE_MULTIPLIER.get().floatValue();
        ExoWitherBalance.EXO_MISSILE_EXPLOSION_RADIUS_XZ = ExoWitherConfig.EXO_MISSILE_EXPLOSION_RADIUS_XZ.get();
        ExoWitherBalance.EXO_MISSILE_EXPLOSION_RADIUS_Y_BELOW = ExoWitherConfig.EXO_MISSILE_EXPLOSION_RADIUS_Y_BELOW.get();
        ExoWitherBalance.EXO_MISSILE_EXPLOSION_RADIUS_Y_ABOVE = ExoWitherConfig.EXO_MISSILE_EXPLOSION_RADIUS_Y_ABOVE.get();
        ExoWitherBalance.EXO_MISSILE_SPLIT_COUNT = ExoWitherConfig.EXO_MISSILE_SPLIT_COUNT.get();
        ExoWitherBalance.EXO_MISSILE_SPLIT_YAW_OFFSET = ExoWitherConfig.EXO_MISSILE_SPLIT_YAW_OFFSET.get().floatValue();
        ExoWitherBalance.EXO_MISSILE_SPLIT_VERTICAL_OFFSET = ExoWitherConfig.EXO_MISSILE_SPLIT_VERTICAL_OFFSET.get();
        ExoWitherBalance.EXO_MISSILE_SPLIT_SPEED_SCALE = ExoWitherConfig.EXO_MISSILE_SPLIT_SPEED_SCALE.get();
        ExoWitherBalance.EXO_MISSILE_FREEZING_STRIKE_DURATION = ExoWitherConfig.EXO_MISSILE_FREEZING_STRIKE_DURATION.get();

        ExoWitherBalance.EXO_SKULL_DIRECT_DAMAGE = ExoWitherConfig.EXO_SKULL_DIRECT_DAMAGE.get().floatValue();
        ExoWitherBalance.EXO_SKULL_EXPLOSION_DAMAGE = ExoWitherConfig.EXO_SKULL_EXPLOSION_DAMAGE.get().floatValue();
        ExoWitherBalance.EXO_SKULL_MAX_LIFE = ExoWitherConfig.EXO_SKULL_MAX_LIFE.get();
        ExoWitherBalance.EXO_SKULL_SPEED_SCALE = ExoWitherConfig.EXO_SKULL_SPEED_SCALE.get();
        ExoWitherBalance.EXO_SKULL_TURN_LERP_FACTOR = ExoWitherConfig.EXO_SKULL_TURN_LERP_FACTOR.get();
        ExoWitherBalance.EXO_SKULL_TARGET_HEIGHT_OFFSET = ExoWitherConfig.EXO_SKULL_TARGET_HEIGHT_OFFSET.get();
        ExoWitherBalance.EXO_SKULL_BASE_EXPLOSION_RADIUS = ExoWitherConfig.EXO_SKULL_BASE_EXPLOSION_RADIUS.get().floatValue();
        ExoWitherBalance.EXO_SKULL_LIFESPAN_RADIUS_SCALE = ExoWitherConfig.EXO_SKULL_LIFESPAN_RADIUS_SCALE.get().floatValue();
        ExoWitherBalance.EXO_SKULL_FREEZING_STRIKE_DURATION_DIRECT = ExoWitherConfig.EXO_SKULL_FREEZING_STRIKE_DURATION_DIRECT.get();
        ExoWitherBalance.EXO_SKULL_FREEZING_STRIKE_DURATION_EXPLOSION = ExoWitherConfig.EXO_SKULL_FREEZING_STRIKE_DURATION_EXPLOSION.get();
        ExoWitherBalance.EXO_SKULL_FREEZING_STRIKE_AMPLIFIER_EXPLOSION = ExoWitherConfig.EXO_SKULL_FREEZING_STRIKE_AMPLIFIER_EXPLOSION.get();

        ExoWitherBalance.FROST_FIELD_DURATION = ExoWitherConfig.FROST_FIELD_DURATION.get();
        ExoWitherBalance.FROST_FIELD_RADIUS = ExoWitherConfig.FROST_FIELD_RADIUS.get().floatValue();
        ExoWitherBalance.FROST_FIELD_EFFECT_INTERVAL = ExoWitherConfig.FROST_FIELD_EFFECT_INTERVAL.get();
        ExoWitherBalance.FROST_FIELD_EFFECT_BASE_DURATION = ExoWitherConfig.FROST_FIELD_EFFECT_BASE_DURATION.get();
        ExoWitherBalance.FROST_FIELD_EFFECT_DURATION_INCREASE = ExoWitherConfig.FROST_FIELD_EFFECT_DURATION_INCREASE.get();
        ExoWitherBalance.FROST_FIELD_EFFECT_MAX_DURATION = ExoWitherConfig.FROST_FIELD_EFFECT_MAX_DURATION.get();
        ExoWitherBalance.FROST_FIELD_EFFECT_MAX_AMPLIFIER = ExoWitherConfig.FROST_FIELD_EFFECT_MAX_AMPLIFIER.get();

        ExoWitherBalance.SMALL_BEAM_BASE_DAMAGE = ExoWitherConfig.SMALL_BEAM_BASE_DAMAGE.get().floatValue();
        ExoWitherBalance.SMALL_BEAM_MAX_LIFE = ExoWitherConfig.SMALL_BEAM_MAX_LIFE.get();
        ExoWitherBalance.SMALL_BEAM_ENRAGED_PIERCE_COUNT = ExoWitherConfig.SMALL_BEAM_ENRAGED_PIERCE_COUNT.get();
        ExoWitherBalance.SMALL_BEAM_EFFECT_BASE_DURATION = ExoWitherConfig.SMALL_BEAM_EFFECT_BASE_DURATION.get();
        ExoWitherBalance.SMALL_BEAM_EFFECT_DAMAGE_DURATION_MULTIPLIER = ExoWitherConfig.SMALL_BEAM_EFFECT_DAMAGE_DURATION_MULTIPLIER.get();
        ExoWitherBalance.SMALL_BEAM_EFFECT_AMPLIFIER_NORMAL = ExoWitherConfig.SMALL_BEAM_EFFECT_AMPLIFIER_NORMAL.get();
        ExoWitherBalance.SMALL_BEAM_EFFECT_AMPLIFIER_ENRAGED = ExoWitherConfig.SMALL_BEAM_EFFECT_AMPLIFIER_ENRAGED.get();

        ExoWitherBalance.FROZEN_EXOHEART_PERCENT = ExoWitherConfig.FROZEN_EXOHEART_PERCENT.get().floatValue();
    }
}