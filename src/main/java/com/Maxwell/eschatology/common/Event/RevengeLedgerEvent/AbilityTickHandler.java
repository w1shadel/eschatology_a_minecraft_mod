package com.Maxwell.eschatology.common.Event.RevengeLedgerEvent;import com.Maxwell.eschatology.Eschatology;
import com.Maxwell.eschatology.Balance.ModConstants;
import com.Maxwell.eschatology.common.Network.ModMessages;
import com.Maxwell.eschatology.common.Network.SyncRLAbilityGuiPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;@Mod.EventBusSubscriber(modid = Eschatology.MODID)
public class AbilityTickHandler {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide) return;
        ServerPlayer player = (ServerPlayer) event.player;
        CompoundTag data = player.getPersistentData();
        if (!data.getBoolean("revenge_skill_active")) return;
        ServerLevel level = player.serverLevel();
        int stage = data.getInt("revenge_skill_stage");
        int timer = data.getInt("revenge_skill_timer");
        data.putInt("revenge_skill_timer", timer + 1);
        player.fallDistance = 0;
        player.setInvulnerable(true);
        player.setNoGravity(true);
        LivingEntity target = null;
        if (data.hasUUID("revenge_skill_target")) {
            Entity entity = level.getEntity(data.getUUID("revenge_skill_target"));
            if (!(entity instanceof LivingEntity le && le.isAlive())) {
                finishAbility(player);
                return;
            }
            target = le;
            if (target.getPersistentData().getBoolean("revenge_skill_frozen")) {
                target.teleportTo(target.getX(), target.getY(), target.getZ());
            }
        } else if (stage < 6) {
            finishAbility(player);
            return;
        }
        switch (stage) {
            case 0:
                if (timer == 5) {
                    ModMessages.sendToPlayer(new SyncRLAbilityGuiPacket(false, "ability.revenge_ledger.dialogue1"), player);
                }
                if (timer > 10) {
                    target.hurt(level.damageSources().playerAttack(player), ModConstants.Revenge.STAGE0_DAMAGE);
                    Vec3 lookDirection = player.getLookAngle();
                    Vec3 horizontalLook = new Vec3(lookDirection.x, 0, lookDirection.z).normalize();
                    Vec3 knockbackVector = horizontalLook.scale(ModConstants.Revenge.STAGE0_KNOCKBACK_XZ)
                            .add(0, ModConstants.Revenge.STAGE0_KNOCKBACK_Y, 0);
                    target.getPersistentData().remove("revenge_skill_frozen");
                    data.putInt("revenge_skill_stage", 1);
                    data.putInt("revenge_skill_timer", 0);
                }
                break;
            case 1:
                player.teleportTo(player.getX(), player.getY(), player.getZ());
                if (timer > ModConstants.Revenge.PAUSE_DURATION) {
                    data.putDouble("revenge_skill_start_y", player.getY());
                    data.putInt("revenge_skill_stage", 2);
                    data.putInt("revenge_skill_timer", 0);
                }
                break;
            case 2:
                double startY = data.getDouble("revenge_skill_start_y");
                float progress = (float) timer / ModConstants.Revenge.JUMP_DURATION;
                if (progress > 1.0f) progress = 1.0f;
                if (timer == 1) {
                    ModMessages.sendToPlayer(new SyncRLAbilityGuiPacket(false, "ability.revenge_ledger.dialogue2"), player);
                }
                double yOffset = ModConstants.Revenge.JUMP_HEIGHT * Mth.sin(progress * (float) (Math.PI / 2.0));
                player.teleportTo(player.getX(), startY + yOffset, player.getZ());
                if (timer == 5) {
                    level.playSound(null, player.blockPosition(), SoundEvents.TRIDENT_RIPTIDE_3, SoundSource.PLAYERS, 1.5F, 0.8F);
                }
                if (timer > ModConstants.Revenge.JUMP_DURATION) {
                    player.setDeltaMovement(0, 0, 0);
                    data.putInt("revenge_skill_stage", 3);
                    data.putInt("revenge_skill_timer", 0);
                }
                break;
            case 3:
                player.setDeltaMovement(0, 0, 0);
                player.teleportTo(player.getX(), player.getY(), player.getZ());
                if (timer > ModConstants.Revenge.HOVER_DURATION) {
                    data.putInt("revenge_skill_stage", 4);
                    data.putInt("revenge_skill_timer", 0);
                }
                break;
            case 4:
                if (target == null) {
                    finishAbility(player);
                    break;
                }
                Vec3 currentPos = player.position();
                Vec3 targetPos = target.position().add(0, target.getBbHeight() / 2.0, 0);
                Vec3 nextPos = currentPos.lerp(targetPos, ModConstants.Revenge.DASH_LERP);
                if (player.distanceToSqr(target) < ModConstants.Revenge.DASH_TRIGGER_DISTANCE_SQR || timer > ModConstants.Revenge.DASH_TIMEOUT) {
                    data.putInt("revenge_skill_stage", 5);
                    data.putInt("revenge_skill_timer", 0);
                }
                break;
            case 5:
                if (timer == 1) {
                    Vec3 impactPos = player.position();
                    level.explode(null, impactPos.x, impactPos.y, impactPos.z, ModConstants.Revenge.STAGE5_EXPLOSION_RADIUS, Level.ExplosionInteraction.NONE);
                    level.playSound(null, player.blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 2.0F, 0.5F);
                    if (target != null && target.distanceToSqr(impactPos) < ModConstants.Revenge.STAGE5_DAMAGE_RANGE_SQR) {
                        target.hurt(level.damageSources().playerAttack(player), ModConstants.Revenge.STAGE5_EXPLOSION_DAMAGE);
                    }
                    ModMessages.sendToPlayer(new SyncRLAbilityGuiPacket(false, "ability.revenge_ledger.dialogue3"), player);
                    int radius = ModConstants.Revenge.STAGE5_BLOCK_DESTROY_RADIUS;
                    BlockPos center = player.blockPosition();
                    for (BlockPos pos : BlockPos.betweenClosed(center.offset(-radius, -2, -radius), center.offset(radius, 2, radius))) {
                        BlockState state = level.getBlockState(pos);
                        if (!state.isAir() && state.getDestroySpeed(level, pos) >= 0 && state.getBlock() != Blocks.BEDROCK) {
                            level.removeBlock(pos, false);
                            FallingBlockEntity fallingBlock = FallingBlockEntity.fall(level, pos, state);
                            fallingBlock.setDeltaMovement((level.random.nextDouble() - 0.5) * 0.5, level.random.nextDouble() * 1.2 + 0.5, (level.random.nextDouble() - 0.5) * 0.5);
                            fallingBlock.getPersistentData().putBoolean("revenge_skill_block", true);
                        }
                    }
                }
                if (timer > 30) {
                    data.putInt("revenge_skill_stage", 6);
                    data.putInt("revenge_skill_timer", 0);
                }
                break;
            case 6:
                finishAbility(player);
                break;
        }
    }

    private static void finishAbility(ServerPlayer player) {
        player.setInvulnerable(false);
        if (!player.isCreative() && !player.isSpectator()) {
            player.setNoGravity(false);
        }
        CompoundTag data = player.getPersistentData();
        data.remove("revenge_skill_active");
        data.remove("revenge_skill_stage");
        data.remove("revenge_skill_timer");
        data.remove("revenge_skill_target");
        data.remove("revenge_skill_start_y");
        ModMessages.sendToPlayer(new SyncRLAbilityGuiPacket(true, ""), player);
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        DamageSource source = event.getSource();
        if (source.getDirectEntity() instanceof FallingBlockEntity fallingBlock) {
            if (fallingBlock.getPersistentData().getBoolean("revenge_skill_block")) {
                event.setAmount(ModConstants.Revenge.FALLING_BLOCK_DAMAGE);
            }
        }
    }
}
