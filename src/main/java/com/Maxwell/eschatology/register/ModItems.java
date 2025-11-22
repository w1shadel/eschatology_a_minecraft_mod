package com.Maxwell.eschatology.register;

import com.Maxwell.eschatology.Eschatology;
import com.Maxwell.eschatology.common.Items.*;
import com.Maxwell.eschatology.common.Items.Apocalypse.Apocalypse;
import com.Maxwell.eschatology.common.Items.Awe_Contempt.Awe_contempt;
import com.Maxwell.eschatology.common.Items.Curios.Frozen_ExoHeart;
import com.Maxwell.eschatology.common.Items.Curios.The_once_and_future_gun;
import com.Maxwell.eschatology.common.Items.FellBullet.FellBullet;
import com.Maxwell.eschatology.common.Items.MagicBulletWeapon.MagicBulletShooterItem;
import com.Maxwell.eschatology.common.Items.WitchsCrest.WitchsCrestItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<CreativeModeTab> TABS;
    public static final RegistryObject<CreativeModeTab> Eschatology_TAB;    public static final DeferredRegister<Item> ITEMS;
    public static final RegistryObject<Item> DEMOND;
    public static final RegistryObject<Item> ENTITY_REMOVER;
    public static final RegistryObject<Item> APOCALYPSE;
    public static final RegistryObject<Item> FROSTED_SCRAP;
    public static final RegistryObject<Item> A_TIME_CRYSTAL;
    public static final RegistryObject<Item> FROZEN_EXOHEART;
    public static final RegistryObject<Item> SINGULARITC_INGOT;
    public static final RegistryObject<Item> SINGULARITY_CORE;
    public static final RegistryObject<Item> WITCHS_CREST;
    public static final RegistryObject<Item> REVENGE_LEDGER;
    public  static final RegistryObject<Item> THE_ONCE_AND_FUTURE_GUN;
    public  static final RegistryObject<Item> AWE_CONTEMPT;
    public  static final RegistryObject<Item> APISTLE;
    public static final RegistryObject<Item> MAGIC_BULLETS;    public static final RegistryObject<Item> FELL_BULLETS;
    static
    {
        TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Eschatology.MODID);
        ITEMS = DeferredRegister.create(Registries.ITEM,Eschatology.MODID);
        DEMOND = ITEMS.register("demond", () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));
        FROSTED_SCRAP = ITEMS.register("frosted_scrap", () -> new Frosted_Scrap(new Item.Properties().rarity(Rarity.UNCOMMON)));
        A_TIME_CRYSTAL = ITEMS.register("a_time_crystal", () -> new A_time_Crystal(new Item.Properties().rarity(Rarity.UNCOMMON)));
        APISTLE = ITEMS.register("apitle", () -> new Apistle(new Item.Properties().rarity(Rarity.EPIC)));
        SINGULARITC_INGOT = ITEMS.register("singularitc_ingot", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
        FROZEN_EXOHEART = ITEMS.register("frozen_exoheart", () -> new Frozen_ExoHeart(new Item.Properties().rarity(Rarity.EPIC)));
        SINGULARITY_CORE = ITEMS.register("singularity_core", () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));
        AWE_CONTEMPT = ITEMS.register("awe_contempt", () -> new Awe_contempt(ModTiers.WITCHS_CREST_TIER,10,-2.9f,new Item.Properties().rarity(Rarity.EPIC)));
        THE_ONCE_AND_FUTURE_GUN = ITEMS.register("the_once_and_future_gun", () -> new The_once_and_future_gun(new Item.Properties().rarity(Rarity.EPIC)));
        FELL_BULLETS = ITEMS.register("fell_bullet_weapon", () -> new FellBullet(ModTiers.WITCHS_CREST_TIER,0,-3.3f,new Item.Properties().rarity(Rarity.EPIC)));
        WITCHS_CREST = ITEMS.register("witchs_crest", () -> new WitchsCrestItem(Tiers.DIAMOND,0,-2.8f,new Item.Properties().rarity(Rarity.EPIC)));
        REVENGE_LEDGER = ITEMS.register("revenge_ledger", () -> new RevengeLedgerItem(Tiers.DIAMOND,3,-1.0f,new Item.Properties().rarity(Rarity.EPIC)));
        MAGIC_BULLETS = ITEMS.register("magic_bullet", () -> new MagicBulletShooterItem(Tiers.DIAMOND,6,-2.8f,new Item.Properties().rarity(Rarity.EPIC)));
        APOCALYPSE = ITEMS.register("apocalypse", () -> new Apocalypse(Tiers.DIAMOND,11,-2.4f,new Item.Properties().rarity(Rarity.EPIC)));
        ENTITY_REMOVER = ITEMS.register("haetataki", () -> new EntityRemove(new Item.Properties().rarity(Rarity.EPIC)));
        Eschatology_TAB = TABS.register("eschatology", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.Eschatology.items")).icon(() ->
                new ItemStack(DEMOND.get())).displayItems((enabledFeatures, entries) ->
        {
            entries.accept(DEMOND.get());
            entries.accept(FROSTED_SCRAP.get());
            entries.accept(A_TIME_CRYSTAL.get());
            entries.accept(SINGULARITC_INGOT.get());
            entries.accept(SINGULARITY_CORE.get());
            entries.accept(ModBlocks.ALTAR_OF_THE_ECLIPSE.get());
            entries.accept(ModBlocks.FROZEN_EXO_WITHER.get());
            entries.accept(ModBlocks.ECLIPSE_FORGE.get());
            entries.accept(APOCALYPSE.get());
            entries.accept(MAGIC_BULLETS.get());
            entries.accept(FELL_BULLETS.get());
            entries.accept(AWE_CONTEMPT.get());
            entries.accept(THE_ONCE_AND_FUTURE_GUN.get());
            entries.accept(WITCHS_CREST.get());
            entries.accept(REVENGE_LEDGER.get());
            entries.accept(ENTITY_REMOVER.get());
            entries.accept(FROZEN_EXOHEART.get());
        }).build());
    }
}
