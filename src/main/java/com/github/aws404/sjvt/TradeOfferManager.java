package com.github.aws404.sjvt;

import com.github.aws404.sjvt.trade_offers.TradeOfferFactory;
import com.github.aws404.sjvt.trade_offers.TradeOfferFactoryType;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.entity.EntityType;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.StreamSupport;

public class TradeOfferManager extends JsonDataLoader implements IdentifiableResourceReloadListener {

    private static final Identifier ID = SimpleJsonVillagerTradesMod.id("trade_offers");
    private static final Gson GSON = TradeOfferFactoryType.getTradeOffersGsonBuilder().create();
    private static final Identifier WANDERING_TRADER_PROFESSION_ID = Registry.ENTITY_TYPE.getId(EntityType.WANDERING_TRADER);

    private Map<Identifier, Int2ObjectMap<TradeOffers.Factory[]>> offerFactories = Map.of();

    public TradeOfferManager() {
        super(GSON, ID.getPath());
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        Map<Identifier, Int2ObjectMap<List<TradeOffers.Factory>>> builderMap = new HashMap<>();

        // Firstly, add the hardcoded trades to the new map
        loadVanillaTradesIntoMap(builderMap);

        // Secondly, parse and add the JSON data
        AtomicInteger loadedCount = new AtomicInteger();
        prepared.forEach((identifier, jsonElement) -> {
            try {
                JsonObject topObject = JsonHelper.asObject(jsonElement, "top level");
                Identifier profession = new Identifier(JsonHelper.getString(topObject, "profession"));
                boolean replace = JsonHelper.getBoolean(topObject, "replace", false);
                JsonObject offersObject = JsonHelper.getObject(topObject, "offers");

                if (replace) {
                    builderMap.put(profession, new Int2ObjectOpenHashMap<>());
                } else {
                    builderMap.putIfAbsent(profession, new Int2ObjectOpenHashMap<>());
                }

                offersObject.keySet().forEach(s -> {
                    MerchantLevel key = MerchantLevel.valueOf(s.toUpperCase());
                    List<TradeOffers.Factory> offers = StreamSupport.stream(JsonHelper.getArray(offersObject, s).spliterator(), false).map(element -> (TradeOffers.Factory) GSON.fromJson(JsonHelper.asObject(element, s + "[?]"), TradeOfferFactory.class)).toList();
                    builderMap.get(profession).putIfAbsent(key.id, new ArrayList<>());
                    builderMap.get(profession).get(key.id).addAll(offers);
                });
                loadedCount.incrementAndGet();
            } catch (JsonParseException e) {
                SimpleJsonVillagerTradesMod.LOGGER.error("Couldn't parse trade offer {}", identifier, e);
            }
        });

        ImmutableMap.Builder<Identifier, Int2ObjectMap<TradeOffers.Factory[]>> builder = ImmutableMap.builder();
        builderMap.forEach((identifier, listInt2ObjectMap) -> {
            ImmutableMap.Builder<Integer, TradeOffers.Factory[]> innerBuilder = ImmutableMap.builder();
            listInt2ObjectMap.forEach((integer, tradeOfferFactories) -> innerBuilder.put(integer, tradeOfferFactories.toArray(TradeOffers.Factory[]::new)));
            builder.put(identifier, new Int2ObjectOpenHashMap<>(innerBuilder.build()));
        });

        this.offerFactories = builder.build();

        SimpleJsonVillagerTradesMod.LOGGER.info("Loaded {} trade offer files", loadedCount.get());
    }

    public Optional<Int2ObjectMap<TradeOffers.Factory[]>> getVillagerOffers(VillagerProfession profession) {
        return Optional.ofNullable(offerFactories.get(Registry.VILLAGER_PROFESSION.getId(profession)));
    }

    public Optional<TradeOffers.Factory[]> getWanderingTraderOffers(MerchantLevel rarity) {
        return Optional.ofNullable(offerFactories.get(WANDERING_TRADER_PROFESSION_ID).get(rarity.id));
    }

    public static void loadVanillaTradesIntoMap(Map<Identifier, Int2ObjectMap<List<TradeOffers.Factory>>> builderMap) {
        TradeOffers.PROFESSION_TO_LEVELED_TRADE.forEach((profession, int2ObjectMap) -> {
            Identifier id = new Identifier(profession.getId());
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

    @SuppressWarnings("unused")
    public enum MerchantLevel {
        NOVICE(1),
        APPRENTICE(2),
        JOURNEYMAN(3),
        EXPERT(4),
        MASTER(5),
        COMMON(1),
        RARE(2);

        public final int id;

        MerchantLevel(int id) {
            this.id = id;
        }
    }

}
