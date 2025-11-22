package com.Maxwell.eschatology.common.Items.Curios;import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
public class CuriosItem extends Item implements ICurioItem {
    public CuriosItem(Properties properties) {
        super(properties);    }    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack)
    {
        return true;
    }}