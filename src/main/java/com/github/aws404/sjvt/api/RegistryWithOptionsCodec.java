package com.github.aws404.sjvt.api;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import net.minecraft.util.registry.Registry;

import java.util.stream.Stream;

public class RegistryWithOptionsCodec<T> extends MapCodec<T> {

    public static <R> Codec<R> of(Registry<Codec<? extends R>> registry) {
        return new RegistryWithOptionsCodec<>(registry, "type").codec();
    }

    protected final Registry<Codec<? extends T>> registry;
    private final String key;

    private RegistryWithOptionsCodec(Registry<Codec<? extends T>> registry, String key) {
        this.registry = registry;
        this.key = key;
    }

    @Override
    public <T1> DataResult<T> decode(DynamicOps<T1> ops, MapLike<T1> input) {
        DataResult<Codec<? extends T>> codec = this.registry.getCodec().decode(ops, input.get(this.key)).map(Pair::getFirst);
        if (codec.error().isPresent()) {
            return DataResult.error(codec.error().get().message());
        }
        DataResult<T> test = codec.result().get().decode(ops, ops.createMap(input.entries())).map(Pair::getFirst);
        if (test.error().isPresent()) {
            return DataResult.error(test.error().get().message());
        }

        return test;
    }

    @SuppressWarnings("unchecked")
    private <C extends T> C downCast(T controller) {
        return (C) controller;
    }

    @Override
    public <T1> RecordBuilder<T1> encode(T input, DynamicOps<T1> ops, RecordBuilder<T1> builder) {
        if (input instanceof CodecHolder codecHolder) {
            Codec<T> type = codecHolder.getCodec();
            builder.add(ops.createString(this.key), this.registry.getCodec().encodeStart(ops, type));

            type.encodeStart(JsonOps.INSTANCE, downCast(input)).result().ifPresent(jsonElement ->
                    jsonElement.getAsJsonObject().entrySet().forEach(entry ->
                            builder.add(ops.createString(entry.getKey()), JsonOps.INSTANCE.convertTo(ops, entry.getValue()))
                    )
            );

            return builder;
        }
        return builder.withErrorsFrom(DataResult.error("Input is not of serializable type"));
    }

    @Override
    public <T1> Stream<T1> keys(DynamicOps<T1> ops) {
        return null;
    }

    /**
     * Used for typing purposes only
     */
    public static <T> Class<Codec<? extends T>> getTypeForRegistry(@SuppressWarnings("unused") Class<T> type) {
        return null;
    }

    public interface CodecHolder<T> {
        Codec<T> getCodec();
    }
}
