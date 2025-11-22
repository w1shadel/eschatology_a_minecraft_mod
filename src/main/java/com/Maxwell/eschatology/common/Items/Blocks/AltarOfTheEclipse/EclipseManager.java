package com.Maxwell.eschatology.common.Items.Blocks.AltarOfTheEclipse;

import com.Maxwell.eschatology.common.Network.ModMessages;
import com.Maxwell.eschatology.common.Network.SyncEclipseStatePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.GameRules;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EclipseManager {
    private static boolean isEclipseInProgress = false;
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    public static <T extends Mob> void tryStartEclipse(ServerLevel level, BlockPos altarPos, EntityType<T> bossTypeToSpawn) {
        if (isEclipseInProgress) {
            return;
        }
        startEclipseSequence(level, altarPos, bossTypeToSpawn);
    }
    private static <T extends Mob> void startEclipseSequence(ServerLevel level, BlockPos altarPos, EntityType<T> bossTypeToSpawn) {
        isEclipseInProgress = true;
        level.playSound(null, altarPos, SoundEvents.WITHER_DEATH, SoundSource.HOSTILE, 2.0F, 0.5F);
        ModMessages.sendToAll(new SyncEclipseStatePacket(true));
        scheduler.schedule(() -> {
            level.getServer().execute(() -> {
                level.getGameRules().getRule(GameRules.RULE_DAYLIGHT).set(false, level.getServer());
                level.setDayTime(14000);
                T boss = bossTypeToSpawn.create(level);
                if (boss != null) {
                    boss.setPos(altarPos.getX() + 0.5, altarPos.getY() + 5.0, altarPos.getZ() + 0.5);
                    boss.finalizeSpawn(level, level.getCurrentDifficultyAt(altarPos), MobSpawnType.EVENT, null, null);
                    level.addFreshEntity(boss);
                }
            });
        }, 5, TimeUnit.SECONDS);
    }
}