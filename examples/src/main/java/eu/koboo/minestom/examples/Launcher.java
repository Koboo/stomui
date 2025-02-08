package eu.koboo.minestom.examples;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.minestom.LiteMinestomFactory;
import eu.koboo.minestom.examples.invue.command.CommandMultiView;
import eu.koboo.minestom.examples.invue.command.CommandView;
import eu.koboo.minestom.invue.api.ViewRegistry;
import eu.koboo.minestom.invue.core.MinestomInvue;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.block.Block;

public class Launcher {

    public static void main(String[] args) {
        MinecraftServer minecraftServer = MinecraftServer.init();
        MojangAuth.init();
        minecraftServer.start("127.0.0.1", 25565);
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer defaultInstance = instanceManager.createInstanceContainer();
        defaultInstance.setGenerator(unit ->
            unit.modifier().fillHeight(0, 1, Block.GRASS_BLOCK)
        );
        defaultInstance.setChunkSupplier(LightingChunk::new);
        GlobalEventHandler eventHandler = MinecraftServer.getGlobalEventHandler();
        eventHandler.addListener(AsyncPlayerConfigurationEvent.class,
            event -> {
                event.setSpawningInstance(defaultInstance);
                event.getPlayer().setRespawnPoint(new Pos(0, 1, 0));
                event.getPlayer().setGameMode(GameMode.CREATIVE);
            });

        // Create a new instance of the ViewRegistry.
        ViewRegistry viewRegistry = MinestomInvue.create();

        // Register the listener of invue
        viewRegistry.enable();

        // If ur server shuts down, you can disable view registries by:
        //viewRegistry.disable();

        LiteCommands<CommandSender> liteCommands = LiteMinestomFactory.builder()
            .commands(
                new CommandView(viewRegistry),
                new CommandMultiView(viewRegistry)
            )
            .settings(configurator ->
                configurator.permissionResolver((sender, permission) -> true)
            )
            .build();
        liteCommands.register();
    }
}
