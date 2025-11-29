package com.Maxwell.eschatology.client.ExoHeart;import com.Maxwell.eschatology.Eschatology;
import com.Maxwell.eschatology.common.Capability.ExoHeart.ExoHeartData;
import com.Maxwell.eschatology.Balance.ModConstants;
import com.Maxwell.eschatology.register.ModItems;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;
@SuppressWarnings("removal")
@Mod.EventBusSubscriber(modid = Eschatology.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ExoHeartHudOverlay {
    private static final ResourceLocation EXOHEART_HUD = new ResourceLocation(Eschatology.MODID, ModConstants.ExoHeart.TEXTURE_PATH);
    public static int clientCharge = 0;    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui || mc.player.isDeadOrDying()) return;
        boolean hasHeart = CuriosApi.getCuriosHelper()
                .findEquippedCurio(ModItems.FROZEN_EXOHEART.get(), mc.player)
                .isPresent();
        if (!hasHeart) return;
        int charge = mc.player.getCapability(ExoHeartData.CAPABILITY)
                .map(ExoHeartData::getCharge)
                .orElse(clientCharge);
        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();
        int x = width / 2 + ModConstants.ExoHeart.HUD_X_OFFSET;
        int y = height - ModConstants.ExoHeart.HUD_Y_OFFSET;
        GuiGraphics guiGraphics = event.getGuiGraphics();
        RenderSystem.enableBlend();
        RenderSystem.setShaderTexture(0, EXOHEART_HUD);
        guiGraphics.blit(EXOHEART_HUD, x, y, 0, 0, 182, 10, 256, 256);
        int filled = Math.min(182, (int) (charge * ModConstants.ExoHeart.CHARGE_BAR_SCALE));
        guiGraphics.blit(EXOHEART_HUD, x, y, 0, 10, filled, 10, 256, 256);        RenderSystem.disableBlend();        guiGraphics.drawString(mc.font, "ExoCharge: " + charge + "%", x, y - ModConstants.ExoHeart.HUD_TEXT_OFFSET, ModConstants.ExoHeart.TEXT_COLOR, false);
    }
}