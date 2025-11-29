package com.Maxwell.eschatology.common.Items.Awe_Contempt.Event;import com.Maxwell.eschatology.Eschatology;
import com.Maxwell.eschatology.Balance.ModConstants;
import com.Maxwell.eschatology.register.ModEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;@Mod.EventBusSubscriber(modid = Eschatology.MODID)
public class ContemptHandler {
    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        LivingEntity target = event.getEntity();        if (target.hasEffect(ModEffects.CONTEMPT.get())) {
            event.setAmount(event.getAmount() * ModConstants.Counter.CONTEMPT_DAMAGE_MULT);
        }
        if (target.hasEffect(ModEffects.SELF_CONTEMPT.get())) {
            event.setAmount(event.getAmount() * ModConstants.Counter.SELF_CONTEMPT_DAMAGE_MULT);
        }
    }
}