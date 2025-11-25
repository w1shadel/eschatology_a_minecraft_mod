package com.Maxwell.eschatology.Config.Boss;

import net.minecraftforge.common.ForgeConfigSpec;

public class BlackBoolConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.DoubleValue MAX_HEALTH;
    public static final ForgeConfigSpec.DoubleValue MOVEMENT_SPEED;
    public static final ForgeConfigSpec.DoubleValue FLYING_SPEED;
    public static final ForgeConfigSpec.DoubleValue ATTACK_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue ATTACK_SPEED;
    public static final ForgeConfigSpec.DoubleValue FOLLOW_RANGE;
    public static final ForgeConfigSpec.DoubleValue KNOCKBACK_RESISTANCE;
    public static final ForgeConfigSpec.DoubleValue ARMOR;
    public static final ForgeConfigSpec.DoubleValue CONTACT_DAMAGE_RATIO;

    public static final ForgeConfigSpec.IntValue SPAWN_ANIMATION_DURATION_TICKS;
    public static final ForgeConfigSpec.IntValue DEATH_PHASE1_OUTBURST_TICKS;
    public static final ForgeConfigSpec.IntValue DEATH_PHASE2_COLLAPSE_TICKS;
    public static final ForgeConfigSpec.IntValue DEATH_PHASE3_IMPLOSION_TICKS;
    public static final ForgeConfigSpec.IntValue DEATH_PHASE4_FINAL_TICKS;
    public static final ForgeConfigSpec.DoubleValue DEATH_PULL_AREA_RADIUS;
    public static final ForgeConfigSpec.DoubleValue DEATH_PULL_STRENGTH;

    public static final ForgeConfigSpec.DoubleValue STANCE_SWITCH_MELEE_RANGE_SQ;
    public static final ForgeConfigSpec.DoubleValue STANCE_SWITCH_RANGED_RANGE_SQ;
    public static final ForgeConfigSpec.IntValue STANCE_SWITCH_COOLDOWN_MIN;
    public static final ForgeConfigSpec.IntValue STANCE_SWITCH_COOLDOWN_RANDOM;
    public static final ForgeConfigSpec.IntValue STANCE_SWITCH_FORCED_COOLDOWN_MIN;
    public static final ForgeConfigSpec.IntValue STANCE_SWITCH_FORCED_COOLDOWN_RANDOM;
    public static final ForgeConfigSpec.DoubleValue MELEE_STANCE_IDEAL_DISTANCE;
    public static final ForgeConfigSpec.DoubleValue RANGED_STANCE_IDEAL_DISTANCE;
    public static final ForgeConfigSpec.DoubleValue MELEE_STANCE_SPEED_SCALE;
    public static final ForgeConfigSpec.DoubleValue RANGED_STANCE_SPEED_SCALE;
    public static final ForgeConfigSpec.DoubleValue RANDOM_FLY_RADIUS;

    public static final ForgeConfigSpec.DoubleValue PHASE_TWO_HP_THRESHOLD;
    public static final ForgeConfigSpec.DoubleValue CRITICAL_ENRAGE_HP_THRESHOLD;
    public static final ForgeConfigSpec.DoubleValue LANCE_SPAWN_START_HP_THRESHOLD;

    public static final ForgeConfigSpec.IntValue CHARGE_COOLDOWN_BASE;
    public static final ForgeConfigSpec.IntValue GRAVITY_ORB_COOLDOWN_BASE;
    public static final ForgeConfigSpec.IntValue BARRAGE_COOLDOWN_BASE;
    public static final ForgeConfigSpec.IntValue LASER_COOLDOWN_BASE;
    public static final ForgeConfigSpec.IntValue PASSIVE_ORB_COOLDOWN_BASE;

    public static final ForgeConfigSpec.IntValue MAX_ORBITING_PROJECTILES;
    public static final ForgeConfigSpec.DoubleValue PROJECTILE_CAPTURE_RADIUS;
    public static final ForgeConfigSpec.DoubleValue DAMAGE_CAP_PER_TICK;
    public static final ForgeConfigSpec.DoubleValue DAMAGE_CAP_REDUCTION_RATIO;

    public static final ForgeConfigSpec.DoubleValue CHARGE_MIN_DISTANCE_SQ;
    public static final ForgeConfigSpec.DoubleValue CHARGE_MAX_DISTANCE_SQ;
    public static final ForgeConfigSpec.IntValue CHARGE_PREPARATION_TICKS;
    public static final ForgeConfigSpec.IntValue CHARGE_TIMEOUT_TICKS;
    public static final ForgeConfigSpec.DoubleValue CHARGE_SPEED;
    public static final ForgeConfigSpec.DoubleValue CHARGE_MAX_TRAVEL_DISTANCE;
    public static final ForgeConfigSpec.DoubleValue CHARGE_END_EXPLOSION_KNOCKBACK;
    public static final ForgeConfigSpec.DoubleValue CHARGE_END_EXPLOSION_RADIUS;
    public static final ForgeConfigSpec.DoubleValue CHARGE_END_EXPLOSION_DAMAGE_RATIO;
    public static final ForgeConfigSpec.IntValue CHARGE_COUNT_NORMAL;
    public static final ForgeConfigSpec.IntValue CHARGE_COUNT_ENRAGED;

    public static final ForgeConfigSpec.DoubleValue GRAVITY_ORB_MIN_DISTANCE_SQ;
    public static final ForgeConfigSpec.IntValue GRAVITY_ORB_CHARGE_UP_TICKS;
    public static final ForgeConfigSpec.DoubleValue BARRAGE_MIN_DISTANCE_SQ;
    public static final ForgeConfigSpec.IntValue BARRAGE_CHARGE_UP_TICKS;
    public static final ForgeConfigSpec.IntValue BARRAGE_DURATION_NORMAL;
    public static final ForgeConfigSpec.IntValue BARRAGE_FIRE_RATE_NORMAL;
    public static final ForgeConfigSpec.IntValue BARRAGE_DURATION_ENRAGED;
    public static final ForgeConfigSpec.IntValue BARRAGE_FIRE_RATE_ENRAGED;

    public static final ForgeConfigSpec.DoubleValue LASER_HP_THRESHOLD;
    public static final ForgeConfigSpec.IntValue LASER_CHARGE_UP_TICKS;
    public static final ForgeConfigSpec.IntValue LASER_DURATION_TICKS;
    public static final ForgeConfigSpec.DoubleValue LASER_DAMAGE_RATIO;
    public static final ForgeConfigSpec.DoubleValue LASER_FIELD_HP_THRESHOLD;

    public static final ForgeConfigSpec.DoubleValue EVENT_HORIZON_HP_THRESHOLD;
    public static final ForgeConfigSpec.IntValue EVENT_HORIZON_CAST_TIME_TICKS;
    public static final ForgeConfigSpec.DoubleValue REVENANT_DETECT_RADIUS;
    public static final ForgeConfigSpec.DoubleValue SING_CORE_PERCENT;

    static {
        BUILDER.push("BlackBool_Settings");

        BUILDER.push("BaseStats");
        MAX_HEALTH = BUILDER.defineInRange("max_health", 550.0, 1.0, 100000.0);
        MOVEMENT_SPEED = BUILDER.defineInRange("movement_speed", 1.2, 0.0, 10.0);
        FLYING_SPEED = BUILDER.defineInRange("flying_speed", 1.2, 0.0, 10.0);
        ATTACK_DAMAGE = BUILDER.defineInRange("attack_damage", 15.0, 0.0, 1000.0);
        ATTACK_SPEED = BUILDER.defineInRange("attack_speed", 4.0, 0.0, 20.0);
        FOLLOW_RANGE = BUILDER.defineInRange("follow_range", 64.0, 0.0, 512.0);
        KNOCKBACK_RESISTANCE = BUILDER.defineInRange("knockback_resistance", 1.0, 0.0, 1.0);
        ARMOR = BUILDER.defineInRange("armor", 25.0, 0.0, 100.0);
        CONTACT_DAMAGE_RATIO = BUILDER.defineInRange("contact_damage_ratio", 0.4, 0.0, 10.0);
        BUILDER.pop();

        BUILDER.push("Animations");
        SPAWN_ANIMATION_DURATION_TICKS = BUILDER.defineInRange("spawn_anim_ticks", 60, 0, 1000);
        DEATH_PHASE1_OUTBURST_TICKS = BUILDER.defineInRange("death_phase1_ticks", 40, 0, 1000);
        DEATH_PHASE2_COLLAPSE_TICKS = BUILDER.defineInRange("death_phase2_ticks", 80, 0, 1000);
        DEATH_PHASE3_IMPLOSION_TICKS = BUILDER.defineInRange("death_phase3_ticks", 60, 0, 1000);
        DEATH_PHASE4_FINAL_TICKS = BUILDER.defineInRange("death_phase4_ticks", 40, 0, 1000);
        DEATH_PULL_AREA_RADIUS = BUILDER.defineInRange("death_pull_radius", 32.0, 0.0, 128.0);
        DEATH_PULL_STRENGTH = BUILDER.defineInRange("death_pull_strength", 0.4, 0.0, 10.0);
        BUILDER.pop();

        BUILDER.push("StanceAI");
        STANCE_SWITCH_MELEE_RANGE_SQ = BUILDER.defineInRange("switch_melee_range_sq", 100.0, 0.0, 1024.0);
        STANCE_SWITCH_RANGED_RANGE_SQ = BUILDER.defineInRange("switch_ranged_range_sq", 225.0, 0.0, 1024.0);
        STANCE_SWITCH_COOLDOWN_MIN = BUILDER.defineInRange("switch_cooldown_min", 100, 0, 10000);
        STANCE_SWITCH_COOLDOWN_RANDOM = BUILDER.defineInRange("switch_cooldown_random", 100, 0, 10000);
        STANCE_SWITCH_FORCED_COOLDOWN_MIN = BUILDER.defineInRange("forced_switch_min", 200, 0, 10000);
        STANCE_SWITCH_FORCED_COOLDOWN_RANDOM = BUILDER.defineInRange("forced_switch_random", 200, 0, 10000);
        MELEE_STANCE_IDEAL_DISTANCE = BUILDER.defineInRange("melee_ideal_dist", 3.5, 0.0, 128.0);
        RANGED_STANCE_IDEAL_DISTANCE = BUILDER.defineInRange("ranged_ideal_dist", 15.0, 0.0, 128.0);
        MELEE_STANCE_SPEED_SCALE = BUILDER.defineInRange("melee_speed_scale", 0.4, 0.0, 5.0);
        RANGED_STANCE_SPEED_SCALE = BUILDER.defineInRange("ranged_speed_scale", 0.25, 0.0, 5.0);
        RANDOM_FLY_RADIUS = BUILDER.defineInRange("random_fly_radius", 16.0, 0.0, 128.0);
        BUILDER.pop();

        BUILDER.push("Thresholds");
        PHASE_TWO_HP_THRESHOLD = BUILDER.defineInRange("phase_two_hp", 0.5, 0.0, 1.0);
        CRITICAL_ENRAGE_HP_THRESHOLD = BUILDER.defineInRange("enrage_hp", 0.25, 0.0, 1.0);
        LANCE_SPAWN_START_HP_THRESHOLD = BUILDER.defineInRange("lance_spawn_hp", 0.75, 0.0, 1.0);
        BUILDER.pop();

        BUILDER.push("Cooldowns");
        CHARGE_COOLDOWN_BASE = BUILDER.defineInRange("charge_cd", 100, 0, 10000);
        GRAVITY_ORB_COOLDOWN_BASE = BUILDER.defineInRange("gravity_orb_cd", 600, 0, 10000);
        BARRAGE_COOLDOWN_BASE = BUILDER.defineInRange("barrage_cd", 300, 0, 10000);
        LASER_COOLDOWN_BASE = BUILDER.defineInRange("laser_cd", 400, 0, 10000);
        PASSIVE_ORB_COOLDOWN_BASE = BUILDER.defineInRange("passive_orb_cd", 80, 0, 10000);
        BUILDER.pop();

        BUILDER.push("GeneralAbilities");
        MAX_ORBITING_PROJECTILES = BUILDER.defineInRange("max_orbiting_projectiles", 8, 0, 64);
        PROJECTILE_CAPTURE_RADIUS = BUILDER.defineInRange("proj_capture_radius", 10.0, 0.0, 64.0);
        DAMAGE_CAP_PER_TICK = BUILDER.defineInRange("damage_cap_per_tick", 25.0, 0.0, 1000.0);
        DAMAGE_CAP_REDUCTION_RATIO = BUILDER.defineInRange("damage_cap_reduction", 0.2, 0.0, 1.0);
        BUILDER.pop();

        BUILDER.push("ChargeAbility");
        CHARGE_MIN_DISTANCE_SQ = BUILDER.defineInRange("charge_min_dist_sq", 16.0, 0.0, 1024.0);
        CHARGE_MAX_DISTANCE_SQ = BUILDER.defineInRange("charge_max_dist_sq", 100.0, 0.0, 1024.0);
        CHARGE_PREPARATION_TICKS = BUILDER.defineInRange("charge_prep_ticks", 30, 0, 200);
        CHARGE_TIMEOUT_TICKS = BUILDER.defineInRange("charge_timeout_ticks", 40, 0, 200);
        CHARGE_SPEED = BUILDER.defineInRange("charge_speed", 2.0, 0.0, 10.0);
        CHARGE_MAX_TRAVEL_DISTANCE = BUILDER.defineInRange("charge_max_travel", 30.0, 0.0, 256.0);
        CHARGE_END_EXPLOSION_KNOCKBACK = BUILDER.defineInRange("charge_explosion_kb", 1.5, 0.0, 10.0);
        CHARGE_END_EXPLOSION_RADIUS = BUILDER.defineInRange("charge_explosion_radius", 6.0, 0.0, 64.0);
        CHARGE_END_EXPLOSION_DAMAGE_RATIO = BUILDER.defineInRange("charge_explosion_dmg_ratio", 0.8, 0.0, 10.0);
        CHARGE_COUNT_NORMAL = BUILDER.defineInRange("charge_count_normal", 1, 1, 10);
        CHARGE_COUNT_ENRAGED = BUILDER.defineInRange("charge_count_enraged", 3, 1, 10);
        BUILDER.pop();

        BUILDER.push("GravityAndBarrage");
        GRAVITY_ORB_MIN_DISTANCE_SQ = BUILDER.defineInRange("gravity_min_dist_sq", 144.0, 0.0, 1024.0);
        GRAVITY_ORB_CHARGE_UP_TICKS = BUILDER.defineInRange("gravity_charge_ticks", 25, 0, 200);
        BARRAGE_MIN_DISTANCE_SQ = BUILDER.defineInRange("barrage_min_dist_sq", 225.0, 0.0, 1024.0);
        BARRAGE_CHARGE_UP_TICKS = BUILDER.defineInRange("barrage_charge_ticks", 25, 0, 200);
        BARRAGE_DURATION_NORMAL = BUILDER.defineInRange("barrage_duration_normal", 50, 0, 1000);
        BARRAGE_FIRE_RATE_NORMAL = BUILDER.defineInRange("barrage_rate_normal", 6, 1, 100);
        BARRAGE_DURATION_ENRAGED = BUILDER.defineInRange("barrage_duration_enraged", 80, 0, 1000);
        BARRAGE_FIRE_RATE_ENRAGED = BUILDER.defineInRange("barrage_rate_enraged", 4, 1, 100);
        BUILDER.pop();

        BUILDER.push("LaserAbility");
        LASER_HP_THRESHOLD = BUILDER.defineInRange("laser_hp_threshold", 0.5, 0.0, 1.0);
        LASER_CHARGE_UP_TICKS = BUILDER.defineInRange("laser_charge_ticks", 40, 0, 200);
        LASER_DURATION_TICKS = BUILDER.defineInRange("laser_duration_ticks", 100, 0, 1000);
        LASER_DAMAGE_RATIO = BUILDER.defineInRange("laser_damage_ratio", 1.2, 0.0, 10.0);
        LASER_FIELD_HP_THRESHOLD = BUILDER.defineInRange("laser_field_hp", 0.3, 0.0, 1.0);
        BUILDER.pop();

        BUILDER.push("Misc");
        EVENT_HORIZON_HP_THRESHOLD = BUILDER.defineInRange("event_horizon_hp", 0.5, 0.0, 1.0);
        EVENT_HORIZON_CAST_TIME_TICKS = BUILDER.defineInRange("event_horizon_cast_time", 60, 0, 1000);
        REVENANT_DETECT_RADIUS = BUILDER.defineInRange("revenant_detect_radius", 64.0, 0.0, 256.0);
        SING_CORE_PERCENT = BUILDER.defineInRange("singularity_core_drop_chance", 0.4, 0.0, 1.0);
        BUILDER.pop();

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}