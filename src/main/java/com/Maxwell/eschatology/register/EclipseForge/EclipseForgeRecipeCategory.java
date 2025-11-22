package com.Maxwell.eschatology.register.EclipseForge;import com.Maxwell.eschatology.Eschatology;
import com.Maxwell.eschatology.register.ModBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
@SuppressWarnings("removal")
public class EclipseForgeRecipeCategory implements IRecipeCategory<EFRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(Eschatology.MODID, "eclipse_forge");
    public static final ResourceLocation TEXTURE = new ResourceLocation(Eschatology.MODID, "textures/gui/eclipse_forge_jei.png");
    public static final RecipeType<EFRecipe> RECIPE_TYPE = new RecipeType<>(UID, EFRecipe.class);    private final IDrawable background;
    private final IDrawable icon;    public EclipseForgeRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE, 0, 0, 176, 85);
        ItemStack forgeIcon = new ItemStack(ModBlocks.ECLIPSE_FORGE.get());
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, forgeIcon);
    }    @Override
    public RecipeType<EFRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }    @Override
    public Component getTitle() {
        return Component.translatable("block.eschatology.eclipse_forge");
    }    @Override
    public IDrawable getBackground() {
        return background;
    }    @Override
    public IDrawable getIcon() {
        return icon;
    }    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, EFRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 26, 33)
                .addItemStack(recipe.getLeftInput());
        builder.addSlot(RecipeIngredientRole.INPUT, 133, 33)
                .addItemStack(recipe.getRightInput());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 80, 33)
                .addItemStack(recipe.getOutput());
    }
    @Override
    public void draw(EFRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Minecraft mc = Minecraft.getInstance();
        Font font = mc.font;
        float seconds = recipe.getProcessTime() / 20f;
        String timeText = String.format("Time: %.1f s", seconds);
        int x = 6;
        int y = background.getHeight() - 10;        guiGraphics.drawString(font, timeText, x, y, 0x808080, false);
    }
}