package eu.koboo.minestom.examples.stomui.command;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import eu.koboo.minestom.examples.stomui.views.multiview.TwoPlayerExampleProvider;
import eu.koboo.minestom.stomui.api.ViewRegistry;
import net.minestom.server.entity.Player;

@Command(name = "multiview")
public final class CommandComponentForTwoPlayer {

    TwoPlayerExampleProvider multiViewComponent;

    public CommandComponentForTwoPlayer(ViewRegistry registry) {
        // Creates the ViewProvider instance once on server start,
        // because litecommands we only call the constructor once.
        // Every player opens the same ViewProvider instance.
        multiViewComponent = new TwoPlayerExampleProvider(registry);
    }

    @Execute
    public void onExecute(@Context Player player) {
        // Opening the same ViewProvider instance for each player
        // opens different Inventories and different PlayerViews for each player,
        // but at the end the same ViewComponent instances handle the logic and render their items.
        // So all players have a different minestom inventory,
        // but stomui components are shared between all players.
        multiViewComponent.open(player);
    }

}