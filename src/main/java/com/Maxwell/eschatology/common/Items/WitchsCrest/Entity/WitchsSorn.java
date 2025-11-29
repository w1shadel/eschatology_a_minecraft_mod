package com.Maxwell.eschatology.common.Items.WitchsCrest.Entity;import com.Maxwell.eschatology.register.ModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;import javax.annotation.Nullable;
import java.util.UUID;public class WitchsSorn extends Entity {    @Nullable
    private LivingEntity owner;
    @Nullable
    private UUID ownerUUID;    private int lifeTime = 20;     public WitchsSorn(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }    public WitchsSorn(Level pLevel, double pX, double pY, double pZ, @Nullable LivingEntity pOwner) {
        super(ModEntities.WITCHS_SORN.get(), pLevel);
        this.setOwner(pOwner);
        this.setPos(pX, pY, pZ);
    }    public void setOwner(@Nullable LivingEntity pOwner) {
        this.owner = pOwner;
        this.ownerUUID = pOwner == null ? null : pOwner.getUUID();
    }    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null && this.level() instanceof ServerLevel) {
            Entity entity = ((ServerLevel)this.level()).getEntity(this.ownerUUID);
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity)entity;
            }
        }
        return this.owner;
    }    @Override
    public void tick() {
        super.tick();        if (this.lifeTime > 0) {
            this.lifeTime--;
        } else {
            this.discard();
        }        if (!this.level().isClientSide()) {            for(LivingEntity livingentity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox())) {                if (livingentity != this.getOwner() && livingentity.isAlive()) {                    DamageSource source = this.level().damageSources().indirectMagic(this, this.getOwner());                    livingentity.hurt(source, 6.0F);
                }
            }
        }
    }    @Override
    protected void defineSynchedData() { }    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        if (pCompound.hasUUID("Owner")) {
            this.ownerUUID = pCompound.getUUID("Owner");
        }
    }    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        if (this.ownerUUID != null) {
            pCompound.putUUID("Owner", this.ownerUUID);
        }
    }    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
