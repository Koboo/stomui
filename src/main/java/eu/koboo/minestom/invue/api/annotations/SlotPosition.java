package eu.koboo.minestom.invue.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation has the same usages as the {@link Slot} annotation,
 * but uses row and column. Both values start at 0.
 * See {@link Slot} for more information.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SlotPosition {

    /**
     * @return the specific row, starting at 0
     */
    int row();

    /**
     * @return the specific column, starting at 0
     */
    int column();
}
