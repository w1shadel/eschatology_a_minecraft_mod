package com.Maxwell.eschatology.common.Items.Blocks.Eclipse_Forge.Client;import com.Maxwell.eschatology.Eschatology;
import com.Maxwell.eschatology.common.Items.Blocks.Eclipse_Forge.EFBlockEntity;
import com.Maxwell.eschatology.common.Items.Blocks.Eclipse_Forge.EFMenu;
import com.Maxwell.eschatology.common.Network.ModMessages;
import com.Maxwell.eschatology.common.Network.StartWorkbenchCraftPacket;
import com.Maxwell.eschatology.register.EclipseForge.EFRecipeType;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
@SuppressWarnings("removal")
public class EFScreen extends AbstractContainerScreen<EFMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(Eschatology.MODID, "textures/gui/eclipse_forge.png");    private float rotation = 0f;    public EFScreen(EFMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        graphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);
        rotation += 2f;
        graphics.pose().pushPose();
        graphics.pose().translate(x + 88, y + 53, 0);
        graphics.pose().mulPose(com.mojang.math.Axis.ZP.rotationDegrees(rotation));
        graphics.blit(TEXTURE, -16, -16, 176, 0, 32, 32);
        graphics.pose().popPose();
    }
    @Override
    protected void init() {
        super.init();        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        this.addRenderableWidget(Button.builder(
                        Component.translatable("gui.eclipse_forge.start"),
                        btn -> {
                            ModMessages.sendToServer(
                                    new StartWorkbenchCraftPacket(menu.getBlockEntity().getBlockPos())
                            );
                        })
                .bounds(x + 65, y + 60, 46, 20)
                .build()
        );
    }
    @Override
    public void containerTick() {
        super.containerTick();        boolean canStart = false;
        var be = menu.getBlockEntity();        if (!menu.isAnimating()) {
            ItemStack left = be.getItemHandler().getStackInSlot(EFBlockEntity.SLOT_LEFT);
            ItemStack right = be.getItemHandler().getStackInSlot(EFBlockEntity.SLOT_RIGHT);            if (!left.isEmpty() && !right.isEmpty()) {
                var level = Minecraft.getInstance().level;
                if (level != null) {                    canStart = level.getRecipeManager()
                            .getAllRecipesFor(EFRecipeType.INSTANCE)
                            .stream()
                            .anyMatch(r ->
                                    ItemStack.isSameItemSameTags(left, r.getLeftInput())
                                            && ItemStack.isSameItemSameTags(right, r.getRightInput())
                            );
                }
            }
        }        for (var widget : this.renderables) {
            if (widget instanceof Button button) {
                button.active = canStart;
            }
        }
    }
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        if (menu.isAnimating()) {
            int ticks = menu.getAnimationTicks();
            renderOrbitingItems(guiGraphics, ticks, partialTicks);
        }        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
    private void renderOrbitingItems(GuiGraphics guiGraphics, int ticks, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        ItemRenderer itemRenderer = mc.getItemRenderer();
        EFBlockEntity be = menu.getBlockEntity();        double leftSlotX = leftPos + 26 + 8;
        double rightSlotX = leftPos + 133 + 8;
        double centerSlotX = leftPos + 80 + 8;
        double centerSlotY = topPos + 33 + 8;
        double radiusStart = (rightSlotX - leftSlotX) / 2.0;
        int totalTicks = be.getCurrentProcessTime();
        if (totalTicks <= 0) totalTicks = 80;         double progress = Math.min(1.0, (double) ticks / (double) totalTicks);
        double radius = radiusStart * (1.0 - progress);
        double angleSpeed = 0.25;        ItemStack left = be.getAnimLeft();
        ItemStack right = be.getAnimRight();        renderOrbitingItem(guiGraphics, itemRenderer, left, centerSlotX, centerSlotY, ticks, 0, radius, angleSpeed, partialTicks);
        renderOrbitingItem(guiGraphics, itemRenderer, right, centerSlotX, centerSlotY, ticks, Math.PI, radius, angleSpeed, partialTicks);
    }
    private void renderOrbitingItem(GuiGraphics g, ItemRenderer renderer, ItemStack stack,
                                    double cx, double cy, int ticks, double phase, double radius, double speed, float partialTicks) {
        if (stack.isEmpty()) return;        double angle = (ticks + partialTicks) * speed + phase;        double x = cx + Math.cos(angle) * radius - 8;
        double y = cy - Math.sin(angle) * radius - 8;        PoseStack pose = g.pose();
        pose.pushPose();        float alpha = (float) Math.max(0.2, radius / 30.0);
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1f, 1f, 1f, alpha);        g.renderItem(stack, (int) x, (int) y);        RenderSystem.disableBlend();
        pose.popPose();
    }
}