package dev.kitteh.factions;

import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Test stub mirroring the subset of the FactionsUUID 4.x FPlayers API used by the team hook.
 */
public final class FPlayers {
    private static final FPlayers INSTANCE = new FPlayers();
    private final Map<UUID, FPlayer> players = new HashMap<>();

    public static FPlayers fPlayers() {
        return INSTANCE;
    }

    public FPlayer get(OfflinePlayer player) {
        return players.get(player.getUniqueId());
    }

    public void put(UUID id, Faction faction) {
        players.put(id, new FPlayer(faction));
    }

    public void clear() {
        players.clear();
    }
}
