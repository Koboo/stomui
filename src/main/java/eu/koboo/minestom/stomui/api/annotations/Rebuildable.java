package eu.koboo.minestom.stomui.api.annotations;

import eu.koboo.minestom.stomui.api.PlayerView;
import eu.koboo.minestom.stomui.api.item.PrebuiltItem;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Can be annotated on methods to mark their created {@link PrebuiltItem} rebuildable.
 * That means the annotated method gets executed, everytime the method
 * {@link PlayerView#executeRebuild()} is called.
 * This results in a rebuilt of the {@link PrebuiltItem} returned by the annotated method.
 * So you could use stateful/rebuildable variables in the components in the {@link PrebuiltItem}, which
 * you build in the method.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Rebuildable {
}
