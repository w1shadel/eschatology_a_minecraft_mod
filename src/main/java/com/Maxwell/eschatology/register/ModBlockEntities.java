package com.Maxwell.eschatology.register;import com.Maxwell.eschatology.Eschatology;
import com.Maxwell.eschatology.common.Items.Blocks.Eclipse_Forge.EFBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Eschatology.MODID);
    public static final RegistryObject<BlockEntityType<EFBlockEntity>> ECLIPSE_FORGE =
            BLOCK_ENTITIES.register("eclipse_forge",
                    () -> BlockEntityType.Builder.of(
                            EFBlockEntity::new,
                            ModBlocks.ECLIPSE_FORGE.get()
                    ).build(null));    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}