package xyz.salemwitchfinder.sprawl.common.client;

import xyz.salemwitchfinder.sprawl.common.config.SprawlConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class SprawlConfigScreen {

    private SprawlConfigScreen() {

    }

    public static Screen build(Screen parent) {
        SprawlConfig config = SprawlConfig.get();
        SprawlConfig defaults = new SprawlConfig();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("title.sprawlmod.config"))
                .setSavingRunnable(SprawlConfig::save);

        ConfigEntryBuilder entries = builder.entryBuilder();

        ConfigCategory general = builder.getOrCreateCategory(Component.translatable("category.sprawlmod.general"));
        general.addEntry(entries.startBooleanToggle(Component.translatable("option.sprawlmod.sitToggle"), config.sitToggle)
                .setDefaultValue(defaults.sitToggle)
                .setTooltip(Component.translatable("tooltip.sprawlmod.sitToggle"))
                .setSaveConsumer(value -> config.sitToggle = value)
                .build());

        general.addEntry(entries.startBooleanToggle(Component.translatable("option.sprawlmod.crawlToggle"), config.crawlToggle)
                .setDefaultValue(defaults.crawlToggle)
                .setTooltip(Component.translatable("tooltip.sprawlmod.crawlToggle"))
                .setSaveConsumer(value -> config.crawlToggle = value)
                .build());

        return builder.build();
    }
}
