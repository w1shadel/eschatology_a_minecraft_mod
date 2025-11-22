package com.Maxwell.eschatology.client.ExoHeart;import com.Maxwell.eschatology.Eschatology;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;@Mod.EventBusSubscriber(modid = Eschatology.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ExoHeartKeybind {
    public static final String CATEGORY = "key.categories.eschatology";
    public static final String KEY_NAME = "key.eschatology.activate_exoheart";    public static KeyMapping ACTIVATE_EXOHEART;    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        ACTIVATE_EXOHEART = new KeyMapping(
                KEY_NAME,
                GLFW.GLFW_KEY_R,
                CATEGORY
        );
        event.register(ACTIVATE_EXOHEART);
    }
}