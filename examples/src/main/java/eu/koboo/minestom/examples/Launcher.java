package eu.koboo.minestom.examples;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.minestom.LiteMinestomFactory;
import eu.koboo.minestom.examples.invue.command.CommandView;
import eu.koboo.minestom.examples.invue.command.ExampleMultiViewCommand;
import eu.koboo.minestom.invue.api.ViewRegistry;
import eu.koboo.minestom.invue.core.MinestomInvue;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.extras.MojangAuth;

public class Launcher {

    public static void main(String[] args) {
        MinecraftServer minecraftServer = MinecraftServer.init();
        MojangAuth.init();
        minecraftServer.start("127.0.0.1", 25565);

        // Create a new instance of the ViewRegistry.
        ViewRegistry viewRegistry = MinestomInvue.create();

        // Register the listener of invue
        viewRegistry.enable();

        // If ur server shuts down, you can disable view registries by:
        //viewRegistry.disable();

        LiteCommands<CommandSender> liteCommands = LiteMinestomFactory.builder()
            .commands(
                new CommandView(viewRegistry),
                new ExampleMultiViewCommand(viewRegistry)
            )
            .settings(configurator ->
                configurator.permissionResolver((sender, permission) -> true)
            )
            .build();
        liteCommands.register();
    }
}
