package com.Maxwell.eschatology.client.GUI.Bossbar;

import com.Maxwell.eschatology.Eschatology;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.BossEvent;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;

import java.util.HashMap;
import java.util.Map;
@SuppressWarnings("removal")
public class MaxwellCustomBossBar {
    public static Map<Integer, MaxwellCustomBossBar> customBossBars = new HashMap();
    private final ResourceLocation baseTexture;
    private final ResourceLocation overlayTexture;
    private final boolean hasOverlay;
    private final int baseHeight;
    private final int baseTextureHeight;
    private final int baseOffsetX;
    private final int baseOffsetY;
    private final int overlayOffsetX;
    private final int overlayOffsetY;
    private final int overlayWidth;
    private final int overlayHeight;
    private final int verticalIncrement;
    private final int getProgress;
    private final ChatFormatting textColor;
    private static final ResourceLocation CUSTOM_FONT = new ResourceLocation(Eschatology.MODID, "default");
    public MaxwellCustomBossBar(ResourceLocation baseTexture, ResourceLocation overlayTexture, int baseHeight, int baseTextureHeight, int baseOffsetX, int baseOffsetY, int overlayOffsetX, int overlayOffsetY, int overlayWidth, int overlayHeight, int verticalIncrement, int getProgress, ChatFormatting textColor) {
        this.baseTexture = baseTexture;
        this.overlayTexture = overlayTexture;
        this.hasOverlay = overlayTexture != null;
        this.baseHeight = baseHeight;
        this.baseTextureHeight = baseTextureHeight;
        this.baseOffsetX = baseOffsetX;
        this.baseOffsetY = baseOffsetY;
        this.overlayOffsetX = overlayOffsetX;
        this.overlayOffsetY = overlayOffsetY;
        this.overlayWidth = overlayWidth;
        this.overlayHeight = overlayHeight;
        this.verticalIncrement = verticalIncrement;
        this.getProgress = getProgress;
        this.textColor = textColor;
    }    public ResourceLocation getBaseTexture() {
        return this.baseTexture;
    }    public ResourceLocation getOverlayTexture() {
        return this.overlayTexture;
    }    public boolean hasOverlay() {
        return this.hasOverlay;
    }    public int getBaseHeight() {
        return this.baseHeight;
    }    public int getBaseTextureHeight() {
        return this.baseTextureHeight;
    }    public int getBaseOffsetX() {
        return this.baseOffsetX;
    }    public int getBaseOffsetY() {
        return this.baseOffsetY;
    }    public int getOverlayOffsetX() {
        return this.overlayOffsetX;
    }    public int getOverlayOffsetY() {
        return this.overlayOffsetY;
    }    public int getOverlayWidth() {
        return this.overlayWidth;
    }    public int getOverlayHeight() {
        return this.overlayHeight;
    }    public int getProgress() {
        return this.getProgress;
    }    public int getVerticalIncrement() {
        return this.verticalIncrement;
    }    public ChatFormatting getTextColor() {
        return this.textColor;
    }    public void renderBossBar(CustomizeGuiOverlayEvent.BossEventProgress event) {
        GuiGraphics guiGraphics = event.getGuiGraphics();
        int y = event.getY();
        int i = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int j = y - 9;
        Minecraft mc = Minecraft.getInstance();
        mc.getProfiler().push("CustomBossBar");
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, this.getBaseTexture());
        this.drawBar(guiGraphics, event.getX() + this.getBaseOffsetX(), y + this.getBaseOffsetY(), event.getBossEvent());
        Style fontStyle = Style.EMPTY.withFont(CUSTOM_FONT);
        Component component = event.getBossEvent().getName().copy().withStyle(this.getTextColor()).withStyle(fontStyle);
        int l = mc.font.width(component);
        int i1 = i / 2 - l / 2;
        guiGraphics.drawString(mc.font, component, i1, j, 16777215);
        if (this.hasOverlay()) {
            mc.getProfiler().push("CustomBossBarOverray");
            RenderSystem.setShaderTexture(0, this.getOverlayTexture());
            event.getGuiGraphics().blit(this.getOverlayTexture(), event.getX() + this.getBaseOffsetX() + this.getOverlayOffsetX(), y + this.getOverlayOffsetY() + this.getBaseOffsetY(), 0.0F, 0.0F, this.getOverlayWidth(), this.getOverlayHeight(), this.getOverlayWidth(), this.getOverlayHeight());
            mc.getProfiler().pop();
        }
        event.setIncrement(this.getVerticalIncrement());
    }    private void drawBar(GuiGraphics guiGraphics, int x, int y, BossEvent event) {
        guiGraphics.blit(this.getBaseTexture(), x, y, 0.0F, 0.0F, this.getProgress(), this.getBaseHeight(), 256, this.getBaseTextureHeight());
        int i = (int)(event.getProgress() * (float)(this.getProgress() + 1));
        if (i > 0) {
            guiGraphics.blit(this.getBaseTexture(), x, y, 0.0F, (float)this.getBaseHeight(), i, this.getBaseHeight(), 256, this.getBaseTextureHeight());
        }    }    static {
        customBossBars.put(0, new MaxwellCustomBossBar(new ResourceLocation(Eschatology.MODID, "textures/gui/boss_bar/blackbool_bar_base.png"), new ResourceLocation(Eschatology.MODID, "textures/gui/boss_bar/blackbool_bar_overlay.png"), 5, 16, 1, 1, -2, -2, 256, 16, 25, 182, ChatFormatting.DARK_PURPLE));
        customBossBars.put(1, new MaxwellCustomBossBar(new ResourceLocation(Eschatology.MODID, "textures/gui/boss_bar/exo_wither_bar_base.png"), new ResourceLocation(Eschatology.MODID, "textures/gui/boss_bar/exo_wither_overray.png"), 5, 16, 1, 1, -2, -2, 256, 16, 25, 182, ChatFormatting.WHITE));
    }
}
