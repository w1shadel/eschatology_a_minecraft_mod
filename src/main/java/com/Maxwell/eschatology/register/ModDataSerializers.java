package com.Maxwell.eschatology.register;import com.Maxwell.eschatology.Eschatology;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;public class ModDataSerializers {
    public static final DeferredRegister<EntityDataSerializer<?>> DATA_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, Eschatology.MODID);
    public static final RegistryObject<EntityDataSerializer<Vec3>> VEC3 = DATA_SERIALIZERS.register("vec3", () ->
            new EntityDataSerializer<>() {
                @Override
                public void write(FriendlyByteBuf buf, Vec3 vec) {
                    buf.writeDouble(vec.x);
                    buf.writeDouble(vec.y);
                    buf.writeDouble(vec.z);
                }
                @Override
                public Vec3 read(FriendlyByteBuf buf) {
                    return new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
                }                @Override
                public Vec3 copy(Vec3 vec) {
                    return new Vec3(vec.x, vec.y, vec.z);
                }
            }
    );
    public static void register(IEventBus eventBus) {
        DATA_SERIALIZERS.register(eventBus);
    }
}