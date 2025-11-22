package com.Maxwell.eschatology.common.Capability.WitchsCrest;import net.minecraft.nbt.CompoundTag;public class WCAttackLevel {    private int attackLevel;    private final int MIN_LEVEL = 0;
    private final int MAX_LEVEL = 99999;     public int getLevel() {
        return this.attackLevel;
    }    public void addLevel(int amount) {
        this.attackLevel = Math.min(this.attackLevel + amount, MAX_LEVEL);
    }    public void setLevel(int level) {
        this.attackLevel = Math.max(MIN_LEVEL, Math.min(level, MAX_LEVEL));
    }    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("attackLevel", this.attackLevel);
    }    public void loadNBTData(CompoundTag nbt) {
        this.attackLevel = nbt.getInt("attackLevel");
    }
}