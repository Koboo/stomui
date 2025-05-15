package eu.koboo.minestom.examples.stomui.utils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.minestom.server.item.Material;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum SettingsTab {

    PROFILE(Material.OAK_BOAT, "Profile"),
    CLAN(Material.ACACIA_BOAT, "Clan"),
    FRIEND(Material.GOLDEN_APPLE, "Friends");

    Material material;
    String name;
}
