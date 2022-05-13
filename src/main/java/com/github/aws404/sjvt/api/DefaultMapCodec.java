package com.github.aws404.sjvt.api;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.BaseMapCodec;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public record DefaultMapCodec<K, V>(Codec<K> keyCodec, Codec<V> valueCodec, Keyable requiredKeys) implements BaseMapCodec<K, V>, Codec<Map<K, V>> {
    public static <K, V> DefaultMapCodec<K, V> of(Codec<K> keyCodec, Codec<V> valueCodec, Keyable requiredKeys) {
        return new DefaultMapCodec<>(keyCodec, valueCodec, requiredKeys);
    }

    @Override
    public <T> DataResult<Map<K, V>> decode(final DynamicOps<T> ops, final MapLike<T> input) {
        final ImmutableMap.Builder<K, V> read = ImmutableMap.builder();
        final ImmutableList.Builder<T> failedKeys = ImmutableList.builder();
        final V defaultValue = elementCodec().decode(ops, input.get(ops.createString("default"))).map(Pair::getFirst).result().orElse(null);

        this.requiredKeys.keys(ops).forEach(key -> {
            V foundValue = Optional.ofNullable(input.get(key)).flatMap(t -> elementCodec().parse(ops, t).result()).orElse(defaultValue);
            if (foundValue == null) {
                failedKeys.add(key);
                return;
            }
            read.put(keyCodec().parse(ops, key).result().orElseThrow(), foundValue);
        });

        final Map<K, V> map = read.build();
        final List<T> errorKeys = failedKeys.build();

        return DataResult.success(Unit.INSTANCE, Lifecycle.stable()).map(unit -> map).mapError(s -> s + " missed keys with no default defined: " + errorKeys);
    }

    @Override
    public Codec<V> elementCodec() {
        return this.valueCodec;
    }

    @Override
    public <T> DataResult<T> encode(final Map<K, V> input, final DynamicOps<T> ops, final T prefix) {
        return encode(input, ops, ops.mapBuilder()).build(prefix);
    }

    @Override
    public <T> DataResult<Pair<Map<K, V>, T>> decode(final DynamicOps<T> ops, final T input) {
        return ops.getMap(input).setLifecycle(Lifecycle.stable()).flatMap(map -> decode(ops, map)).map(r -> Pair.of(r, input));
    }
}
