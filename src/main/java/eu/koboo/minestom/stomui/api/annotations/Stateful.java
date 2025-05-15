package eu.koboo.minestom.stomui.api.annotations;

import eu.koboo.minestom.stomui.api.PlayerView;
import eu.koboo.minestom.stomui.api.item.PrebuiltItem;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Can be annotated on methods to mark their created {@link PrebuiltItem} rebuildable.
 * That means the annotated methods get executed,
 * everytime the {@link PlayerView#executeRebuild()} is called.
 * That would result in a rebuilt of the {@link PrebuiltItem} returned by the annotated method.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Stateful {
}
