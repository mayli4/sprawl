package xyz.salemwitchfinder.sprawl.common.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

@Config(name = "sprawlmod")
public class SprawlConfig implements ConfigData {
    public boolean sitToggle = true;
    public boolean crawlToggle = true;

    public static void register() {
        AutoConfig.register(SprawlConfig.class, GsonConfigSerializer::new);
    }

    public static SprawlConfig get() {
        return AutoConfig.getConfigHolder(SprawlConfig.class).getConfig();
    }

    public static void save() {
        AutoConfig.getConfigHolder(SprawlConfig.class).save();
    }
}
