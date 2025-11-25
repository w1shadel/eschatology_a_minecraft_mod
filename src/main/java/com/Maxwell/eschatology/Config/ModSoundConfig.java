package com.Maxwell.eschatology.Config;

import com.Maxwell.eschatology.Eschatology;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

public class ModSoundConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<String> MUSIC_BLACK_BOOL;
    public static final ForgeConfigSpec.ConfigValue<String> MUSIC_EXO_WITHER;

    static {
        BUILDER.push("Music");

        MUSIC_BLACK_BOOL = BUILDER
                .comment("Boss Music ID for Black Bool (e.g., 'eschatology:exo_wither' or 'eschatology:black_bool')")
                .define("blackBoolMusic", Eschatology.MODID + ":black_bool");

        MUSIC_EXO_WITHER = BUILDER
                .comment("Boss Music ID for Exo Wither")
                .define("ExoWitherMusic", Eschatology.MODID + ":exo_wither");

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    public static SoundEvent getSound(ForgeConfigSpec.ConfigValue<String> configValue) {
        String soundId = configValue.get();
        if (soundId == null || soundId.isEmpty()) {
            return SoundEvent.createVariableRangeEvent(new ResourceLocation(Eschatology.MODID, "black_bool"));
        }
        try {
            ResourceLocation rl = new ResourceLocation(soundId);
            SoundEvent event = ForgeRegistries.SOUND_EVENTS.getValue(rl);
            if (event == null) {
                return SoundEvent.createVariableRangeEvent(rl);
            }
            return event;
        } catch (Exception e) {
            System.err.println("Invalid sound ID in config: " + soundId);
            return SoundEvent.createVariableRangeEvent(new ResourceLocation(Eschatology.MODID, "black_bool"));
        }
    }
}