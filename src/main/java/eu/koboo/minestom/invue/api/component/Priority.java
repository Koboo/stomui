package eu.koboo.minestom.invue.api.component;

/**
 * This enum is used to sort the order of children within a {@link ViewComponent}.
 * The order of all {@link ViewComponent} in one direct layer of the component tree
 * is used to define who gets executed/rendered first by the respective callers/listeners.
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
