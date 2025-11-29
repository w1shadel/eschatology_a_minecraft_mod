package com.Maxwell.eschatology.common.Items.Blocks.Eclipse_Forge.Client;import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;import java.util.function.Consumer;public class EFBlockItem extends BlockItem {
    public EFBlockItem(Block block, Properties props) {
        super(block, props);
    }    @Override
    public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer renderer = new EFItemStackRenderer();            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }
        });
    }
}