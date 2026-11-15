package xyz.salemwitchfinder.sprawl.sit;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class SitState {
    private static final Set<UUID> SITTING = ConcurrentHashMap.newKeySet();

    private SitState() { }

    public static boolean isSitting(UUID id) {
        return SITTING.contains(id);
    }

    public static void setSitting(UUID id, boolean sitting) {
        if (sitting) {
            SITTING.add(id);
        } else {
            SITTING.remove(id);
        }
    }

    public static void clear(UUID id) {
        SITTING.remove(id);
    }
}
