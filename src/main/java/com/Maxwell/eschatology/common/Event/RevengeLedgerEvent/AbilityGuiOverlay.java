package com.Maxwell.eschatology.common.Event.RevengeLedgerEvent;import com.Maxwell.eschatology.Eschatology;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;import java.util.LinkedList;
import java.util.Queue;@Mod.EventBusSubscriber(modid = Eschatology.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class AbilityGuiOverlay {    private static final Queue<Dialogue> dialogueQueue = new LinkedList<>();
    private static Dialogue currentDialogue = null;    private static boolean guiVisible = true;    private static class Dialogue {
        final Component text;
        final int durationTicks;
        int timer;        Dialogue(String key, int durationTicks) {
            this.text = Component.translatable(key);
            this.durationTicks = durationTicks;
            this.timer = 0;
        }        void tick() {
            this.timer++;
        }        float getAlpha() {
            int fadeInTime = 10;
            int fadeOutTime = 15;             if (timer < fadeInTime) {
                return (float) timer / fadeInTime;
            }
            if (timer > durationTicks - fadeOutTime) {
                return 1.0f - (float)(timer - (durationTicks - fadeOutTime)) / fadeOutTime;
            }
            return 1.0f;
        }        boolean isFinished() {
            return timer >= durationTicks;
        }
    }    public static void setGuiVisible(boolean visible) {
        guiVisible = visible;
        if (visible) {
            dialogueQueue.clear();
        }
    }
    public static void setDisplayText(String key) {
        if (!key.isEmpty()) {
            dialogueQueue.add(new Dialogue(key, 50));
        }
    }
    @SubscribeEvent
    public static void onRenderGuiPost(RenderGuiOverlayEvent.Post event) {
        if (!event.getOverlay().id().equals(VanillaGuiOverlay.VIGNETTE.id())) {
            return;
        }        if (currentDialogue == null && !dialogueQueue.isEmpty()) {
            currentDialogue = dialogueQueue.poll();
        }        if (currentDialogue == null) {
            return;
        }
        currentDialogue.tick();        Minecraft mc = Minecraft.getInstance();
        GuiGraphics gg = event.getGuiGraphics();
        Font font = mc.font;
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();        Component textToRender = currentDialogue.text;
        if (textToRender == null) return;        float scale = 1.5f;
        float verticalRatio = 0.75f;
        float alpha = currentDialogue.getAlpha();        if (alpha <= 0.01f) {
            if (currentDialogue.isFinished()) {
                currentDialogue = null;
            }
            return;
        }
        int alphaInt = ((int)(alpha * 255)) << 24;
        int purpleColor = 0xBC13FE;
        int grayOutlineColor = 0x4E4E4E;
        int finalPurple = alphaInt | purpleColor;
        int finalGray = alphaInt | grayOutlineColor;
        gg.pose().pushPose();
        gg.pose().scale(scale, scale, scale);
        int textWidth = font.width(textToRender);
        int x = (int) ((screenWidth / 2f - textWidth / 2f * scale) / scale);
        int y = (int) ((screenHeight * verticalRatio) / scale);
        gg.drawString(font, textToRender, x - 1, y, finalGray, false);
        gg.drawString(font, textToRender, x + 1, y, finalGray, false);
        gg.drawString(font, textToRender, x, y - 1, finalGray, false);
        gg.drawString(font, textToRender, x, y + 1, finalGray, false);
        gg.drawString(font, textToRender, x, y, finalPurple, false);
        gg.pose().popPose();
        if (currentDialogue.isFinished()) {
            currentDialogue = null;
        }
    }
}