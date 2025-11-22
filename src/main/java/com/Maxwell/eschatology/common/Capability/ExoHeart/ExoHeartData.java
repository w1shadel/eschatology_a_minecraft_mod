package com.Maxwell.eschatology.common.Capability.ExoHeart;import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.INBTSerializable;public class ExoHeartData implements INBTSerializable<CompoundTag> {
    public static final Capability<ExoHeartData> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    private int charge = 0;
    private boolean active = false;    public int getCharge() { return charge; }
    public void setCharge(int value) { this.charge = Math.min(100, Math.max(0, value)); }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("Charge", charge);
        tag.putBoolean("Active", active);
        return tag;
    }    @Override
    public void deserializeNBT(CompoundTag tag) {
        charge = tag.getInt("Charge");
        active = tag.getBoolean("Active");
    }
    public static int getCharge(Player player) {
        return player.getCapability(CAPABILITY).map(ExoHeartData::getCharge).orElse(0);
    }    public static void setCharge(Player player, int value) {
        player.getCapability(CAPABILITY).ifPresent(data -> data.setCharge(value));
    }    public static void addCharge(Player player, int amount) {
        player.getCapability(CAPABILITY).ifPresent(data -> {
            int before = data.getCharge();
            data.setCharge(before + amount);
        });
    }
}