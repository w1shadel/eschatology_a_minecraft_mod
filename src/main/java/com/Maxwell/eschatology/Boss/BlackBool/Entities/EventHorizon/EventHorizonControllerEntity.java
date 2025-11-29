package com.Maxwell.eschatology.Boss.BlackBool.Entities.EventHorizon;import com.Maxwell.eschatology.Boss.BlackBool.BlackBool;
import com.Maxwell.eschatology.Boss.BlackBool.Entities.LightOrb.LightOrbEntity;
import com.Maxwell.eschatology.register.ModEffects;
import com.Maxwell.eschatology.register.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;import java.util.List;
import java.util.UUID;public class EventHorizonControllerEntity extends Entity {
    private UUID ownerUUID;
    private BlackBool owner;    private static final double ARENA_RADIUS = 64.0;
    private int orbCooldown = 0;    public EventHorizonControllerEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noCulling = true;
        this.noPhysics = true;
    }    public EventHorizonControllerEntity(Level pLevel, BlackBool owner) {
        this(ModEntities.EVENT_HORIZON_CONTROLLER.get(), pLevel);
        this.setOwner(owner);
        this.setPos(owner.position());
    }    public void setOwner(BlackBool owner) {
        this.owner = owner;
        this.ownerUUID = owner.getUUID();
    }    @Override
    public boolean isNoGravity() {
        return true;
    }    @Override
    public void tick() {
        if (this.level().isClientSide) {            if (this.tickCount % 2 == 0) {
                for (int i = 0; i < 360; i += 5) {
                    double angle = Math.toRadians(i);
                    double x = this.getX() + ARENA_RADIUS * Math.cos(angle);
                    double z = this.getZ() + ARENA_RADIUS * Math.sin(angle);                    BlockPos groundPos = new BlockPos((int) x, (int) this.getY(), (int) z);
                    while (this.level().isEmptyBlock(groundPos) && groundPos.getY() > this.level().getMinBuildHeight()) {
                        groundPos = groundPos.below();
                    }                    this.level().addParticle(ParticleTypes.PORTAL, groundPos.getX() + 0.5, groundPos.getY() + 0.5, groundPos.getZ() + 0.5,
                            this.random.nextDouble() - 0.5, 0.1, this.random.nextDouble() - 0.5);
                }
            }
        } else {            if (owner == null || !owner.isAlive()) {
                this.discard();
                return;
            }            if (this.orbCooldown-- <= 0) {
                spawnOrbFromBoundary();
                this.orbCooldown = 60 + this.random.nextInt(40);
            }            List<Player> players = this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(ARENA_RADIUS + 20.0));
            for (Player player : players) {
                if (player.isCreative() || player.isSpectator()) continue;                double distanceSqFromCenter = player.distanceToSqr(this.position());                if (distanceSqFromCenter > ARENA_RADIUS * ARENA_RADIUS) {
                    player.addEffect(new MobEffectInstance(ModEffects.INFERNAL_FLAMES.get(), 40, 0));
                    double distanceOutside = Math.sqrt(distanceSqFromCenter) - ARENA_RADIUS;
                    if (distanceOutside > 10.0) {
                        player.addEffect(new MobEffectInstance(ModEffects.INFERNAL_FLAMES.get(), 40, 1));
                    }
                    if (this.tickCount % 20 == 0) {
                        spawnOrbFromBoundaryTo(player);
                    }                }
            }
        }
    }    private void spawnOrbFromBoundary() {
        if (owner == null || owner.getTarget() == null) return;
        spawnOrbFromBoundaryTo(owner.getTarget());
    }    private void spawnOrbFromBoundaryTo(LivingEntity target) {
        double angle = this.random.nextDouble() * 2.0 * Math.PI;
        double x = this.getX() + ARENA_RADIUS * Math.cos(angle);
        double z = this.getZ() + ARENA_RADIUS * Math.sin(angle);
        Vec3 spawnPos = new Vec3(x, target.getY() + 2.0, z);        LightOrbEntity orb = new LightOrbEntity(this.level(), owner, target);
        orb.setPos(spawnPos);
        this.level().addFreshEntity(orb);
    }    @Override protected void defineSynchedData() {}
    @Override protected void readAdditionalSaveData(CompoundTag pCompound) {}
    @Override protected void addAdditionalSaveData(CompoundTag pCompound) {}
    @Override public Packet<ClientGamePacketListener> getAddEntityPacket() { return NetworkHooks.getEntitySpawningPacket(this); }
}