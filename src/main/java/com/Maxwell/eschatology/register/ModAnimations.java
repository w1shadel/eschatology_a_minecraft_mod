package com.Maxwell.eschatology.register;

import com.Maxwell.eschatology.Eschatology;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
@SuppressWarnings("removal")
@Mod.EventBusSubscriber(modid = Eschatology.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModAnimations {
    public static final ResourceLocation ANIMATION_LAYER_ID = new ResourceLocation(Eschatology.MODID, "animation_layer");

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(
                ANIMATION_LAYER_ID,
                42,
                ModAnimations::registerPlayerAnimationLayer
        );

    }
    private static IAnimation registerPlayerAnimationLayer(AbstractClientPlayer player) {
        return new ModifierLayer<>();
    }
}