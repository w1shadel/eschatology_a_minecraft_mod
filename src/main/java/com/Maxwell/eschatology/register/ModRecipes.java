package com.Maxwell.eschatology.register;import com.Maxwell.eschatology.Eschatology;
import com.Maxwell.eschatology.register.EclipseForge.EFRecipe;
import com.Maxwell.eschatology.register.EclipseForge.EFRecipeSerializer;
import com.Maxwell.eschatology.register.EclipseForge.EFRecipeType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Eschatology.MODID);
    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, Eschatology.MODID);    public static final RegistryObject<RecipeSerializer<EFRecipe>> ECLIPSE_FORGE_SERIALIZER =
            SERIALIZERS.register("eclipse_forge", () -> EFRecipeSerializer.INSTANCE);
    public static final RegistryObject<RecipeType<EFRecipe>> ECLIPSE_FORGE_TYPE =
            TYPES.register("eclipse_forge", () -> EFRecipeType.INSTANCE);
}