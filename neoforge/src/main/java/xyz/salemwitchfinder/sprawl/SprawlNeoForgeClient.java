package xyz.salemwitchfinder.sprawl;

import xyz.salemwitchfinder.sprawl.common.client.SprawlConfigScreen;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

public final class SprawlNeoForgeClient {

    private SprawlNeoForgeClient() {
    }

    public static void init() {
        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> (minecraft, parent) -> SprawlConfigScreen.build(parent));
    }
}
