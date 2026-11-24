package xyz.salemwitchfinder.sprawl;

import xyz.salemwitchfinder.sprawl.common.config.SprawlConfig;

public class CommonClass {
    public static void init() {
        Constants.LOG.info("{} init success", Constants.MOD_NAME);
        SprawlConfig.register();
    }
}