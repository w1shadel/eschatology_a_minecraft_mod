package com.Maxwell.eschatology.common.Capability.WitchsCrest;import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;import javax.annotation.Nullable;public class WCAttackLevelProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {    public static final Capability<WCAttackLevel> PLAYER_ATTACK_LEVEL =
            CapabilityManager.get(new CapabilityToken<>() {});    private WCAttackLevel attackLevel = null;
    private final LazyOptional<WCAttackLevel> optional = LazyOptional.of(this::createPlayerAttackLevel);    private WCAttackLevel createPlayerAttackLevel() {
        if (this.attackLevel == null) {
            this.attackLevel = new WCAttackLevel();
        }
        return this.attackLevel;
    }    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == PLAYER_ATTACK_LEVEL) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createPlayerAttackLevel().saveNBTData(nbt);
        return nbt;
    }    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerAttackLevel().loadNBTData(nbt);
    }
}