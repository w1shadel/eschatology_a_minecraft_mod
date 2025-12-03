package com.Maxwell.eschatology.Config.Boss;import com.Maxwell.eschatology.Balance.BlackBoolBalance;
import com.Maxwell.eschatology.Eschatology;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;@Mod.EventBusSubscriber(modid = Eschatology.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlackBoolConfigSync {    @SubscribeEvent
    public static void onModConfigEvent(ModConfigEvent event) {
        if (event.getConfig().getSpec() == BlackBoolConfig.SPEC) {
            bakeConfig();
        }
    }    private static void bakeConfig() {        BlackBoolBalance.MAX_HEALTH = BlackBoolConfig.MAX_HEALTH.get();
        BlackBoolBalance.MOVEMENT_SPEED = BlackBoolConfig.MOVEMENT_SPEED.get();
        BlackBoolBalance.FLYING_SPEED = BlackBoolConfig.FLYING_SPEED.get();
        BlackBoolBalance.ATTACK_DAMAGE = BlackBoolConfig.ATTACK_DAMAGE.get();
        BlackBoolBalance.ATTACK_SPEED = BlackBoolConfig.ATTACK_SPEED.get();
        BlackBoolBalance.FOLLOW_RANGE = BlackBoolConfig.FOLLOW_RANGE.get();
        BlackBoolBalance.KNOCKBACK_RESISTANCE = BlackBoolConfig.KNOCKBACK_RESISTANCE.get();
        BlackBoolBalance.ARMOR = BlackBoolConfig.ARMOR.get();        BlackBoolBalance.CONTACT_DAMAGE_RATIO = BlackBoolConfig.CONTACT_DAMAGE_RATIO.get().floatValue();        BlackBoolBalance.SPAWN_ANIMATION_DURATION_TICKS = BlackBoolConfig.SPAWN_ANIMATION_DURATION_TICKS.get();
        BlackBoolBalance.DEATH_PHASE1_OUTBURST_TICKS = BlackBoolConfig.DEATH_PHASE1_OUTBURST_TICKS.get();
        BlackBoolBalance.DEATH_PHASE2_COLLAPSE_TICKS = BlackBoolConfig.DEATH_PHASE2_COLLAPSE_TICKS.get();
        BlackBoolBalance.DEATH_PHASE3_IMPLOSION_TICKS = BlackBoolConfig.DEATH_PHASE3_IMPLOSION_TICKS.get();
        BlackBoolBalance.DEATH_PHASE4_FINAL_TICKS = BlackBoolConfig.DEATH_PHASE4_FINAL_TICKS.get();        BlackBoolBalance.DEATH_PULL_AREA_RADIUS = BlackBoolConfig.DEATH_PULL_AREA_RADIUS.get();
        BlackBoolBalance.DEATH_PULL_STRENGTH = BlackBoolConfig.DEATH_PULL_STRENGTH.get().floatValue();        BlackBoolBalance.STANCE_SWITCH_MELEE_RANGE_SQ = BlackBoolConfig.STANCE_SWITCH_MELEE_RANGE_SQ.get();
        BlackBoolBalance.STANCE_SWITCH_RANGED_RANGE_SQ = BlackBoolConfig.STANCE_SWITCH_RANGED_RANGE_SQ.get();
        BlackBoolBalance.STANCE_SWITCH_COOLDOWN_MIN = BlackBoolConfig.STANCE_SWITCH_COOLDOWN_MIN.get();
        BlackBoolBalance.STANCE_SWITCH_COOLDOWN_RANDOM = BlackBoolConfig.STANCE_SWITCH_COOLDOWN_RANDOM.get();
        BlackBoolBalance.STANCE_SWITCH_FORCED_COOLDOWN_MIN = BlackBoolConfig.STANCE_SWITCH_FORCED_COOLDOWN_MIN.get();
        BlackBoolBalance.STANCE_SWITCH_FORCED_COOLDOWN_RANDOM = BlackBoolConfig.STANCE_SWITCH_FORCED_COOLDOWN_RANDOM.get();
        BlackBoolBalance.MELEE_STANCE_IDEAL_DISTANCE = BlackBoolConfig.MELEE_STANCE_IDEAL_DISTANCE.get();
        BlackBoolBalance.RANGED_STANCE_IDEAL_DISTANCE = BlackBoolConfig.RANGED_STANCE_IDEAL_DISTANCE.get();
        BlackBoolBalance.MELEE_STANCE_SPEED_SCALE = BlackBoolConfig.MELEE_STANCE_SPEED_SCALE.get();
        BlackBoolBalance.RANGED_STANCE_SPEED_SCALE = BlackBoolConfig.RANGED_STANCE_SPEED_SCALE.get();
        BlackBoolBalance.RANDOM_FLY_RADIUS = BlackBoolConfig.RANDOM_FLY_RADIUS.get();        BlackBoolBalance.PHASE_TWO_HP_THRESHOLD = BlackBoolConfig.PHASE_TWO_HP_THRESHOLD.get().floatValue();
        BlackBoolBalance.CRITICAL_ENRAGE_HP_THRESHOLD = BlackBoolConfig.CRITICAL_ENRAGE_HP_THRESHOLD.get().floatValue();
        BlackBoolBalance.LANCE_SPAWN_START_HP_THRESHOLD = BlackBoolConfig.LANCE_SPAWN_START_HP_THRESHOLD.get().floatValue();        BlackBoolBalance.CHARGE_COOLDOWN_BASE = BlackBoolConfig.CHARGE_COOLDOWN_BASE.get();
        BlackBoolBalance.GRAVITY_ORB_COOLDOWN_BASE = BlackBoolConfig.GRAVITY_ORB_COOLDOWN_BASE.get();
        BlackBoolBalance.BARRAGE_COOLDOWN_BASE = BlackBoolConfig.BARRAGE_COOLDOWN_BASE.get();
        BlackBoolBalance.LASER_COOLDOWN_BASE = BlackBoolConfig.LASER_COOLDOWN_BASE.get();
        BlackBoolBalance.PASSIVE_ORB_COOLDOWN_BASE = BlackBoolConfig.PASSIVE_ORB_COOLDOWN_BASE.get();        BlackBoolBalance.MAX_ORBITING_PROJECTILES = BlackBoolConfig.MAX_ORBITING_PROJECTILES.get();
        BlackBoolBalance.PROJECTILE_CAPTURE_RADIUS = BlackBoolConfig.PROJECTILE_CAPTURE_RADIUS.get();
        BlackBoolBalance.DAMAGE_CAP_PER_TICK = BlackBoolConfig.DAMAGE_CAP_PER_TICK.get().floatValue();
        BlackBoolBalance.DAMAGE_CAP_REDUCTION_RATIO = BlackBoolConfig.DAMAGE_CAP_REDUCTION_RATIO.get().floatValue();        BlackBoolBalance.CHARGE_MIN_DISTANCE_SQ = BlackBoolConfig.CHARGE_MIN_DISTANCE_SQ.get();
        BlackBoolBalance.CHARGE_MAX_DISTANCE_SQ = BlackBoolConfig.CHARGE_MAX_DISTANCE_SQ.get();
        BlackBoolBalance.CHARGE_PREPARATION_TICKS = BlackBoolConfig.CHARGE_PREPARATION_TICKS.get();
        BlackBoolBalance.CHARGE_TIMEOUT_TICKS = BlackBoolConfig.CHARGE_TIMEOUT_TICKS.get();
        BlackBoolBalance.CHARGE_SPEED = BlackBoolConfig.CHARGE_SPEED.get();
        BlackBoolBalance.CHARGE_MAX_TRAVEL_DISTANCE = BlackBoolConfig.CHARGE_MAX_TRAVEL_DISTANCE.get();
        BlackBoolBalance.CHARGE_END_EXPLOSION_KNOCKBACK = BlackBoolConfig.CHARGE_END_EXPLOSION_KNOCKBACK.get().floatValue();
        BlackBoolBalance.CHARGE_END_EXPLOSION_RADIUS = BlackBoolConfig.CHARGE_END_EXPLOSION_RADIUS.get();
        BlackBoolBalance.CHARGE_END_EXPLOSION_DAMAGE_RATIO = BlackBoolConfig.CHARGE_END_EXPLOSION_DAMAGE_RATIO.get().floatValue();
        BlackBoolBalance.CHARGE_COUNT_NORMAL = BlackBoolConfig.CHARGE_COUNT_NORMAL.get();
        BlackBoolBalance.CHARGE_COUNT_ENRAGED = BlackBoolConfig.CHARGE_COUNT_ENRAGED.get();        BlackBoolBalance.GRAVITY_ORB_MIN_DISTANCE_SQ = BlackBoolConfig.GRAVITY_ORB_MIN_DISTANCE_SQ.get();
        BlackBoolBalance.GRAVITY_ORB_CHARGE_UP_TICKS = BlackBoolConfig.GRAVITY_ORB_CHARGE_UP_TICKS.get();
        BlackBoolBalance.BARRAGE_MIN_DISTANCE_SQ = BlackBoolConfig.BARRAGE_MIN_DISTANCE_SQ.get();
        BlackBoolBalance.BARRAGE_CHARGE_UP_TICKS = BlackBoolConfig.BARRAGE_CHARGE_UP_TICKS.get();
        BlackBoolBalance.BARRAGE_DURATION_NORMAL = BlackBoolConfig.BARRAGE_DURATION_NORMAL.get();
        BlackBoolBalance.BARRAGE_FIRE_RATE_NORMAL = BlackBoolConfig.BARRAGE_FIRE_RATE_NORMAL.get();
        BlackBoolBalance.BARRAGE_DURATION_ENRAGED = BlackBoolConfig.BARRAGE_DURATION_ENRAGED.get();
        BlackBoolBalance.BARRAGE_FIRE_RATE_ENRAGED = BlackBoolConfig.BARRAGE_FIRE_RATE_ENRAGED.get();        BlackBoolBalance.LASER_HP_THRESHOLD = BlackBoolConfig.LASER_HP_THRESHOLD.get().floatValue();
        BlackBoolBalance.LASER_CHARGE_UP_TICKS = BlackBoolConfig.LASER_CHARGE_UP_TICKS.get();
        BlackBoolBalance.LASER_DURATION_TICKS = BlackBoolConfig.LASER_DURATION_TICKS.get();
        BlackBoolBalance.LASER_DAMAGE_RATIO = BlackBoolConfig.LASER_DAMAGE_RATIO.get().floatValue();
        BlackBoolBalance.LASER_FIELD_HP_THRESHOLD = BlackBoolConfig.LASER_FIELD_HP_THRESHOLD.get().floatValue();        BlackBoolBalance.EVENT_HORIZON_HP_THRESHOLD = BlackBoolConfig.EVENT_HORIZON_HP_THRESHOLD.get().floatValue();
        BlackBoolBalance.EVENT_HORIZON_CAST_TIME_TICKS = BlackBoolConfig.EVENT_HORIZON_CAST_TIME_TICKS.get();
        BlackBoolBalance.REVENANT_DETECT_RADIUS = BlackBoolConfig.REVENANT_DETECT_RADIUS.get();
        BlackBoolBalance.PROJECTILE_RELEASE_INTERVAL = BlackBoolConfig.PROJECTILE_RELEASE_INTERVAL.get().floatValue();
        BlackBoolBalance.AFTER_SUMMON_MUST_RAGE_MODE = BlackBoolConfig.AFTER_SUMMON_MUST_RAGE_MODE.get();
        BlackBoolBalance.SING_CORE_PERCENT = BlackBoolConfig.SING_CORE_PERCENT.get().floatValue();
        BlackBoolBalance.LASER_TRACKING_SPPED = BlackBoolConfig.LASER_TRACKING_SPEED.get();
    }
}