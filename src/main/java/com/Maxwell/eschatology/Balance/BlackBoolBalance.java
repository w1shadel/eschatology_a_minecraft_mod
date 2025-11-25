package com.Maxwell.eschatology.Balance;
public class BlackBoolBalance {

    public static double MAX_HEALTH = 550.0;
    public static double MOVEMENT_SPEED = 1.2;
    public static double FLYING_SPEED = 1.2;
    public static double ATTACK_DAMAGE = 15.0;
    public static double ATTACK_SPEED = 4.0;
    public static double FOLLOW_RANGE = 64.0;
    public static double KNOCKBACK_RESISTANCE = 1.0;
    public static double ARMOR = 25.0;
    public static float CONTACT_DAMAGE_RATIO = 0.4f;

    public static int SPAWN_ANIMATION_DURATION_TICKS = 60;
    public static int DEATH_PHASE1_OUTBURST_TICKS = 40;
    public static int DEATH_PHASE2_COLLAPSE_TICKS = 80;
    public static int DEATH_PHASE3_IMPLOSION_TICKS = 60;
    public static int DEATH_PHASE4_FINAL_TICKS = 40;
    public static double DEATH_PULL_AREA_RADIUS = 32.0;
    public static float DEATH_PULL_STRENGTH = 0.4f;

    public static double STANCE_SWITCH_MELEE_RANGE_SQ = 100.0;
    public static double STANCE_SWITCH_RANGED_RANGE_SQ = 225.0;
    public static int STANCE_SWITCH_COOLDOWN_MIN = 100;
    public static int STANCE_SWITCH_COOLDOWN_RANDOM = 100;
    public static int STANCE_SWITCH_FORCED_COOLDOWN_MIN = 200;
    public static int STANCE_SWITCH_FORCED_COOLDOWN_RANDOM = 200;
    public static double MELEE_STANCE_IDEAL_DISTANCE = 3.5;
    public static double RANGED_STANCE_IDEAL_DISTANCE = 15.0;
    public static double MELEE_STANCE_SPEED_SCALE = 0.4;
    public static double RANGED_STANCE_SPEED_SCALE = 0.25;
    public static double RANDOM_FLY_RADIUS = 16.0;

    public static float PHASE_TWO_HP_THRESHOLD = 0.5f;
    public static float CRITICAL_ENRAGE_HP_THRESHOLD = 0.25f;
    public static float LANCE_SPAWN_START_HP_THRESHOLD = 0.75f;

    public static int CHARGE_COOLDOWN_BASE = 100;
    public static int GRAVITY_ORB_COOLDOWN_BASE = 600;
    public static int BARRAGE_COOLDOWN_BASE = 300;
    public static int LASER_COOLDOWN_BASE = 400;
    public static int PASSIVE_ORB_COOLDOWN_BASE = 80;

    public static int MAX_ORBITING_PROJECTILES = 8;
    public static double PROJECTILE_CAPTURE_RADIUS = 10.0;
    public static float DAMAGE_CAP_PER_TICK = 25.0f;
    public static float DAMAGE_CAP_REDUCTION_RATIO = 0.2f;

    public static double CHARGE_MIN_DISTANCE_SQ = 16.0;
    public static double CHARGE_MAX_DISTANCE_SQ = 100.0;
    public static int CHARGE_PREPARATION_TICKS = 30;
    public static int CHARGE_TIMEOUT_TICKS = 40;
    public static double CHARGE_SPEED = 2.0;
    public static double CHARGE_MAX_TRAVEL_DISTANCE = 30.0;
    public static float CHARGE_END_EXPLOSION_KNOCKBACK = 1.5f;
    public static double CHARGE_END_EXPLOSION_RADIUS = 6.0;
    public static float CHARGE_END_EXPLOSION_DAMAGE_RATIO = 0.8f;
    public static int CHARGE_COUNT_NORMAL = 1;
    public static int CHARGE_COUNT_ENRAGED = 3;

    public static double GRAVITY_ORB_MIN_DISTANCE_SQ = 144.0;
    public static int GRAVITY_ORB_CHARGE_UP_TICKS = 25;

    public static double BARRAGE_MIN_DISTANCE_SQ = 225.0;
    public static int BARRAGE_CHARGE_UP_TICKS = 25;
    public static int BARRAGE_DURATION_NORMAL = 50;
    public static int BARRAGE_FIRE_RATE_NORMAL = 6;
    public static int BARRAGE_DURATION_ENRAGED = 80;
    public static int BARRAGE_FIRE_RATE_ENRAGED = 4;

    public static float LASER_HP_THRESHOLD = 0.5f;
    public static int LASER_CHARGE_UP_TICKS = 40;
    public static int LASER_DURATION_TICKS = 100;
    public static float LASER_DAMAGE_RATIO = 1.2f;
    public static float LASER_FIELD_HP_THRESHOLD = 0.3f;

    public static float EVENT_HORIZON_HP_THRESHOLD = 0.5f;
    public static int EVENT_HORIZON_CAST_TIME_TICKS = 60;
    public static double REVENANT_DETECT_RADIUS = 64.0D;
    public static float SING_CORE_PERCENT = 0.40f;
}