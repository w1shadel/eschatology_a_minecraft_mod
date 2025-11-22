package com.Maxwell.eschatology;


import com.Maxwell.eschatology.common.Network.ModMessages;
import com.Maxwell.eschatology.register.*;
import com.Maxwell.eschatology.register.advancements.ModTriggers;
import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Eschatology.MODID)
public class Eschatology
{
    public static final Logger LOGGER =LogUtils.getLogger();
    public static final String MODID = "eschatology";
    public Eschatology(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();
        ModItems.TABS.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModRecipes.SERIALIZERS.register(modEventBus);
        ModRecipes.TYPES.register(modEventBus);
        ModMenuTypes.MENU_TYPES.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModEntities.ENTITY_TYPES.register(modEventBus);
        ModEffects.MOB_EFFECTS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModSounds.SOUND_EVENTS.register(modEventBus);
        ModDataSerializers.register(modEventBus);
        ModTriggers.register();
        ModMessages.register();
    }
}
