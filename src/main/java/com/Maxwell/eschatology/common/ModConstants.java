package com.Maxwell.eschatology.common;

public class ModConstants {

    // Revenge Ledger Ability (AbilityTickHandler)
    public static class Revenge {
        public static final int PAUSE_DURATION = 18;
        public static final double JUMP_HEIGHT = 7.0;
        public static final int JUMP_DURATION = 16;
        public static final int HOVER_DURATION = 12;
        public static final double DASH_LERP = 0.40;

        public static final float STAGE0_DAMAGE = 20.0F;
        public static final double STAGE0_KNOCKBACK_XZ = 3.0;
        public static final double STAGE0_KNOCKBACK_Y = 0.25;

        public static final float STAGE5_EXPLOSION_RADIUS = 5.0F;
        public static final float STAGE5_EXPLOSION_DAMAGE = 1200.0F;
        public static final double STAGE5_DAMAGE_RANGE_SQR = 16.0; // 4 blocks ^ 2
        public static final int STAGE5_BLOCK_DESTROY_RADIUS = 11;

        public static final double DASH_TRIGGER_DISTANCE_SQR = 6.25; // 2.5 blocks ^ 2
        public static final int DASH_TIMEOUT = 60;

        public static final float FALLING_BLOCK_DAMAGE = 40.0F;
    }

    // Exo Heart HUD
    public static class ExoHeart {
        public static final String TEXTURE_PATH = "textures/gui/exoheart_hud.png";
        public static final float CHARGE_BAR_SCALE = 1.82f;
        public static final int TEXT_COLOR = 0x80D0FF;
    }

    // Apocalypse Sword
    public static class Apocalypse {
        public static final float DAMAGE_MULTIPLIER = 1.5F;
        public static final int DAMAGE_ITERATIONS = 4;
    }

    // Counter Stance & Contempt (Awe_Contempt)
    public static class Counter {
        public static final int DAMAGE_INSTANCES = 50;
        public static final int DAMAGE_INTERVAL = 1;
        public static final float DAMAGE_AMOUNT = 10.0F;
        public static final int DAMAGE_RADIUS = 3;

        public static final int SHARD_SPAWN_TICK = 43;
        public static final int START_TICK = 47;
        public static final int END_TICK = 60;

        public static final int SHARD_COUNT = 42;
        public static final double SHARD_SPREAD_DISTANCE = 4.0;

        public static final float CONTEMPT_DAMAGE_MULT = 1.5f;
        public static final float SELF_CONTEMPT_DAMAGE_MULT = 0.3f;
    }

    // Fell Bullet & Shards
    public static class FellBullet {
        public static final float BASE_DAMAGE = 40.0F;
        public static final int MAX_LIFE_TICKS = 100;

        public static final float SHARD_DAMAGE = 6.0F;
        public static final int SHARD_LIFE_TICKS = 20;
    }

    // Magic Bullet Weapon
    public static class MagicBullet {
        public static final int MAX_BULLETS = 7;
        public static final int MIN_CHARGE_TIME = 20;

        public static final float BASE_DAMAGE = 20.0F;
        public static final float CHARGE_DAMAGE_CAP = 40.0F;
        public static final float CHARGE_DAMAGE_MULTIPLIER = 0.25F;
        public static final float ULTRA_SHOT_DAMAGE = 100.0F;

        public static final int COOLDOWN_NORMAL = 10;
        public static final int COOLDOWN_ULTRA = 60;

        public static final int PROJECTILE_LIFE = 100;
    }
}