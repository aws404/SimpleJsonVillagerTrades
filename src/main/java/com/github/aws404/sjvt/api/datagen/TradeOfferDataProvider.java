package com.github.aws404.sjvt.api.datagen;

import com.google.common.collect.Sets;
import com.google.common.hash.Hashing;
import com.google.common.hash.HashingOutputStream;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonWriter;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.slf4j.Logger;

import com.github.aws404.sjvt.TradeOfferManager;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.Util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public abstract class TradeOfferDataProvider implements DataProvider {
    protected static final Logger LOGGER = LogUtils.getLogger();
    protected static final Comparator<String> JSON_KEY_SORTING_COMPARATOR = Comparator.comparingInt(Util.make(new Object2IntOpenHashMap<>(), (map) -> {
        map.put("type", 0);
        map.put("profession", 1);
        map.put("replace", 2);
        map.defaultReturnValue(3);
        map.put("max_uses", 4);
        map.put("experience", 5);
    }));

    protected final DataOutput.PathResolver pathResolver;
    protected final CompletableFuture<RegistryWrapper.WrapperLookup> registryLookupFuture;

    public TradeOfferDataProvider(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookupFuture) {
        this.pathResolver = output.getResolver(DataOutput.OutputType.DATA_PACK, "trade_offers");
        this.registryLookupFuture = registryLookupFuture;
    }

    protected abstract void generateTradeOffers(Consumer<TradeOfferBuilder> exporter, RegistryWrapper.WrapperLookup lookup);

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        return this.registryLookupFuture.thenAccept((lookup) -> {
            Set<Identifier> set = Sets.newHashSet();
            Consumer<TradeOfferBuilder> consumer = (offerBuilder) -> {
                if (!set.add(offerBuilder.getId())) {
                    throw new IllegalStateException("Duplicate trade offer " + offerBuilder.getId());
                } else {
                    Path path = this.pathResolver.resolveJson(offerBuilder.getId());

                    try {
                        JsonElement result = TradeOfferManager.VillagerTrades.CODEC.encodeStart(JsonOps.INSTANCE, offerBuilder.build()).getOrThrow(false, LOGGER::error);
                        writeToPath(writer, result, path);
                    } catch (IOException var6) {
                        LOGGER.error("Couldn't save trade offer {}", path, var6);
                    }
                }
            };

            this.generateTradeOffers(consumer, lookup);
        });
    }

    @Override
    public String getName() {
        return "Trade Offers";
    }

    /**
     * Beta and Deprecated methods are also used in Vanilla's data generator, so
     * should be considered safe.
     */
    @SuppressWarnings({"UnstableApiUsage", "deprecation"})
    protected static void writeToPath(DataWriter writer, JsonElement json, Path path) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        HashingOutputStream hashingOutputStream = new HashingOutputStream(Hashing.sha1(), byteArrayOutputStream);
        Writer writer2 = new OutputStreamWriter(hashingOutputStream, StandardCharsets.UTF_8);
        JsonWriter jsonWriter = new JsonWriter(writer2);
        jsonWriter.setSerializeNulls(false);
        jsonWriter.setIndent("  ");
        JsonHelper.writeSorted(jsonWriter, json, JSON_KEY_SORTING_COMPARATOR);
        jsonWriter.close();
        writer.write(path, byteArrayOutputStream.toByteArray(), hashingOutputStream.hash());
    }
}
