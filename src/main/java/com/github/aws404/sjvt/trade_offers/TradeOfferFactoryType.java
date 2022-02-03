package com.github.aws404.sjvt.trade_offers;

import com.github.aws404.sjvt.SimpleJsonVillagerTradesMod;
import com.google.gson.GsonBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonSerializableType;
import net.minecraft.util.JsonSerializer;
import net.minecraft.util.JsonSerializing;
import net.minecraft.util.registry.Registry;

public class TradeOfferFactoryType extends JsonSerializableType<TradeOfferFactory> {
    public static final TradeOfferFactoryType BUY_FOR_ONE_EMERALD = Registry.register(SimpleJsonVillagerTradesMod.TRADE_OFFER_FACTORY_TYPE_REGISTRY, new Identifier("buy_for_one_emerald"), new TradeOfferFactoryType(new VanillaTradeOfferFactories.BuyForOneEmeraldFactory.Serialiser()));
    public static final TradeOfferFactoryType SELL_ITEM = Registry.register(SimpleJsonVillagerTradesMod.TRADE_OFFER_FACTORY_TYPE_REGISTRY, new Identifier("sell_item"), new TradeOfferFactoryType(new VanillaTradeOfferFactories.SellItemFactory.Serialiser()));
    public static final TradeOfferFactoryType SELL_SUSPICIOUS_STEW = Registry.register(SimpleJsonVillagerTradesMod.TRADE_OFFER_FACTORY_TYPE_REGISTRY, new Identifier("sell_suspicious_stew"), new TradeOfferFactoryType(new VanillaTradeOfferFactories.SellSuspiciousStewFactory.Serialiser()));
    public static final TradeOfferFactoryType PROCESS_ITEM = Registry.register(SimpleJsonVillagerTradesMod.TRADE_OFFER_FACTORY_TYPE_REGISTRY, new Identifier("process_item"), new TradeOfferFactoryType(new VanillaTradeOfferFactories.ProcessItemFactory.Serialiser()));
    public static final TradeOfferFactoryType SELL_ENCHANTED_TOOL = Registry.register(SimpleJsonVillagerTradesMod.TRADE_OFFER_FACTORY_TYPE_REGISTRY, new Identifier("sell_enchanted_tool"), new TradeOfferFactoryType(new VanillaTradeOfferFactories.SellEnchantedToolFactory.Serialiser()));
    public static final TradeOfferFactoryType TYPE_AWARE_BUY_FOR_ONE_EMERALD = Registry.register(SimpleJsonVillagerTradesMod.TRADE_OFFER_FACTORY_TYPE_REGISTRY, new Identifier("type_aware_buy_for_one_emerald"), new TradeOfferFactoryType(new VanillaTradeOfferFactories.TypeAwareBuyForOneEmeraldFactory.Serialiser()));
    public static final TradeOfferFactoryType SELL_POTION_HOLDING_ITEM = Registry.register(SimpleJsonVillagerTradesMod.TRADE_OFFER_FACTORY_TYPE_REGISTRY, new Identifier("sell_potion_holding_item"), new TradeOfferFactoryType(new VanillaTradeOfferFactories.SellPotionHoldingItemFactory.Serialiser()));
    public static final TradeOfferFactoryType ENCHANT_BOOK = Registry.register(SimpleJsonVillagerTradesMod.TRADE_OFFER_FACTORY_TYPE_REGISTRY, new Identifier("enchant_book"), new TradeOfferFactoryType(new VanillaTradeOfferFactories.EnchantBookFactory.Serialiser()));
    public static final TradeOfferFactoryType SELL_MAP = Registry.register(SimpleJsonVillagerTradesMod.TRADE_OFFER_FACTORY_TYPE_REGISTRY, new Identifier("sell_map"), new TradeOfferFactoryType(new VanillaTradeOfferFactories.SellMapFactory.Serialiser()));
    public static final TradeOfferFactoryType SELL_DYED_ARMOR = Registry.register(SimpleJsonVillagerTradesMod.TRADE_OFFER_FACTORY_TYPE_REGISTRY, new Identifier("sell_dyed_armor"), new TradeOfferFactoryType(new VanillaTradeOfferFactories.SellDyedArmorFactory.Serialiser()));

    public static final TradeOfferFactoryType SELL_ITEM_FOR_ITEMS = Registry.register(SimpleJsonVillagerTradesMod.TRADE_OFFER_FACTORY_TYPE_REGISTRY, SimpleJsonVillagerTradesMod.id("sell_item_for_items"), new TradeOfferFactoryType(new SellItemForItemsOfferFactory.Serialiser()));
    public static final TradeOfferFactoryType TYPE_AWARE_SELL_ITEM_FOR_ITEMS = Registry.register(SimpleJsonVillagerTradesMod.TRADE_OFFER_FACTORY_TYPE_REGISTRY, SimpleJsonVillagerTradesMod.id("type_aware_sell_item_for_items"), new TradeOfferFactoryType(new TypeAwareSellItemForItemsOfferFactory.Serialiser()));

    public static GsonBuilder getTradeOffersGsonBuilder() {
        return new GsonBuilder().registerTypeHierarchyAdapter(TradeOfferFactory.class, JsonSerializing.createSerializerBuilder(SimpleJsonVillagerTradesMod.TRADE_OFFER_FACTORY_TYPE_REGISTRY, "provider", "type", TradeOfferFactory::getType).build());
    }

    public TradeOfferFactoryType(JsonSerializer<? extends TradeOfferFactory> jsonSerializer) {
        super(jsonSerializer);
    }
}
