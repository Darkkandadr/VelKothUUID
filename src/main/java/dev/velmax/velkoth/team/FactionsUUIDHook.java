package dev.velmax.velkoth.team;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

/**
 * Hook for FactionsUUID 4.x (package {@code dev.kitteh.factions}, plugin name {@code FactionsUUID}).
 * <p>
 * Only "normal" factions count as teams. Wilderness, SafeZone and WarZone are pseudo-factions shared by
 * every factionless player, so treating them as a team would make unrelated players team up on the hill.
 * <p>
 * Reflection keeps FactionsUUID an optional, non-shaded runtime dependency; the plugin metadata joins its
 * classpath so the classes are visible to this plugin's class loader.
 */
public class FactionsUUIDHook implements TeamHook {

    private final Object fplayers;
    private final Method getByPlayer;
    private final Method faction;
    private final Method tag;
    private final Method isNormal;

    /**
     * @throws ReflectiveOperationException if the installed FactionsUUID does not expose the 4.x API
     */
    public FactionsUUIDHook() throws ReflectiveOperationException {
        Class<?> fplayersClass = Class.forName("dev.kitteh.factions.FPlayers");
        this.fplayers = fplayersClass.getMethod("fPlayers").invoke(null);
        this.getByPlayer = fplayersClass.getMethod("get", OfflinePlayer.class);

        Class<?> fplayerClass = Class.forName("dev.kitteh.factions.FPlayer");
        this.faction = fplayerClass.getMethod("faction");

        Class<?> factionClass = Class.forName("dev.kitteh.factions.Faction");
        this.tag = factionClass.getMethod("tag");
        this.isNormal = factionClass.getMethod("isNormal");
    }

    @Override
    public boolean isSameTeam(Player p1, Player p2) {
        Object f1 = normalFaction(p1);
        return f1 != null && f1.equals(normalFaction(p2));
    }

    @Override
    public @Nullable String getTeamName(Player player) {
        Object f = normalFaction(player);
        if (f == null)
            return null;
        try {
            return (String) tag.invoke(f);
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }

    /**
     * Returns the player's faction, or null when the player has no real (normal) faction.
     */
    private @Nullable Object normalFaction(Player player) {
        try {
            Object fplayer = getByPlayer.invoke(fplayers, player);
            if (fplayer == null)
                return null;
            Object f = faction.invoke(fplayer);
            if (f == null || !(boolean) isNormal.invoke(f))
                return null;
            return f;
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }
}
