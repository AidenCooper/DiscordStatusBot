package net.arcdevs.discordstatusbot.common.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class HandledLinkedHashMap<K,V> extends LinkedHashMap<K,V> {
    @Nullable
    public K getKey(@NotNull final V value) {
        for(Map.Entry<K, V> entry : this.entrySet()) {
            if(entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }

        return null;
    }

    public void put(@NotNull final K key, Supplier<V> value) {
        if(this.containsKey(key)) return;
        this.put(key, value.get());
    }
}
