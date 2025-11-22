package com.Maxwell.eschatology.register;import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.Tags;public class ModTiers {
    public static final ForgeTier WITCHS_CREST_TIER = new ForgeTier(
            4,
            1200,
            8.0F,
            2.0F,
            20,
            Tags.Blocks.NEEDS_NETHERITE_TOOL,
            () -> Ingredient.of(Items.AMETHYST_SHARD)
    );
}