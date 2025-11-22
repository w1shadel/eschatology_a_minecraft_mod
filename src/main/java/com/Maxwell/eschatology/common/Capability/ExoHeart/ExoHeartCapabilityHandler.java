package com.Maxwell.eschatology.common.Capability.ExoHeart;import com.Maxwell.eschatology.Eschatology;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
@SuppressWarnings("removal")
@Mod.EventBusSubscriber(modid = Eschatology.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ExoHeartCapabilityHandler {    public static final ResourceLocation ID = new ResourceLocation(Eschatology.MODID, "exoheart");    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(ID, new Provider());
        }
    }    public static class Provider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {        private final ExoHeartData data = new ExoHeartData();
        private final LazyOptional<ExoHeartData> optional = LazyOptional.of(() -> data);        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, net.minecraft.core.Direction side) {
            return cap == ExoHeartData.CAPABILITY ? optional.cast() : LazyOptional.empty();
        }        @Override
        public CompoundTag serializeNBT() {
            return data.serializeNBT();
        }        @Override
        public void deserializeNBT(CompoundTag nbt) {
            data.deserializeNBT(nbt);
        }
    }
}