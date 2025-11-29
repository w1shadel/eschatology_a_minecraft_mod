package com.Maxwell.eschatology.register.MobEffect;import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;public class FreezingStrikeEffect extends MobEffect {
    public FreezingStrikeEffect(MobEffectCategory category, int color) {
        super(category, color);
    }    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (amplifier >= 9) {
            if (entity.level() instanceof ServerLevel server) {
                entity.hurt(
                        server.damageSources().outOfBorder(),
                        12.0F + amplifier * 1.5F
                );
                for (int i = 0; i < 40; i++) {
                    double ox = (server.random.nextDouble() - 0.5) * 1.5;
                    double oy = server.random.nextDouble() * 1.5;
                    double oz = (server.random.nextDouble() - 0.5) * 1.5;
                    server.sendParticles(ParticleTypes.SNOWFLAKE,
                            entity.getX() + ox,
                            entity.getY() + oy + 0.5,
                            entity.getZ() + oz,
                            1, 0, 0, 0, 0.02);
                }
                entity.playSound(net.minecraft.sounds.SoundEvents.GLASS_BREAK, 1.2F, 0.7F);
                entity.removeEffect(this);
            }
        }
    }    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}