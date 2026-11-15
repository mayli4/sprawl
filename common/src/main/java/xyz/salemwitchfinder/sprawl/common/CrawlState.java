package xyz.salemwitchfinder.sprawl.common;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class CrawlState {
    private static final Set<UUID> CRAWLING = ConcurrentHashMap.newKeySet();

    private CrawlState() {}

    public static boolean isCrawling(UUID id) {
        return CRAWLING.contains(id);
    }

    public static void setCrawling(UUID id, boolean crawling) {
        if (crawling) {
            CRAWLING.add(id);
        } else {
            CRAWLING.remove(id);
        }
    }

    public static void clear(UUID id) {
        CRAWLING.remove(id);
    }
}
