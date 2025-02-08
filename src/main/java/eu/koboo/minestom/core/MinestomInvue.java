package eu.koboo.minestom.core;

import eu.koboo.minestom.api.ViewRegistry;

public final class MinestomInvue {

    public static ViewRegistry create() {
        return new CoreViewRegistry();
    }
}
