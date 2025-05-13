package eu.koboo.minestom.stomui.api.annotations;

import eu.koboo.minestom.stomui.api.PlayerView;
import eu.koboo.minestom.stomui.api.item.PrebuiltItem;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Can be annotated on methods to mark their respective {@link PrebuiltItem}
 * stateful. That means the annotated methods gets executed,
 * everytime the {@link PlayerView#updateState()} is called.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Stateful {
}
