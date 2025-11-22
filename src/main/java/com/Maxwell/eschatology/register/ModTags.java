package com.Maxwell.eschatology.register;import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
@SuppressWarnings("removal")
public class ModTags {    public static class Items {
        public static final TagKey<Item> POTATOES = createTag("forge", "potatoes");
        private static TagKey<Item> createTag(String namespace, String path) {
            return ItemTags.create(new ResourceLocation(namespace, path));
        }
    }
}