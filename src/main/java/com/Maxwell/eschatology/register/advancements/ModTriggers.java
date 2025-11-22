package com.Maxwell.eschatology.register.advancements;import net.minecraft.advancements.CriteriaTriggers;public class ModTriggers {
    public static final KillBlackBoolTrigger KILL_BLACK_BOOL = new KillBlackBoolTrigger();
    public static final KillExoWitherTrigger KILL_EXO_WITHER_TRIGGER = new KillExoWitherTrigger();    public static void register() {
        CriteriaTriggers.register(KILL_BLACK_BOOL);
        CriteriaTriggers.register(KILL_EXO_WITHER_TRIGGER);
    }
}