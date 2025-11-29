package com.Maxwell.eschatology.Config;import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;public class ClientConfig {
    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final ClientConfig CLIENT;    public final ForgeConfigSpec.IntValue exoHeartXOffset;
    public final ForgeConfigSpec.IntValue exoHeartYOffset;
    public final ForgeConfigSpec.IntValue exoHeartTextOffset;
    public final ForgeConfigSpec.BooleanValue showExoHeartHud;
    public final ForgeConfigSpec.BooleanValue showRevengeGauge;
    public final ForgeConfigSpec.IntValue revengeGaugeX;       
    public final ForgeConfigSpec.IntValue revengeGaugeYOffset; 
    public final ForgeConfigSpec.IntValue revengeGaugeWidth;   
    public final ForgeConfigSpec.IntValue revengeGaugeHeight;      static {
        final Pair<ClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
        CLIENT_SPEC = specPair.getRight();
        CLIENT = specPair.getLeft();
    }    public ClientConfig(ForgeConfigSpec.Builder builder) {
        builder.push("ExoHeart HUD Settings");        showExoHeartHud = builder
                .comment("Toggle the visibility of the ExoHeart HUD")
                .define("showExoHeartHud", true);        exoHeartXOffset = builder
                .comment("X Offset from the center of the screen. Default: -91")
                .defineInRange("exoHeartXOffset", -91, -1000, 1000);        exoHeartYOffset = builder
                .comment("Distance from the bottom of the screen. Default: 50")
                .defineInRange("exoHeartYOffset", 50, 0, 1000);        exoHeartTextOffset = builder
                .comment("Y Offset for the text above the bar. Default: 10")
                .defineInRange("exoHeartTextOffset", 10, -50, 50);        builder.pop();
        builder.push("Revenge Gauge Settings");        showRevengeGauge = builder
                .comment("Toggle the visibility of the Revenge Gauge")
                .define("showRevengeGauge", true);        revengeGaugeX = builder
                .comment("X Position from the left side of the screen. Default: 20")
                .defineInRange("revengeGaugeX", 20, 0, 1000);        revengeGaugeYOffset = builder
                .comment("Y Offset from the vertical center of the screen. Negative is up, positive is down. Default: -50")
                .defineInRange("revengeGaugeYOffset", -50, -1000, 1000);        revengeGaugeWidth = builder
                .comment("Width of the gauge. Default: 10")
                .defineInRange("revengeGaugeWidth", 10, 1, 200);        revengeGaugeHeight = builder
                .comment("Height of the gauge. Default: 100")
                .defineInRange("revengeGaugeHeight", 100, 10, 500);        builder.pop();
    }
}