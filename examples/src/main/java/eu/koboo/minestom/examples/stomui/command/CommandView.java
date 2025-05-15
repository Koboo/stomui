package eu.koboo.minestom.examples.stomui.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.execute.ExecuteDefault;
import eu.koboo.minestom.examples.stomui.views.CounterExampleProvider;
import eu.koboo.minestom.examples.stomui.views.SimpleExampleProvider;
import eu.koboo.minestom.examples.stomui.views.other.AllowInteractionExampleProvider;
import eu.koboo.minestom.examples.stomui.views.other.AnnotatedTabExampleProvider;
import eu.koboo.minestom.examples.stomui.views.other.AnvilInputExampleProvider;
import eu.koboo.minestom.examples.stomui.views.pagination.PageableExampleProvider;
import eu.koboo.minestom.examples.stomui.views.pagination.ScrollableHorizontalExampleProvider;
import eu.koboo.minestom.examples.stomui.views.pagination.ScrollableVerticalExampleProvider;
import eu.koboo.minestom.examples.stomui.views.search.SearchExampleProvider;
import eu.koboo.minestom.examples.stomui.views.switching.SwitchOneExampleProvider;
import eu.koboo.minestom.examples.stomui.views.switching.ViewHistoryExampleProvider;
import eu.koboo.minestom.stomui.api.ViewRegistry;
import eu.koboo.minestom.stomui.api.component.ViewProvider;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.minestom.server.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Command(name = "view")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class CommandView {

    private static final Map<String, Function<ViewRegistry, ViewProvider>> VIEWS = new HashMap<>() {{
        put("annotated", AnnotatedTabExampleProvider::new);
        put("page", PageableExampleProvider::new);
        put("scrollhorizontal", ScrollableHorizontalExampleProvider::new);
        put("scrollvertical", ScrollableVerticalExampleProvider::new);
        put("switch", SwitchOneExampleProvider::new);
        put("allow", AllowInteractionExampleProvider::new);
        put("anvil", AnvilInputExampleProvider::new);
        put("search", SearchExampleProvider::new);
        put("simple", SimpleExampleProvider::new);
        put("history", registry -> new ViewHistoryExampleProvider(registry, 1));
        put("counter", CounterExampleProvider::new);
    }};

    ViewRegistry registry;

    @ExecuteDefault
    public void onExecuteDefault(@Context Player player) {
        printUsage(player);
    }

    @Execute
    public void onExecute(@Context Player player, @Arg("ViewName") String viewName) {
        Function<ViewRegistry, ViewProvider> viewSupplier = VIEWS.get(viewName);
        if (viewSupplier == null) {
            printUsage(player);
            player.sendMessage("View with name " + viewName + " not found.");
            return;
        }

        ViewProvider viewProvider = viewSupplier.apply(registry);
        viewProvider.open(player);
    }

    private void printUsage(Player player) {
        player.sendMessage("Usage: /view <name>");
        player.sendMessage("Available views:");
        player.sendMessage(String.join(", ", VIEWS.keySet()));
    }

}