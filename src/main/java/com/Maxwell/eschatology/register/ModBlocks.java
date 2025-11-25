package com.Maxwell.eschatology.register;

import com.Maxwell.eschatology.Eschatology;
import com.Maxwell.eschatology.common.Items.Blocks.AltarOfTheEclipse.AltarOfTheEclipseBlock;
import com.Maxwell.eschatology.common.Items.Blocks.Eclipse_Forge.Client.EFBlockItem;
import com.Maxwell.eschatology.common.Items.Blocks.Eclipse_Forge.EFBlock;
import com.Maxwell.eschatology.common.Items.Blocks.FrozenExoBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Eschatology.MODID);
    public static final RegistryObject<Block> ALTAR_OF_THE_ECLIPSE =
            registerBlock("altar_of_the_eclipse", AltarOfTheEclipseBlock::new);
    public static final RegistryObject<Block> FROZEN_EXO_WITHER =
            registerBlock("frozen_exo_wither", FrozenExoBlock::new);
    public static final RegistryObject<Block> ECLIPSE_FORGE =
            BLOCKS.register("eclipse_forge",
                    () -> new EFBlock(BlockBehaviour.Properties.of().strength(35.0f,1500f)));

    static {
        ModItems.ITEMS.register("eclipse_forge",
                () -> new EFBlockItem(ECLIPSE_FORGE.get(), new Item.Properties()));
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        if (name.equals("eclipse_forge")) return;
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}