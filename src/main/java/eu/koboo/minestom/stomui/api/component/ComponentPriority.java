package eu.koboo.minestom.stomui.api.component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to define the render/execution priority of a {@link ViewComponent}
 * within its layer of siblings in the component tree / hierarchy.
 * See {@link Priority} for the order.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ComponentPriority {

    /**
     * @return the specific {@link Priority} for this {@link ViewComponent}
     */
    Priority value();
}
