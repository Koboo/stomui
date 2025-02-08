package eu.koboo.minestom.examples;

import net.minestom.server.MinecraftServer;
import net.minestom.server.extras.MojangAuth;

public class Launcher {

    public static void main(String[] args) {
        MinecraftServer minecraftServer = MinecraftServer.init();
        MojangAuth.init();
        minecraftServer.start("127.0.0.1", 25565);
    }
}
