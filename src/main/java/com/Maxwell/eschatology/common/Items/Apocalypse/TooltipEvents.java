package com.Maxwell.eschatology.common.Items.Apocalypse;import com.Maxwell.eschatology.Eschatology;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;import java.awt.*;
import java.util.ListIterator;@Mod.EventBusSubscriber(modid = Eschatology.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class TooltipEvents {    @SubscribeEvent
    public static void onTooltipModify(ItemTooltipEvent event) {
        if (!(event.getItemStack().getItem() instanceof Apocalypse)) {
            return;
        }
        ListIterator<Component> iterator = event.getToolTip().listIterator();
        final String attackDamageTranslated = Component.translatable("attribute.name.generic.attack_damage").getString();
        while (iterator.hasNext()) {
            Component component = iterator.next();
            String lineText = component.getString();
            if (lineText.contains(attackDamageTranslated)) {
                String fullText = "??? " + attackDamageTranslated;
                Color[] colors = {Color.YELLOW, Color.BLACK, Color.RED};
                Component animatedGradientText = createAnimatedGradientComponent(fullText, colors);
                iterator.set(Component.literal(" ").append(animatedGradientText));
            }
        }
    }
    public static MutableComponent createAnimatedGradientComponent(String text, Color... colors) {
        MutableComponent finalComponent = Component.literal("");
        int n = text.length();
        long animationDuration = 20000L;
        double timeOffset = (double) (System.currentTimeMillis() % animationDuration) / animationDuration;
        if (n <= 1 || colors.length < 2) {
            Color color = (colors.length > 0) ? colors[0] : Color.WHITE;
            return Component.literal(text).withStyle(style -> style.withColor(color.getRGB()));
        }
        int numSegments = colors.length - 1;
        for (int i = 0; i < n; i++) {
            double baseProgress = (double) i / (n - 1);
            double effectiveProgress = (baseProgress + timeOffset) % 1.0;
            int segmentIndex = (int) Math.min(effectiveProgress * numSegments, numSegments - 1);
            Color startColor = colors[segmentIndex];
            Color endColor = colors[segmentIndex + 1];
            double segmentProgress = (effectiveProgress - (double) segmentIndex / numSegments) * numSegments;
            int r = (int) (startColor.getRed() + (endColor.getRed() - startColor.getRed()) * segmentProgress);
            int g = (int) (startColor.getGreen() + (endColor.getGreen() - startColor.getGreen()) * segmentProgress);
            int b = (int) (startColor.getBlue() + (endColor.getBlue() - startColor.getBlue()) * segmentProgress);
            int finalRgb = (r << 16) | (g << 8) | b;
            MutableComponent charComponent = Component.literal(String.valueOf(text.charAt(i)))
                    .withStyle(style -> style.withColor(finalRgb));
            finalComponent.append(charComponent);
        }
        return finalComponent;
    }
}