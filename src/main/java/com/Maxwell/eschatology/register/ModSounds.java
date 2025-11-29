package com.Maxwell.eschatology.register;import com.Maxwell.eschatology.Eschatology;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
@SuppressWarnings("removal")
public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Eschatology.MODID);
    public static final RegistryObject<SoundEvent> MUSIC_BLACK_BOOL =
            registerSoundEvent("black_bool");
    public static final RegistryObject<SoundEvent> MUSIC_EXO_WITHER =
            registerSoundEvent("exo_wither");
    public static final RegistryObject<SoundEvent> TWILIGHT_ATK1 =
            registerSoundEvent("twilight_attack1");
    public static final RegistryObject<SoundEvent> TWILIGHT_ATK2 =
            registerSoundEvent("twilight_attack2");
    public static final RegistryObject<SoundEvent> MAGIC_BULLET_CREATE =
            registerSoundEvent("magic_bullet_create");    public static final RegistryObject<SoundEvent> MAGIC_BULLET_SHOOT =
            registerSoundEvent("magic_bullet_shoot");    public static final RegistryObject<SoundEvent> BUTURI_ATK_1 =
            registerSoundEvent("buturi_atk_1");    public static final RegistryObject<SoundEvent> BUTURI_ATK_2 =
            registerSoundEvent("buturi_atk_2");    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = new ResourceLocation(Eschatology.MODID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }
}