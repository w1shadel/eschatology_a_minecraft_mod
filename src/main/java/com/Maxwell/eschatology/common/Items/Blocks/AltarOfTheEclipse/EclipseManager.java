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
    // 外部から読み取れるようにgetterがあると便利ですが、今回はprivateのままでいきます
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

        // クライアントへ「儀式開始(true)」を通知（空が暗くなる演出など？）
        ModMessages.sendToAll(new SyncEclipseStatePacket(true));

        scheduler.schedule(() -> {
            level.getServer().execute(() -> {
                // 時間を止めて夜にする
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

    // --- 追加箇所: 儀式を終了させるメソッド ---
    public static void endEclipse(ServerLevel level) {
        // フラグをリセットして、再使用可能にする
        isEclipseInProgress = false;

        // 時間進行を再開させる
        level.getGameRules().getRule(GameRules.RULE_DAYLIGHT).set(true, level.getServer());

        // 天候が荒れているなら晴れに戻す（必要であれば）
        // level.setWeatherParameters(6000, 0, false, false);

        // クライアントへ「儀式終了(false)」を通知
        ModMessages.sendToAll(new SyncEclipseStatePacket(false));
    }
}