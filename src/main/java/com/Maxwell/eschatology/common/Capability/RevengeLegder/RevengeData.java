package com.Maxwell.eschatology.common.Capability.RevengeLegder;import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;public class RevengeData {    private static final String REVENGE_KEY = "RevengeGauge";
    public static final int MAX_GAUGE = 100;    public static void addGauge(Player player, int amount) {
        CompoundTag data = player.getPersistentData();
        int current = data.getInt(REVENGE_KEY);
        data.putInt(REVENGE_KEY, Math.min(current + amount, MAX_GAUGE));
    }    public static int getGauge(Player player) {
        return player.getPersistentData().getInt(REVENGE_KEY);
    }    public static boolean isMaxGauge(Player player) {
        return getGauge(player) >= MAX_GAUGE;
    }
    public static void setGaugeClient(Player player, int value) {
        player.getPersistentData().putInt("RevengeGaugeClient", value);
    }    public static int getGaugeClient(Player player) {
        return player.getPersistentData().getInt("RevengeGaugeClient");
    }
    public static void consumeGauge(Player player) {
        player.getPersistentData().putInt(REVENGE_KEY, 0);
    }
}