package com.Maxwell.eschatology.client.EschatologicItem;import com.Maxwell.eschatology.Eschatology;
import com.Maxwell.eschatology.common.Items.EntityRemove;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;@Mod.EventBusSubscriber(modid = Eschatology.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class CosmicSheathRenderer {    @SubscribeEvent
    public static void onRenderTooltip(RenderTooltipEvent.Pre event) {        ItemStack stack = event.getItemStack();        if (!stack.isEmpty() && stack.getItem() instanceof EntityRemove && stack.hasTag() && stack.getTag().getBoolean("is_artifact")) {            int x = event.getX() - 1;
            int y = event.getY() - 1;
            int width = 18;
            int height = 18;             GuiGraphics guiGraphics = event.getGraphics();            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableDepthTest();            float time = (System.currentTimeMillis() % 4000L) / 4000.0f;
            float hue = time;
            int color = Mth.hsvToRgb(hue, 0.8f, 1.0f);             float r = ((color >> 16) & 0xFF) / 255.0f;
            float g = ((color >> 8) & 0xFF) / 255.0f;
            float b = (color & 0xFF) / 255.0f;
            float alpha = 0.4f + (float)(Math.sin(time * 2.0 * Math.PI) * 0.2 + 0.2);             guiGraphics.fill(x, y, x + width, y + height, (int)(alpha * 255) << 24 | (int)(r * 255) << 16 | (int)(g * 255) << 8 | (int)(b * 255));            RenderSystem.disableBlend();
            RenderSystem.enableDepthTest();
        }
    }
}