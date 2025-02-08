package eu.koboo.minestom.invue.api.interaction;

import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface CooldownInteraction {

    void onSlotCooldown(@NotNull Player player, int slot);
}
