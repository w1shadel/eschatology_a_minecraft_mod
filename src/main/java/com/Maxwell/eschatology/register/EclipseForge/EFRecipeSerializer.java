package com.Maxwell.eschatology.register.EclipseForge;import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;public class EFRecipeSerializer implements RecipeSerializer<EFRecipe> {
    public static final EFRecipeSerializer INSTANCE = new EFRecipeSerializer();    @Override
    public EFRecipe fromJson(ResourceLocation id, JsonObject json) {
        ItemStack left = ShapedRecipe.itemStackFromJson(json.getAsJsonObject("left"));
        ItemStack right = ShapedRecipe.itemStackFromJson(json.getAsJsonObject("right"));
        ItemStack output = ShapedRecipe.itemStackFromJson(json.getAsJsonObject("output"));
        int time = json.get("time").getAsInt();
        return new EFRecipe(id, left, right, output, time);
    }    @Override
    public EFRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
        ItemStack left = buf.readItem();
        ItemStack right = buf.readItem();
        ItemStack output = buf.readItem();
        int time = buf.readInt();
        return new EFRecipe(id, left, right, output, time);
    }    @Override
    public void toNetwork(FriendlyByteBuf buf, EFRecipe recipe) {
        buf.writeItem(recipe.getLeftInput());
        buf.writeItem(recipe.getRightInput());
        buf.writeItem(recipe.getOutput());
        buf.writeInt(recipe.getProcessTime());
    }
}