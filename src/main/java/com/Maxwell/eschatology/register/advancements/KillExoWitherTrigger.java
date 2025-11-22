package com.Maxwell.eschatology.register.advancements;import com.Maxwell.eschatology.Eschatology;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;@SuppressWarnings("removal")
public class KillExoWitherTrigger extends SimpleCriterionTrigger<KillExoWitherTrigger.TriggerInstance> {    static final ResourceLocation ID = new ResourceLocation(Eschatology.MODID, "kill_exo_wither");    @Override
    public ResourceLocation getId() {
        return ID;
    }    @Override
    public TriggerInstance createInstance(JsonObject pJson, ContextAwarePredicate pPlayer, DeserializationContext pContext) {
        return new TriggerInstance(pPlayer);
    }    public void trigger(ServerPlayer pPlayer) {
        this.trigger(pPlayer, (triggerInstance) -> true);
    }    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        public TriggerInstance(ContextAwarePredicate pPlayer) {
            super(KillExoWitherTrigger.ID, pPlayer);
        }
    }
}