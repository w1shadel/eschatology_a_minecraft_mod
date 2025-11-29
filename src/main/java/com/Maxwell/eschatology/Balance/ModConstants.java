package com.Maxwell.eschatology.Balance;public class ModConstants {    public static class Revenge {
        public static  int PAUSE_DURATION = 18;
        public static double JUMP_HEIGHT = 7.0;
        public static  int JUMP_DURATION = 16;
        public static  int HOVER_DURATION = 12;
        public static  double DASH_LERP = 0.40;        public static  float STAGE0_DAMAGE = 20.0F;
        public static  double STAGE0_KNOCKBACK_XZ = 3.0;
        public static  double STAGE0_KNOCKBACK_Y = 0.25;        public static  float STAGE5_EXPLOSION_RADIUS = 5.0F;
        public static  float STAGE5_EXPLOSION_DAMAGE = 1200.0F;
        public static  double STAGE5_DAMAGE_RANGE_SQR = 16.0;
        public static  int STAGE5_BLOCK_DESTROY_RADIUS = 11;        public static  double DASH_TRIGGER_DISTANCE_SQR = 6.25;
        public static  int DASH_TIMEOUT = 60;        public static  float FALLING_BLOCK_DAMAGE = 40.0F;
        public static  int ADD_REVENGE = 10;
        public static  double BASE_EXTRA_DAMAGE_AT_FULL = 8.0D;
        public static  double BASE_ATTACK_SPEED_REF = 4.0D;
        public static  double TARGET_SEARCH_DIST = 5.0D;
        public static  double TARGET_SEARCH_WIDTH = 2.5D;
    }
    public static class WitchsCrest {        public static  int GAIN_POISONOUS = 10;
        public static  int GAIN_RECIPE = 5;
        public static  int GAIN_TAG = 1;        public static  int DURATION_PER_LEVEL = 600;
        public static  int AMPLIFIER_THRESHOLD_LEVEL = 10;        public static  float PENALTY_DAMAGE = 2.0F;
        public static  int PENALTY_DURATION = 400;        public static  float SELF_DAMAGE = 1.0F;
        public static  float SORN_BASE_DAMAGE = 6.0F;
        public static  float DAMAGE_PER_LEVEL = 0.5F;
    }
    public static class TOaFG {
        public static  int TARGET_RANGE = 16;
        public static  int CHECK_INTERVAL = 20;
        public static  int MAX_TARGETS = 2;
        public static  float ATTACK_DAMAGE = 2.0F;        public static  int PARTICLES_PER_TARGET = 10;
        public static  int TRAIL_COUNT = 4;
        public static  double SPAWN_SPREAD_XZ = 8.0;
        public static  double SPAWN_HEIGHT = 4.0;
    }    public static class ExoHeart {        public static  String TEXTURE_PATH = "textures/gui/exoheart_hud.png";
        public static  float CHARGE_BAR_SCALE = 1.82f;
        public static  int TEXT_COLOR = 0x80D0FF;
        public static int HUD_X_OFFSET = -91;        public static int HUD_Y_OFFSET = 50;        public static int HUD_TEXT_OFFSET = 10;        public static  double BOSS_CHECK_RADIUS = 16.0D;
        public static  int LOSS_DELAY_TICKS = 100;
        public static  int MAX_CHARGE = 100;        public static  int ACTIVE_DRAIN_INTERVAL = 10;
        public static  int ACTIVE_DRAIN_AMOUNT = 10;
        public static  int ACTIVE_HEAL_INTERVAL = 8;
        public static  float ACTIVE_HEAL_AMOUNT = 1.0F;        public static  int PASSIVE_CHARGE_AMOUNT = 1;
        public static  int PASSIVE_DRAIN_AMOUNT = 1;
    }    public static class Apocalypse {
        public static  float DAMAGE_MULTIPLIER = 1.5F;
    }    public static class Counter {
        public static  int DAMAGE_INSTANCES = 50;
        public static  int DAMAGE_INTERVAL = 1;
        public static  float DAMAGE_AMOUNT = 10.0F;
        public static  int DAMAGE_RADIUS = 3;        public static  int SHARD_SPAWN_TICK = 43;
        public static  int START_TICK = 47;
        public static  int END_TICK = 60;        public static  int SHARD_COUNT = 42;
        public static  double SHARD_SPREAD_DISTANCE = 4.0;        public static  float CONTEMPT_DAMAGE_MULT = 1.5f;
        public static  float SELF_CONTEMPT_DAMAGE_MULT = 0.3f;
        public static  float SWEEP_DAMAGE = 120f;
        public static   double RANGE = 3.0d;
    }    public static class FellBullet {
        public static  float BASE_DAMAGE = 40.0F;
        public static  int MAX_LIFE_TICKS = 100;        public static  float SHARD_DAMAGE = 6.0F;
        public static  int SHARD_LIFE_TICKS = 20;
    }    public static class MagicBullet {
        public static  int MAX_BULLETS = 7;
        public static  int MIN_CHARGE_TIME = 20;        public static  float BASE_DAMAGE = 20.0F;
        public static  float CHARGE_DAMAGE_CAP = 40.0F;
        public static  float CHARGE_DAMAGE_MULTIPLIER = 0.25F;
        public static  float ULTRA_SHOT_DAMAGE = 100.0F;        public static  int COOLDOWN_NORMAL = 10;
        public static  int COOLDOWN_ULTRA = 60;        public static  int PROJECTILE_LIFE = 100;
    }
    public static class Colors {        public static  int BLACK_BOOL_BASE = 0x1A1A1A;
        public static  int BLACK_BOOL_DOT = 0xFF0000;
        public static  int EXO_WITHER_BASE = 0x555555;
        public static  int EXO_WITHER_DOT = 0x00FFFF;
    }
}