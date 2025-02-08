package eu.koboo.minestom.invue.core;

import eu.koboo.minestom.invue.api.ViewRegistry;

public final class MinestomInvue {

    public static ViewRegistry create() {
        return new CoreViewRegistry();
    }
}
