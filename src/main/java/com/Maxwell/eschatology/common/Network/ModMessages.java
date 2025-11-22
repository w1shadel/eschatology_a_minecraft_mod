package com.Maxwell.eschatology.common.Network;import com.Maxwell.eschatology.Eschatology;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("removal")
public class ModMessages {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Eschatology.MODID, "messages"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    
    public static void register() {

        addServerboundMessage(AnimationEndMessage.class, AnimationEndMessage::new);
        addServerboundMessage(RequestGlitchStrengthPacket.class, RequestGlitchStrengthPacket::new);
        addServerboundMessage(RequestRevengeGaugeIncreasePacket.class, RequestRevengeGaugeIncreasePacket::new);
        addServerboundMessage(StartWorkbenchCraftPacket.class, StartWorkbenchCraftPacket::new);
        addServerboundMessage(SyncExoHeartActivatePacket.class, SyncExoHeartActivatePacket::new);

        addClientboundMessage(PlayBossMusicPacket.class, PlayBossMusicPacket::new);
        addClientboundMessage(SpawnCounterParticlesPacket.class, SpawnCounterParticlesPacket::new);
        addClientboundMessage(StartAnimationPacket.class, StartAnimationPacket::new);
        addClientboundMessage(SyncBossBarPacket.class, SyncBossBarPacket::new);
        addClientboundMessage(SyncEclipseStatePacket.class, SyncEclipseStatePacket::new);
        addClientboundMessage(SyncExoHeartDataPacket.class, SyncExoHeartDataPacket::new);
        addClientboundMessage(SyncGlitchEffectPacket.class, SyncGlitchEffectPacket::new);
        addClientboundMessage(SyncLaserEffectsPacket.class, SyncLaserEffectsPacket::new);
        addClientboundMessage(SyncRevengeGaugePacket.class, SyncRevengeGaugePacket::new);
        addClientboundMessage(SyncRLAbilityGuiPacket.class, SyncRLAbilityGuiPacket::new);
        addClientboundMessage(SyncShatterScreenPacket.class, SyncShatterScreenPacket::new);
        addClientboundMessage(SyncWCPlayerATKLevelPacket.class, SyncWCPlayerATKLevelPacket::new);
        addClientboundMessage(SyncWorkbenchDataPacket.class, SyncWorkbenchDataPacket::new);
    }
    private static <MSG extends IServerboundPacket> void handleServerboundPacket(MSG message, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        ServerPlayer sender = context.getSender();
        if (sender != null) {
            message.handle(sender, context);
        }
    }
    private static <MSG extends IClientboundPacket> void handleClientboundPacket(MSG message, Supplier<NetworkEvent.Context> ctx) {
        message.handle(ctx.get());
    }
    private static <MSG extends IServerboundPacket> void addServerboundMessage(Class<MSG> clazz, Function<FriendlyByteBuf, MSG> decoder) {
        INSTANCE.messageBuilder(clazz, id(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(IServerboundPacket::encode)
                .decoder(decoder)
                .consumerMainThread(ModMessages::handleServerboundPacket)
                .add();
    }
    private static <MSG extends IClientboundPacket> void addClientboundMessage(Class<MSG> clazz, Function<FriendlyByteBuf, MSG> decoder) {
        INSTANCE.messageBuilder(clazz, id(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(IClientboundPacket::encode)
                .decoder(decoder)
                .consumerMainThread(ModMessages::handleClientboundPacket)
                .add();
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToAll(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
    public static <MSG> void sendToClientsAround(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), message);
    }
}