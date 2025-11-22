package com.Maxwell.eschatology.common.Event;import com.Maxwell.eschatology.Eschatology;
import com.Maxwell.eschatology.common.Items.MagicBulletWeapon.MagicBulletShooterItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;@Mod.EventBusSubscriber(modid = Eschatology.MODID)
public class MagicBulletEvent {
    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        if (!(event.getSource().getEntity() instanceof Player player)) {
            return;
        }        ItemStack heldItem = player.getMainHandItem();
        if (!(heldItem.getItem() instanceof MagicBulletShooterItem)) {
            return;
        }
        if (MagicBulletShooterItem.getBulletCount(heldItem) <= 0) {
            event.setAmount(1.0F);
        }
    }
}
