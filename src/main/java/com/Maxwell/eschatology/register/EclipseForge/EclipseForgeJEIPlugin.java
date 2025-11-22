package com.Maxwell.eschatology.register.EclipseForge;import com.Maxwell.eschatology.Eschatology;
import com.Maxwell.eschatology.register.ModBlocks;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
@SuppressWarnings("removal")
@JeiPlugin
public class EclipseForgeJEIPlugin implements IModPlugin {
    public static final ResourceLocation UID = new ResourceLocation(Eschatology.MODID, "eclipse_forge_plugin");    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new EclipseForgeRecipeCategory(registration.getJeiHelpers().getGuiHelper())
        );
    }    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        Level level = Minecraft.getInstance().level;
        if (level == null) return;        var recipes = level.getRecipeManager().getAllRecipesFor(EFRecipeType.INSTANCE);
        registration.addRecipes(EclipseForgeRecipeCategory.RECIPE_TYPE, recipes);
    }
    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(
                new ItemStack(ModBlocks.ECLIPSE_FORGE.get()),
                EclipseForgeRecipeCategory.RECIPE_TYPE
        );
    }
}