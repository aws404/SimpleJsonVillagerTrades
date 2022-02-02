package com.github.aws404.sjvt.trade_offers;

import com.github.aws404.sjvt.SimpleJsonVillagerTradesMod;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
    private static final Gson GSON = TradeOfferFactorySerialiser.getTradeOfferBuilder().create();

    private Map<Identifier, Int2ObjectMap<TradeOffers.Factory[]>> villagerOfferFactories = Map.of();
    private Map<WanderingTraderTradeRarity, TradeOffers.Factory[]> wanderingTraderOfferFactories = Map.of();

    public TradeOfferManager() {
        super(GSON, ID.getPath());
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        HashMap<Identifier, Int2ObjectMap<List<TradeOffers.Factory>>> villagerBuildingMap = new HashMap<>();
        HashMap<WanderingTraderTradeRarity, List<TradeOffers.Factory>> wanderingTraderBuildingMap = new HashMap<>();

        AtomicInteger loadedCount = new AtomicInteger();

        // Firstly, add the hardcoded trades to the new map
        TradeOffers.PROFESSION_TO_LEVELED_TRADE.forEach((profession, int2ObjectMap) -> {
            Identifier id = new Identifier(profession.getId());
            villagerBuildingMap.putIfAbsent(id, new Int2ObjectOpenHashMap<>());
            int2ObjectMap.forEach((integer, factories) -> {
                villagerBuildingMap.get(id).putIfAbsent(integer, new ArrayList<>());
                villagerBuildingMap.get(id).get((int) integer).addAll(List.of(factories));
            });
        });
        TradeOffers.WANDERING_TRADER_TRADES.forEach((integer, factories) -> {
            WanderingTraderTradeRarity rarity = WanderingTraderTradeRarity.fromVanillaId(integer).orElse(WanderingTraderTradeRarity.COMMON);
            wanderingTraderBuildingMap.putIfAbsent(rarity, new ArrayList<>());
            wanderingTraderBuildingMap.get(rarity).addAll(List.of(factories));
        });

        // Secondly, parse and add the JSON data
        prepared.forEach((identifier, jsonElement) -> {
            JsonObject topObject = JsonHelper.asObject(jsonElement, "top level");
            Identifier profession = new Identifier(JsonHelper.getString(topObject, "profession"));
            boolean replace = JsonHelper.getBoolean(topObject, "replace", false);
            JsonObject offersObject = JsonHelper.getObject(topObject, "offers");

            if (profession.equals(EntityType.getId(EntityType.WANDERING_TRADER))) {
                // Wandering Trader
                if (replace) {
                    wanderingTraderBuildingMap.put(WanderingTraderTradeRarity.COMMON, new ArrayList<>());
                    wanderingTraderBuildingMap.put(WanderingTraderTradeRarity.RARE, new ArrayList<>());
                }
                offersObject.keySet().forEach(s -> {
                    WanderingTraderTradeRarity key = WanderingTraderTradeRarity.valueOf(s.toUpperCase());
                    List<TradeOffers.Factory> offers = StreamSupport.stream(JsonHelper.getArray(offersObject, s).spliterator(), false).map(jsonElement1 -> (TradeOffers.Factory) GSON.fromJson(jsonElement1, TradeOfferFactorySerialiser.TradeOfferFactory.class)).toList();
                    wanderingTraderBuildingMap.putIfAbsent(key, new ArrayList<>());
                    wanderingTraderBuildingMap.get(key).addAll(offers);
                });
            } else {
                // Villagers
                if (replace) {
                    villagerBuildingMap.put(profession, new Int2ObjectOpenHashMap<>());
                } else {
                    villagerBuildingMap.putIfAbsent(profession, new Int2ObjectOpenHashMap<>());
                }

                offersObject.keySet().forEach(s -> {
                    VillagerTradeLevel key = VillagerTradeLevel.valueOf(s.toUpperCase());
                    List<TradeOffers.Factory> offers = StreamSupport.stream(JsonHelper.getArray(offersObject, s).spliterator(), false).map(jsonElement1 -> (TradeOffers.Factory) GSON.fromJson(jsonElement1, TradeOfferFactorySerialiser.TradeOfferFactory.class)).toList();
                    villagerBuildingMap.get(profession).putIfAbsent(key.vanillaId, new ArrayList<>());
                    villagerBuildingMap.get(profession).get(key.vanillaId).addAll(offers);
                });
            }
            loadedCount.incrementAndGet();
        });

        ImmutableMap.Builder<Identifier, Int2ObjectMap<TradeOffers.Factory[]>> builder = ImmutableMap.builder();
        villagerBuildingMap.forEach((identifier, listInt2ObjectMap) -> {
            ImmutableMap.Builder<Integer, TradeOffers.Factory[]> innerBuilder = ImmutableMap.builder();
            listInt2ObjectMap.forEach((integer, tradeOfferFactories) -> innerBuilder.put(integer, tradeOfferFactories.toArray(TradeOffers.Factory[]::new)));
            builder.put(identifier, new Int2ObjectOpenHashMap<>(innerBuilder.build()));
        });

        this.villagerOfferFactories = builder.build();

        ImmutableMap.Builder<WanderingTraderTradeRarity, TradeOffers.Factory[]> wanderingTraderBuilder = ImmutableMap.builder();
        wanderingTraderBuildingMap.forEach((rarity, offers) -> wanderingTraderBuilder.put(rarity, offers.toArray(TradeOffers.Factory[]::new)));
        this.wanderingTraderOfferFactories = wanderingTraderBuilder.build();

        SimpleJsonVillagerTradesMod.LOGGER.info("Loaded {} trade offer files", loadedCount.get());
    }

    public Optional<Int2ObjectMap<TradeOffers.Factory[]>> getOffers(VillagerProfession profession) {
        return Optional.ofNullable(villagerOfferFactories.get(Registry.VILLAGER_PROFESSION.getId(profession)));
    }

    public Optional<TradeOffers.Factory[]> getWanderingTraderOffers(WanderingTraderTradeRarity rarity) {
        return Optional.ofNullable(wanderingTraderOfferFactories.get(rarity));
    }

    @Override
    public Identifier getFabricId() {
        return ID;
    }

    @SuppressWarnings("unused")
    public enum VillagerTradeLevel {
        NOVICE(1),
        APPRENTICE(2),
        JOURNEYMAN(3),
        EXPERT(4),
        MASTER(5);

        public final int vanillaId;

        VillagerTradeLevel(int vanillaId) {
            this.vanillaId = vanillaId;
        }
    }

    public enum WanderingTraderTradeRarity {
        COMMON(1),
        RARE(2);

        public final int vanillaId;

        WanderingTraderTradeRarity(int vanillaId) {
            this.vanillaId = vanillaId;
        }

        public static Optional<WanderingTraderTradeRarity> fromVanillaId(int vanillaId) {
            return Arrays.stream(values()).filter(rarity -> rarity.vanillaId == vanillaId).findFirst();
        }
    }

}
