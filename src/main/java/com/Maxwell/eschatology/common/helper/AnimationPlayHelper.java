package com.Maxwell.eschatology.common.helper;import com.Maxwell.eschatology.Eschatology;
import com.Maxwell.eschatology.common.Network.AnimationEndMessage;
import com.Maxwell.eschatology.common.Network.ModMessages;
import com.Maxwell.eschatology.register.ModAnimations;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
@SuppressWarnings("removal")
@Mod.EventBusSubscriber(modid = Eschatology.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class AnimationPlayHelper {    private static AnimationState localPlayerAnimationState = null;
    private static class AnimationState {
        int perspectiveTicks = 0;
        int endTicks = 0;
        CameraType oldPerspective;
        boolean shouldDamage = false;
        boolean lockMovement = false;        AnimationState(KeyframeAnimation anim, boolean damage, boolean lock) {
            this.perspectiveTicks = anim.stopTick;
            this.endTicks = anim.endTick;
            this.shouldDamage = damage;
            this.lockMovement = lock;            this.oldPerspective = Minecraft.getInstance().options.getCameraType();
        }
    }
    public static void playAnimation(Player player, String animationName, boolean damage, boolean lock) {        Minecraft mc = Minecraft.getInstance();        var data = PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) player);
        ModifierLayer<IAnimation> layer = (ModifierLayer<IAnimation>) data.get(ModAnimations.ANIMATION_LAYER_ID);
        if (layer == null) return;        ResourceLocation id = new ResourceLocation(Eschatology.MODID, animationName);
        KeyframeAnimation anim = PlayerAnimationRegistry.getAnimation(id);
        if (anim == null) {
            return;
        }        layer.setAnimation(new KeyframeAnimationPlayer(anim));        if (player == mc.player) {            localPlayerAnimationState = new AnimationState(anim, damage, lock);            mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
        }
    }    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {        if (event.phase != TickEvent.Phase.END || localPlayerAnimationState == null) return;        if (localPlayerAnimationState.endTicks > 0) {
            localPlayerAnimationState.endTicks--;
            if (localPlayerAnimationState.endTicks == 0) {
                if (localPlayerAnimationState.shouldDamage) {
                    ModMessages.sendToServer(new AnimationEndMessage());
                }
            }
        }        if (localPlayerAnimationState.perspectiveTicks > 0) {
            localPlayerAnimationState.perspectiveTicks--;
            if (localPlayerAnimationState.perspectiveTicks == 0) {
                Minecraft.getInstance().options.setCameraType(localPlayerAnimationState.oldPerspective);                localPlayerAnimationState = null;
            }
        }
    }    @SubscribeEvent
    public static void onMovementInput(MovementInputUpdateEvent event) {        if (localPlayerAnimationState != null && localPlayerAnimationState.lockMovement && localPlayerAnimationState.perspectiveTicks > 0) {
            event.getInput().forwardImpulse = 0;
            event.getInput().leftImpulse = 0;
            event.getInput().jumping = false;
            event.getInput().shiftKeyDown = false;
        }
    }
}