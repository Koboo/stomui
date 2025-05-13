package eu.koboo.minestom.stomui.core;

import eu.koboo.minestom.stomui.api.ViewRegistry;

public final class MinestomUI {

    public static ViewRegistry create() {
        return new CoreViewRegistry();
    }
}
