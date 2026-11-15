package xyz.salemwitchfinder.sprawl.common;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public final class SprawlCommonKeys {
    public static final String CATEGORY = "key.categories.sprawlmod";
    public static final String KEY_SIT = "key.sprawlmod.sit";
    public static final String KEY_CRAWL = "key.sprawlmod.crawl";

    public static final KeyMapping SIT_KEY = new KeyMapping(
            KEY_SIT,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_Z,
            CATEGORY
    );

    public static final KeyMapping CRAWL_KEY = new KeyMapping(
            KEY_CRAWL,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_C,
            CATEGORY
    );

    private SprawlCommonKeys() { }
}
