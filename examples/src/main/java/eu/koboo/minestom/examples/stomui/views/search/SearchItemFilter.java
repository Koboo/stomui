package eu.koboo.minestom.examples.stomui.views.search;

import eu.koboo.minestom.stomui.api.pagination.ItemFilter;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

public class SearchItemFilter implements ItemFilter<Material> {

    private String textInput;

    public void setTextInput(String textInput) {
        this.textInput = textInput;
    }

    @Override
    public boolean include(@NotNull Material item) {
        // Only blocks please.  Why only blocks?
        // To show filtering based on categories or other parameters
        if (item == Material.AIR || !item.isBlock()) {
            return false;
        }
        // No text input → display all items.
        if (textInput == null || textInput.isEmpty()) {
            return true;
        }
        // We got an input, so we check materials names.
        if (!item.name().toLowerCase().contains(textInput.toLowerCase())) {
            return false;
        }
        // Got that in your name material? Good for you, have fun paginated!
        return true;
    }
}
