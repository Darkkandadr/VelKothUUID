package dev.velmax.velkoth.team;

import dev.kitteh.factions.FPlayers;
import dev.kitteh.factions.Faction;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FactionsUUIDHookTest {

    private final Faction red = new Faction("Red", true);
    private final Faction blue = new Faction("Blue", true);
    private final Faction wilderness = new Faction("Wilderness", false);

    @AfterEach
    void clearPlayers() {
        FPlayers.fPlayers().clear();
    }

    @Test
    void shouldTreatMembersOfSameFactionAsSameTeam() throws Exception {
        FactionsUUIDHook hook = new FactionsUUIDHook();
        Player a = playerIn(red);
        Player b = playerIn(red);

        assertTrue(hook.isSameTeam(a, b));
        assertEquals("Red", hook.getTeamName(a));
    }

    @Test
    void shouldNotTreatDifferentFactionsAsSameTeam() throws Exception {
        FactionsUUIDHook hook = new FactionsUUIDHook();

        assertFalse(hook.isSameTeam(playerIn(red), playerIn(blue)));
    }

    @Test
    void shouldNotTreatFactionlessPlayersAsSameTeam() throws Exception {
        FactionsUUIDHook hook = new FactionsUUIDHook();
        Player a = playerIn(wilderness);
        Player b = playerIn(wilderness);

        assertFalse(hook.isSameTeam(a, b));
        assertNull(hook.getTeamName(a));
    }

    @Test
    void shouldReturnNoTeamForUnknownPlayer() throws Exception {
        FactionsUUIDHook hook = new FactionsUUIDHook();
        Player unknown = player(UUID.randomUUID());

        assertNull(hook.getTeamName(unknown));
        assertFalse(hook.isSameTeam(unknown, playerIn(red)));
    }

    private Player playerIn(Faction faction) {
        UUID id = UUID.randomUUID();
        FPlayers.fPlayers().put(id, faction);
        return player(id);
    }

    private static Player player(UUID id) {
        return (Player) Proxy.newProxyInstance(Player.class.getClassLoader(), new Class<?>[]{Player.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "getUniqueId" -> id;
                    case "equals" -> proxy == args[0];
                    case "hashCode" -> System.identityHashCode(proxy);
                    default -> null;
                });
    }
}
