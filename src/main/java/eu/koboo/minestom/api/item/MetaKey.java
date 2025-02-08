package eu.koboo.minestom.api.item;

public record MetaKey(String key) {

    public static final MetaKey CONDITION = new MetaKey("meta_key_condition");
}
