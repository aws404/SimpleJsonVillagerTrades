package com.github.aws404.sjvt;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import com.github.aws404.sjvt.trade_offers.TradeOfferFactoryType;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;

import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TradeOfferManager extends JsonDataLoader implements IdentifiableResourceReloadListener {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final Identifier WANDERING_TRADER_PROFESSION_ID = Registries.ENTITY_TYPE.getId(EntityType.WANDERING_TRADER);
    private static final Identifier ID = SimpleJsonVillagerTradesMod.id("trade_offers");

    private Map<Identifier, Int2ObjectMap<TradeOffers.Factory[]>> offerFactories = Map.of();

    public TradeOfferManager() {
        super(GSON, ID.getPath());
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        Map<Identifier, Int2ObjectMap<List<TradeOffers.Factory>>> builderMap = new HashMap<>();

        loadVanillaTradesIntoMap(builderMap);

        AtomicInteger loadedCount = new AtomicInteger();
        prepared.forEach((identifier, jsonElement) -> {
            try {
                VillagerTrades trades = VillagerTrades.CODEC.decode(JsonOps.INSTANCE, jsonElement).getOrThrow(false, s -> SimpleJsonVillagerTradesMod.LOGGER.error("Failed to read file {}: {}", identifier.toString(), s)).getFirst();
                if (trades.replace) {
                    builderMap.put(trades.profession, new Int2ObjectOpenHashMap<>());
                } else {
                    builderMap.putIfAbsent(trades.profession, new Int2ObjectOpenHashMap<>());
                }

                trades.trades.forEach((level, factories) -> {
                    builderMap.get(trades.profession).putIfAbsent(level, new ArrayList<>());
                    builderMap.get(trades.profession).get((int) level).addAll(factories);
                });
                loadedCount.incrementAndGet();
            } catch (Exception ignored) { }
        });

        this.offerFactories = builderMap.entrySet().stream().map(entry -> {
            Int2ObjectMap<TradeOffers.Factory[]> entries = entry.getValue().int2ObjectEntrySet().stream().map(entry2 -> Pair.of(entry2.getIntKey(), entry2.getValue().toArray(TradeOffers.Factory[]::new))).collect(Int2ObjectOpenHashMap::new, (map, pair) -> map.put((int) pair.getFirst(), pair.getSecond()), Int2ObjectOpenHashMap::putAll);
            return Pair.of(entry.getKey(), entries);
        }).collect(ImmutableMap.toImmutableMap(Pair::getFirst, Pair::getSecond));

        SimpleJsonVillagerTradesMod.LOGGER.info("Loaded {} trade offer files", loadedCount.get());
    }

    public Optional<Int2ObjectMap<TradeOffers.Factory[]>> getVillagerOffers(VillagerProfession profession) {
        return Optional.ofNullable(offerFactories.get(Registries.VILLAGER_PROFESSION.getId(profession)));
    }

    public Optional<TradeOffers.Factory[]> getWanderingTraderOffers(int rarity) {
        return Optional.ofNullable(offerFactories.get(WANDERING_TRADER_PROFESSION_ID).get(rarity));
    }

    public static void loadVanillaTradesIntoMap(Map<Identifier, Int2ObjectMap<List<TradeOffers.Factory>>> builderMap) {
        TradeOffers.PROFESSION_TO_LEVELED_TRADE.forEach((profession, int2ObjectMap) -> {
            Identifier id = new Identifier(profession.id());
            builderMap.putIfAbsent(id, new Int2ObjectOpenHashMap<>());
            int2ObjectMap.forEach((integer, factories) -> {
                builderMap.get(id).putIfAbsent(integer, new ArrayList<>());
                builderMap.get(id).get((int) integer).addAll(List.of(factories));
            });
        });

        builderMap.putIfAbsent(WANDERING_TRADER_PROFESSION_ID, new Int2ObjectOpenHashMap<>());
        builderMap.get(WANDERING_TRADER_PROFESSION_ID).putIfAbsent(MerchantLevel.COMMON.id, new ArrayList<>());
        builderMap.get(WANDERING_TRADER_PROFESSION_ID).putIfAbsent(MerchantLevel.RARE.id, new ArrayList<>());
        TradeOffers.WANDERING_TRADER_TRADES.forEach((integer, factories) -> builderMap.get(WANDERING_TRADER_PROFESSION_ID).get((int) integer).addAll(List.of(factories)));
    }

    @Override
    public Identifier getFabricId() {
        return ID;
    }

    public enum MerchantLevel implements StringIdentifiable {
        NOVICE("novice", 1),
        APPRENTICE("apprentice", 2),
        JOURNEYMAN("journeyman", 3),
        EXPERT("expert", 4),
        MASTER("master", 5),
        COMMON("common", 1),
        RARE("rare", 2);

        public final String name;
        public final int id;

        MerchantLevel(String name, int id) {
            this.name = name;
            this.id = id;
        }

        public int getId() {
            return this.id;
        }

        public static DataResult<MerchantLevel> fromId(int id) {
            for (MerchantLevel value : values()) {
                if (value.id == id) {
                    return DataResult.success(value);
                }
            }

            return DataResult.error(() -> "Invalid level index " + id + " provided.");
        }

        @Override
        public String asString() {
            return this.name;
        }
    }

    public static record VillagerTrades(Identifier profession, boolean replace, Map<Integer, List<TradeOffers.Factory>> trades) {
        public static final Codec<VillagerTrades> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Identifier.CODEC.fieldOf("profession").forGetter(VillagerTrades::profession),
                Codec.BOOL.optionalFieldOf("replace", false).forGetter(VillagerTrades::replace),
                Codec.unboundedMap(
                        StringIdentifiable.createCodec(MerchantLevel::values).flatComapMap(MerchantLevel::getId, MerchantLevel::fromId),
                        TradeOfferFactoryType.CODEC.listOf()
                ).fieldOf("offers").forGetter(VillagerTrades::trades)
        ).apply(instance, VillagerTrades::new));
    }

}
