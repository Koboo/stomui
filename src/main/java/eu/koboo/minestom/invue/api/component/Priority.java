package eu.koboo.minestom.invue.api.component;

/**
 * This enum is used to sort the {@link ViewComponent} order. The order
 * of all {@link ViewComponent} in one layer is used to define who renders first.
 */
public enum Priority implements Comparable<Priority> {

    // Gets executed first
    LOW,
    LOWEST,
    MEDIUM,
    HIGH,
    HIGHEST,
    // Gets executed last
    MONITOR
}
