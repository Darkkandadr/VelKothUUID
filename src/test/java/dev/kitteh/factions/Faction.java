package dev.kitteh.factions;

/**
 * Test stub mirroring the subset of the FactionsUUID 4.x Faction API used by the team hook.
 */
public final class Faction {
    private final String tag;
    private final boolean normal;

    public Faction(String tag, boolean normal) {
        this.tag = tag;
        this.normal = normal;
    }

    public String tag() {
        return tag;
    }

    public boolean isNormal() {
        return normal;
    }
}
