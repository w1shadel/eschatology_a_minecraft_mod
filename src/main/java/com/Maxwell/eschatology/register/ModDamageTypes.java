package com.Maxwell.eschatology.register;import com.Maxwell.eschatology.Eschatology;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
@SuppressWarnings("removal")
public class ModDamageTypes {
    public static final ResourceKey<DamageType> RED = createKey("red");
    public static final ResourceKey<DamageType> WHITE = createKey("white");
    public static final ResourceKey<DamageType> BLACK = createKey("black");
    public static final ResourceKey<DamageType> PALE = createKey("pale");
    private static ResourceKey<DamageType> createKey(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Eschatology.MODID, name));
    }
}