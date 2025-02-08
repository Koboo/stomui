package eu.koboo.minestom.invue.api.component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Can be used to define the render priority of a {@link ViewComponent} within its layer.
 * See {@link Priority} for more information.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ComponentPriority {

    /**
     * @return the specific {@link Priority} for this {@link ViewComponent}
     */
    Priority value();
}
