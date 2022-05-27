package com.github.aws404.sjvt;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.BaseMapCodec;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public record DefaultMapCodec<K, V>(Codec<K> keyCodec, Codec<V> valueCodec, Keyable requiredKeys) implements BaseMapCodec<K, V>, Codec<Map<K, V>> {
    private static final String DEFAULT_VALUE_KEY = "default";

    public static <K, V> DefaultMapCodec<K, V> of(Codec<K> keyCodec, Codec<V> valueCodec, Keyable requiredKeys) {
        return new DefaultMapCodec<>(keyCodec, valueCodec, requiredKeys);
    }

    @Override
    public <T> DataResult<Map<K, V>> decode(final DynamicOps<T> ops, final MapLike<T> input) {
        final AtomicReference<DataResult<Map<K, V>>> result = new AtomicReference<>(DataResult.success(new HashMap<>()));

        this.requiredKeys.keys(ops).forEach(key -> {
            DataResult<V> value = getOrDefault(input, key, ops);

            value.error().ifPresent(vPartialResult -> {
                // Make the DataResult partial (error) if it was not
                if (result.get().error().isEmpty()) {
                    result.set(result.get().flatMap(kvMap -> DataResult.error("", kvMap)));
                }

                result.set(result.get().mapError(s -> s + " " + vPartialResult.message()));
            });

            value.result().ifPresent(v -> result.get().map(kvMap -> {
                kvMap.put(keyCodec().parse(ops, key).result().orElseThrow(), v);
                return kvMap;
            }));
        });

        return result.get();
    }

    private <T> DataResult<V> getOrDefault(final MapLike<T> input, final T key, final DynamicOps<T> ops) {
        final T encodedValue = input.get(key);

        if (encodedValue == null) {
            final T encodedDefaultValue = input.get(DEFAULT_VALUE_KEY);
            if (encodedDefaultValue == null) {
                return DataResult.error("Missing value for key " + key + " and no default defined");
            }
            return elementCodec().decode(ops, encodedDefaultValue).map(Pair::getFirst);
        }

        return elementCodec().decode(ops, encodedValue).map(Pair::getFirst);
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
        return ops.getMap(input).flatMap(map -> decode(ops, map)).map(r -> Pair.of(r, input));
    }
}
