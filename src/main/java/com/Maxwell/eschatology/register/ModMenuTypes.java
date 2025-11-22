package com.Maxwell.eschatology.register;import com.Maxwell.eschatology.Eschatology;
import com.Maxwell.eschatology.common.Items.Blocks.Eclipse_Forge.EFMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, Eschatology.MODID);    public static final RegistryObject<MenuType<EFMenu>> ECLIPSE_FORGE =
            MENU_TYPES.register("eclipse_forge",
                    () -> IForgeMenuType.create(EFMenu::new));
}