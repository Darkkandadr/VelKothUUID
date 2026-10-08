package dev.kitteh.factions;

/**
 * Test stub mirroring the subset of the FactionsUUID 4.x FPlayer API used by the team hook.
 */
public final class FPlayer {
    private final Faction faction;

    public FPlayer(Faction faction) {
        this.faction = faction;
    }

    public Faction faction() {
        return faction;
    }
}
