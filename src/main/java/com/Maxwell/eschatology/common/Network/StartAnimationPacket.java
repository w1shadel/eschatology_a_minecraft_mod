package com.Maxwell.eschatology.common.Network;import com.Maxwell.eschatology.common.helper.AnimationPlayHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;import java.util.UUID;public class StartAnimationPacket implements IClientboundPacket {
    private final UUID playerUUID;
    private final String animationName;
    private final boolean shouldDamage;
    private final boolean lockMovement;    public StartAnimationPacket(UUID playerUUID, String animationName, boolean shouldDamage, boolean lockMovement) {
        this.playerUUID = playerUUID;
        this.animationName = animationName;
        this.shouldDamage = shouldDamage;
        this.lockMovement = lockMovement;
    }    public StartAnimationPacket(FriendlyByteBuf buf) {
        this.playerUUID = buf.readUUID();
        this.animationName = buf.readUtf();
        this.shouldDamage = buf.readBoolean();
        this.lockMovement = buf.readBoolean();
    }    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(this.playerUUID);
        buf.writeUtf(this.animationName);
        buf.writeBoolean(this.shouldDamage);
        buf.writeBoolean(this.lockMovement);
    }    @Override
    @OnlyIn(Dist.CLIENT)
    public void handle(NetworkEvent.Context context) {
        Level level = Minecraft.getInstance().level;
        if (level == null) return;        Player targetPlayer = level.getPlayerByUUID(this.playerUUID);
        if (targetPlayer != null) {
            AnimationPlayHelper.playAnimation(targetPlayer, this.animationName, this.shouldDamage, this.lockMovement);
        }
    }
}