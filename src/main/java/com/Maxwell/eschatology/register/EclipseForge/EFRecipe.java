package com.Maxwell.eschatology.register.EclipseForge;import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;public class EFRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final ItemStack leftInput;
    private final ItemStack rightInput;
    private final ItemStack output;
    private final int processTime;    public EFRecipe(ResourceLocation id, ItemStack leftInput, ItemStack rightInput, ItemStack output, int processTime) {
        this.id = id;
        this.leftInput = leftInput;
        this.rightInput = rightInput;
        this.output = output;
        this.processTime = processTime;
    }    public ItemStack getLeftInput() { return leftInput; }
    public ItemStack getRightInput() { return rightInput; }
    public ItemStack getOutput() { return output.copy(); }
    public int getProcessTime() {
        return processTime * 20;
    }    @Override
    public boolean matches(Container container, Level level) {
        if (level.isClientSide) return false;
        ItemStack left = container.getItem(0);
        ItemStack right = container.getItem(1);
        return ItemStack.isSameItemSameTags(left, leftInput)
                && ItemStack.isSameItemSameTags(right, rightInput);
    }    @Override
    public ItemStack assemble(Container pContainer, RegistryAccess pRegistryAccess) {
        return output.copy();
    }
    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return output.copy();
    }    @Override
    public ResourceLocation getId() {
        return id;
    }    @Override
    public RecipeSerializer<?> getSerializer() {
        return EFRecipeSerializer.INSTANCE;
    }    @Override
    public RecipeType<?> getType() {
        return EFRecipeType.INSTANCE;
    }
}
