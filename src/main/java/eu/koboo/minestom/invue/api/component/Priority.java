package eu.koboo.minestom.invue.api.component;

public enum Priority implements Comparable<Priority> {

    LOW, // Gets executed first
    LOWEST,
    MEDIUM,
    HIGH,
    HIGHEST,
    MONITOR; // Gets executed last
}
