package com.Maxwell.eschatology.client;import com.Maxwell.eschatology.Boss.BlackBool.BlackBoolModel;
import com.Maxwell.eschatology.Boss.BlackBool.BlackBoolRenderer;
import com.Maxwell.eschatology.Boss.BlackBool.Entities.EmptyRenderer;
import com.Maxwell.eschatology.Boss.BlackBool.Entities.EndLaser.EndLaserBeamRenderer;
import com.Maxwell.eschatology.Boss.BlackBool.Entities.EventHorizon.EventHorizonControllerRenderer;
import com.Maxwell.eschatology.Boss.BlackBool.Entities.GravityOrb.GravityOrbModel;
import com.Maxwell.eschatology.Boss.BlackBool.Entities.GravityOrb.GravityOrbRenderer;
import com.Maxwell.eschatology.Boss.BlackBool.Entities.LightOrb.LightOrbModel;
import com.Maxwell.eschatology.Boss.BlackBool.Entities.LightOrb.LightOrbRenderer;
import com.Maxwell.eschatology.Boss.BlackBool.Entities.Singularity.SingularityRenderer;
import com.Maxwell.eschatology.Boss.BlackBool.Entities.VoidLance.VoidLanceModel;
import com.Maxwell.eschatology.Boss.BlackBool.Entities.VoidLance.VoidLanceRenderer;
import com.Maxwell.eschatology.Boss.BlackBool.Entities.VoidLance.VoidRift.VoidRiftRenderer;
import com.Maxwell.eschatology.Boss.BlackBool.Entities.VoidWave.VoidWaveModel;
import com.Maxwell.eschatology.Boss.BlackBool.Entities.VoidWave.VoidWaveRenderer;
import com.Maxwell.eschatology.Boss.XF07_Revanant.Entities.ExoMisslie.ExoMissileModel;
import com.Maxwell.eschatology.Boss.XF07_Revanant.Entities.ExoMisslie.ExoMissileRenderer;
import com.Maxwell.eschatology.Boss.XF07_Revanant.Entities.ExoSkull.ExoSkullModel;
import com.Maxwell.eschatology.Boss.XF07_Revanant.Entities.ExoSkull.ExoSkullRenderer;
import com.Maxwell.eschatology.Boss.XF07_Revanant.Entities.FrostLaser.FrostLaserRenderer;
import com.Maxwell.eschatology.Boss.XF07_Revanant.Entities.SmallBeam.SmallBeamRenderer;
import com.Maxwell.eschatology.Boss.XF07_Revanant.ExoWitherModel;
import com.Maxwell.eschatology.Boss.XF07_Revanant.ExoWitherRenderer;
import com.Maxwell.eschatology.Eschatology;
import com.Maxwell.eschatology.client.GUI.Glitch.GlitchyGuiManager;
import com.Maxwell.eschatology.common.Items.Blocks.Eclipse_Forge.Client.EFBlockEntityRenderer;
import com.Maxwell.eschatology.common.Items.Blocks.Eclipse_Forge.Client.EFScreen;
import com.Maxwell.eschatology.common.Items.Blocks.Eclipse_Forge.Client.Eclipse_Forge_Model;
import com.Maxwell.eschatology.common.Items.WitchsCrest.Entity.WitchsSornModel;
import com.Maxwell.eschatology.common.Items.WitchsCrest.Entity.WitchsSornRenderer;
import com.Maxwell.eschatology.register.ModBlockEntities;
import com.Maxwell.eschatology.register.ModBlocks;
import com.Maxwell.eschatology.register.ModEntities;
import com.Maxwell.eschatology.register.ModMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
@SuppressWarnings("removal")
@Mod.EventBusSubscriber(modid = Eschatology.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientEvents {    @SubscribeEvent
    public static void onRegisterRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.BLACK_BOOL.get(), BlackBoolRenderer::new);
        event.registerEntityRenderer(ModEntities.VOID_WAVE.get(), VoidWaveRenderer::new);
        event.registerEntityRenderer(ModEntities.GRAVITY_ORB.get(), GravityOrbRenderer::new);
        event.registerEntityRenderer(ModEntities.LIGHT_ORB.get(), LightOrbRenderer::new);
        event.registerEntityRenderer(ModEntities.VOID_LANCE.get(), VoidLanceRenderer::new);
        event.registerEntityRenderer(ModEntities.VOID_RIFT.get(), VoidRiftRenderer::new);
        event.registerEntityRenderer(ModEntities.SINGULARITY.get(), SingularityRenderer::new);
        event.registerEntityRenderer(ModEntities.END_LASER_BEAM.get(), EndLaserBeamRenderer::new);
        event.registerEntityRenderer(ModEntities.END_LASER_BEAM_DAMAGE_FILED.get(), EmptyRenderer::new);
        event.registerEntityRenderer(ModEntities.EVENT_HORIZON_CONTROLLER.get(), EventHorizonControllerRenderer::new);
        event.registerEntityRenderer(ModEntities.EXO_WITHER.get(), ExoWitherRenderer::new);
        event.registerEntityRenderer(ModEntities.SMALL_BEAM.get(), SmallBeamRenderer::new);
        event.registerEntityRenderer(ModEntities.FROST_LASER.get(), FrostLaserRenderer::new);
        event.registerEntityRenderer(ModEntities.EXO_MISSILE.get(), ExoMissileRenderer::new);
        event.registerEntityRenderer(ModEntities.FROST_FIELD.get(), EmptyRenderer::new);
        event.registerEntityRenderer(ModEntities.EXO_SKULL.get(), ExoSkullRenderer::new);
        event.registerEntityRenderer(ModEntities.WITCHS_SORN.get(), WitchsSornRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.ECLIPSE_FORGE.get(), EFBlockEntityRenderer::new);
        event.registerEntityRenderer(ModEntities.PENETRATING_BULLET.get(), EmptyRenderer::new);
        event.registerEntityRenderer(ModEntities.FELL_BULLET.get(), EmptyRenderer::new);
        event.registerEntityRenderer(ModEntities.FELL_SHARD.get(), EmptyRenderer::new);
    }
    @SubscribeEvent
    public static void onRegisterLayerDefinitions(final EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BlackBoolModel.LAYER_LOCATION, BlackBoolModel::createBodyLayer);
        event.registerLayerDefinition(VoidWaveModel.LAYER_LOCATION, VoidWaveModel::createBodyLayer);
        event.registerLayerDefinition(GravityOrbModel.LAYER_LOCATION, GravityOrbModel::createBodyLayer);
        event.registerLayerDefinition(LightOrbModel.LAYER_LOCATION, LightOrbModel::createBodyLayer);
        event.registerLayerDefinition(VoidLanceModel.LAYER_LOCATION, VoidLanceModel::createBodyLayer);
        event.registerLayerDefinition(ExoWitherModel.LAYER_LOCATION, ExoWitherModel::createBodyLayer);
        event.registerLayerDefinition(ExoMissileModel.LAYER_LOCATION, ExoMissileModel::createBodyLayer);
        event.registerLayerDefinition(ExoSkullModel.LAYER_LOCATION, ExoSkullModel::createBodyLayer);
        event.registerLayerDefinition(WitchsSornModel.LAYER_LOCATION, WitchsSornModel::createBodyLayer);
        event.registerLayerDefinition(Eclipse_Forge_Model.LAYER_LOCATION, Eclipse_Forge_Model::createBodyLayer);
    }
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new GlitchyGuiManager());
        event.enqueueWork(() -> {
            MenuScreens.register(ModMenuTypes.ECLIPSE_FORGE.get(), EFScreen::new);
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.ECLIPSE_FORGE.get(), RenderType.cutout());
        });
    }
}