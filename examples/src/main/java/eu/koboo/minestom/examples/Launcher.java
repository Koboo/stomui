package eu.koboo.minestom.examples;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.minestom.LiteMinestomFactory;
import eu.koboo.minestom.examples.stomui.command.CommandComponentForTwoPlayer;
import eu.koboo.minestom.examples.stomui.command.CommandView;
import eu.koboo.minestom.stomui.api.ViewRegistry;
import eu.koboo.minestom.stomui.core.MinestomUI;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
public class Launcher {

    public static void main(String[] args) {
        MinecraftServer minecraftServer = MinecraftServer.init();
        MojangAuth.init();
        minecraftServer.start("127.0.0.1", 25565);
        log.info("Server-Version: " + MinecraftServer.VERSION_NAME);

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
        ViewRegistry viewRegistry = MinestomUI.create();

        // Register the listener of stomui
        viewRegistry.enable();

        // If your server shuts down, you can disable view registries by:
        //viewRegistry.disable();

        LiteCommands<CommandSender> liteCommands = LiteMinestomFactory.builder()
            .commands(
                new CommandView(viewRegistry),
                new CommandComponentForTwoPlayer(viewRegistry)
            )
            .build();
        liteCommands.register();
    }
}
