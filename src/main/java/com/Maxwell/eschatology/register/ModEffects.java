package com.Maxwell.eschatology.register;import com.Maxwell.eschatology.Eschatology;
import com.Maxwell.eschatology.register.MobEffect.Awe_Contempt.ContemptEffect;
import com.Maxwell.eschatology.register.MobEffect.Awe_Contempt.CounterStanceEffect;
import com.Maxwell.eschatology.register.MobEffect.Awe_Contempt.GazeEffect;
import com.Maxwell.eschatology.register.MobEffect.Awe_Contempt.SelfContemptEffect;
import com.Maxwell.eschatology.register.MobEffect.FreezingStrikeEffect;
import com.Maxwell.eschatology.register.MobEffect.GravityBoundEffect;
import com.Maxwell.eschatology.register.MobEffect.InfernalFlamesEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Eschatology.MODID);    public static final RegistryObject<MobEffect> GRAVITY_BOUND = MOB_EFFECTS.register("gravity_bound",
            () -> new GravityBoundEffect(MobEffectCategory.HARMFUL, 0x5a00a1));
    public static final RegistryObject<MobEffect> INFERNAL_FLAMES = MOB_EFFECTS.register("infernal_flames",
            InfernalFlamesEffect::new);
    public static final RegistryObject<MobEffect> GAZE = MOB_EFFECTS.register("gaze",
            GazeEffect::new);
    public static final RegistryObject<MobEffect> CONTEMPT = MOB_EFFECTS.register("contempt",
            ContemptEffect::new);
    public static final RegistryObject<MobEffect> SELF_CONTEMPT = MOB_EFFECTS.register("self_contempt",
            SelfContemptEffect::new);    public static final RegistryObject<MobEffect> COUNTER_STANCE = MOB_EFFECTS.register("counter_stance",
            CounterStanceEffect::new);    public static final RegistryObject<MobEffect> FREEZING_STRIKE = MOB_EFFECTS.register("freezing_strike",
            () -> new FreezingStrikeEffect(MobEffectCategory.BENEFICIAL, 0x5a00a1));}