package com.Maxwell.eschatology.common.Event;

import com.Maxwell.eschatology.Eschatology;
import com.Maxwell.eschatology.common.Capability.WitchsCrest.WCAttackLevelProvider;
import com.Maxwell.eschatology.common.Items.WitchsCrest.Entity.WitchsSorn;
import com.Maxwell.eschatology.common.Network.ModMessages;
import com.Maxwell.eschatology.common.Network.SyncWCPlayerATKLevelPacket;
import com.Maxwell.eschatology.register.ModItems;
import com.Maxwell.eschatology.register.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

@Mod.EventBusSubscriber(modid = Eschatology.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Witchs_CrestEvent {

    private static final Map<Item, Boolean> POTATO_RECIPE_CACHE = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerEat(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ItemStack eatenItem = event.getItem();
            if (!eatenItem.isEdible()) {
                return;
            }

            boolean hasWitchsCrest = player.getInventory().hasAnyOf(Set.of(ModItems.WITCHS_CREST.get()));

            if (hasWitchsCrest) {

                int levelGain = calculateLevelGain(eatenItem, player.level());

                if (levelGain > 0) {
                    player.getCapability(WCAttackLevelProvider.PLAYER_ATTACK_LEVEL).ifPresent(level -> {
                        level.addLevel(levelGain);

                        player.sendSystemMessage(Component.translatable("guide.potato.level", levelGain));
                        ModMessages.sendToPlayer(new SyncWCPlayerATKLevelPacket(level.getLevel()), player);
                    });

                    applyBeneficialEffects(player, levelGain);

                } else {

                    applyPenaltyEffects(player);
                }
            }
        }
    }
    private static int calculateLevelGain(ItemStack stack, Level level) {

        if (stack.is(Items.POISONOUS_POTATO)) {
            return 10;
        }

        if (hasPotatoInRecipe(stack.getItem(), level)) {
            return 5;
        }

        if (stack.is(ModTags.Items.POTATOES)) {
            return 1;
        }

        return 0; 
    }
    private static boolean hasPotatoInRecipe(Item item, Level level) {

        if (POTATO_RECIPE_CACHE.containsKey(item)) {
            return POTATO_RECIPE_CACHE.get(item);
        }

        boolean containsPotato = false;



        Collection<Recipe<?>> recipes = level.getRecipeManager().getRecipes().stream()
                .filter(r -> r.getResultItem(level.registryAccess()).is(item)) 
                .toList();

        ItemStack referencePotato = new ItemStack(Items.POTATO);

        for (Recipe<?> recipe : recipes) {
            for (Ingredient ingredient : recipe.getIngredients()) {


                if (ingredient.test(referencePotato) || isIngredientPotatoTag(ingredient)) {
                    containsPotato = true;
                    break;
                }
            }
            if (containsPotato) break;
        }

        POTATO_RECIPE_CACHE.put(item, containsPotato);
        return containsPotato;
    }

    private static boolean isIngredientPotatoTag(Ingredient ingredient) {

        for (ItemStack stack : ingredient.getItems()) {
            if (stack.is(ModTags.Items.POTATOES)) return true;
        }
        return false;
    }


    private static void applyBeneficialEffects(ServerPlayer player, int levelGain) {

        int duration = 600 * levelGain; 
        int amplifier = 0;
        if (levelGain >= 10) amplifier = 1; 

        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, duration, amplifier));
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, duration, amplifier));
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, duration, amplifier));
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, duration, amplifier));
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, duration, amplifier));
    }

    private static void applyPenaltyEffects(ServerPlayer player) {
        float penaltyDamage = 2.0F;
        DamageSource penaltySource = player.damageSources().magic();

        List<MobEffect> beneficialEffectsToRemove = new ArrayList<>();
        for (MobEffectInstance effectInstance : player.getActiveEffects()) {
            if (effectInstance.getEffect().isBeneficial()) {
                beneficialEffectsToRemove.add(effectInstance.getEffect());
            }
        }
        beneficialEffectsToRemove.forEach(player::removeEffect);

        int duration = 400;
        int amplifier = 0;
        player.addEffect(new MobEffectInstance(MobEffects.POISON, duration, amplifier));
        player.addEffect(new MobEffectInstance(MobEffects.WITHER, duration, amplifier));
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, duration, amplifier));
        player.addEffect(new MobEffectInstance(MobEffects.HUNGER, duration, amplifier));
        player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, duration, amplifier));
        player.hurt(penaltySource, penaltyDamage);
        player.sendSystemMessage(Component.translatable("guide.potato.donteat").withStyle(ChatFormatting.AQUA));
    }
    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        if (!event.getEntity().level().isClientSide() && event.getEntity().getMainHandItem().is(ModItems.WITCHS_CREST.get())) {
            Player attacker = event.getEntity();
            float selfDamageAmount = 1.0F;
            DamageSource selfDamageSource = attacker.damageSources().magic();
            attacker.hurt(selfDamageSource, selfDamageAmount);
        }
    }
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        DamageSource source = event.getSource();
        if (source.getDirectEntity() instanceof WitchsSorn sorn && sorn.getOwner() instanceof Player attacker) {
            if (attacker.level().isClientSide()) return;
            attacker.getCapability(WCAttackLevelProvider.PLAYER_ATTACK_LEVEL).ifPresent(level -> {
                int attackLevel = level.getLevel();
                if (attackLevel > 0) {
                    float baseDamage = 6.0F;
                    float bonusDamage = attackLevel * 0.5f;
                    event.setAmount(baseDamage + bonusDamage);
                }
            });
            return;
        }
        if (source.getEntity() instanceof Player attacker) {
            if (attacker.level().isClientSide()) return;
            if (attacker.getMainHandItem().is(ModItems.WITCHS_CREST.get())) {
                attacker.getCapability(WCAttackLevelProvider.PLAYER_ATTACK_LEVEL).ifPresent(level -> {
                    int attackLevel = level.getLevel();
                    if (attackLevel > 0) {
                        float originalDamage = event.getAmount();
                        float bonusDamage = attackLevel * 0.5f;
                        event.setAmount(originalDamage + bonusDamage);
                    }
                });
            }
        }
    }
}
