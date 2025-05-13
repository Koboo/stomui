package eu.koboo.minestom.stomui.api.annotations;

import eu.koboo.minestom.stomui.api.annotations.components.AnnotationRenderComponent;
import eu.koboo.minestom.stomui.api.item.PrebuiltItem;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation can be used to define a specific inventory slot
 * to a {@link PrebuiltItem} returned by the annotated method.
 * <p>
 * To enable the rendering of annotated methods, you need to add
 * the {@link AnnotationRenderComponent} component to a component in the hierarchy / component tree.
 * Example:
 * <pre>
 * {@code
 *      public MyComponent(ViewRegistry registry) {
 *          super(registry);
 *          addChild(new AnnotationRenderComponent(registry));
 *      }
 * }
 * </pre>
 * <p>
 * Required method signature is:
 * <pre>
 * {@code
 *      @Slot(0) // first slot in inventory
 *      public PrebuiltItem exampleMethodName() {
 *          //return PrebuiltItem...
 *      }
 * }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Slot {

    /**
     * @return the specific slot as rawSlot. Can be used to set bottom items, too.
     */
    int value();
}
