package eu.koboo.minestom.examples.invue.command;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import eu.koboo.minestom.invue.api.ViewRegistry;
import eu.koboo.minestom.examples.invue.views.multiview.MultiViewExampleProvider;
import net.minestom.server.entity.Player;

@Command(name = "test")
public final class ExampleMultiViewCommand {

    MultiViewExampleProvider multiViewComponent;

    public ExampleMultiViewCommand(ViewRegistry registry) {
        multiViewComponent = new MultiViewExampleProvider(registry);
    }

    @Execute
    public void onExecute(@Context Player player) {
        // Opening the same instance of the ViewProvider
        // for every player, so they get different inventories
        // but at the end the same handlers on their items.
        multiViewComponent.open(player);
    }

}