package com.github.aws404.sjvt.trade_offers;

import com.github.aws404.sjvt.SimpleJsonVillagerTradesMod;
import com.google.gson.GsonBuilder;
import net.minecraft.util.*;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.TradeOffers;

public interface TradeOfferFactorySerialiser<T extends TradeOfferFactorySerialiser.TradeOfferFactory> extends JsonSerializer<T> {

    TradeOfferFactoryType BUY_FOR_ONE_EMERALD = Registry.register(SimpleJsonVillagerTradesMod.TRADE_OFFER_FACTORY_TYPE_REGISTRY, new Identifier("buy_for_one_emerald"), new TradeOfferFactoryType(new DefaultTradeOfferFactories.BuyForOneEmeraldFactory.Serialiser()));
    TradeOfferFactoryType SELL_ITEM = Registry.register(SimpleJsonVillagerTradesMod.TRADE_OFFER_FACTORY_TYPE_REGISTRY, new Identifier("sell_item"), new TradeOfferFactoryType(new DefaultTradeOfferFactories.SellItemFactory.Serialiser()));
    TradeOfferFactoryType SELL_SUSPICIOUS_STEW = Registry.register(SimpleJsonVillagerTradesMod.TRADE_OFFER_FACTORY_TYPE_REGISTRY, new Identifier("sell_suspicious_stew"), new TradeOfferFactoryType(new DefaultTradeOfferFactories.SellSuspiciousStewFactory.Serialiser()));
    TradeOfferFactoryType PROCESS_ITEM = Registry.register(SimpleJsonVillagerTradesMod.TRADE_OFFER_FACTORY_TYPE_REGISTRY, new Identifier("process_item"), new TradeOfferFactoryType(new DefaultTradeOfferFactories.ProcessItemFactory.Serialiser()));
    TradeOfferFactoryType SELL_ENCHANTED_TOOL = Registry.register(SimpleJsonVillagerTradesMod.TRADE_OFFER_FACTORY_TYPE_REGISTRY, new Identifier("sell_enchanted_tool"), new TradeOfferFactoryType(new DefaultTradeOfferFactories.SellEnchantedToolFactory.Serialiser()));
    TradeOfferFactoryType TYPE_AWARE_BUY_FOR_ONE_EMERALD = Registry.register(SimpleJsonVillagerTradesMod.TRADE_OFFER_FACTORY_TYPE_REGISTRY, new Identifier("type_aware_buy_for_one_emerald"), new TradeOfferFactoryType(new DefaultTradeOfferFactories.TypeAwareBuyForOneEmeraldFactory.Serialiser()));
    TradeOfferFactoryType SELL_POTION_HOLDING_ITEM = Registry.register(SimpleJsonVillagerTradesMod.TRADE_OFFER_FACTORY_TYPE_REGISTRY, new Identifier("sell_potion_holding_item"), new TradeOfferFactoryType(new DefaultTradeOfferFactories.SellPotionHoldingItemFactory.Serialiser()));
    TradeOfferFactoryType ENCHANT_BOOK = Registry.register(SimpleJsonVillagerTradesMod.TRADE_OFFER_FACTORY_TYPE_REGISTRY, new Identifier("enchant_book"), new TradeOfferFactoryType(new DefaultTradeOfferFactories.EnchantBookFactory.Serialiser()));
    TradeOfferFactoryType SELL_MAP = Registry.register(SimpleJsonVillagerTradesMod.TRADE_OFFER_FACTORY_TYPE_REGISTRY, new Identifier("sell_map"), new TradeOfferFactoryType(new DefaultTradeOfferFactories.SellMapFactory.Serialiser()));
    TradeOfferFactoryType SELL_DYED_ARMOR = Registry.register(SimpleJsonVillagerTradesMod.TRADE_OFFER_FACTORY_TYPE_REGISTRY, new Identifier("sell_dyed_armor"), new TradeOfferFactoryType(new DefaultTradeOfferFactories.SellDyedArmorFactory.Serialiser()));
    TradeOfferFactoryType SELL_ITEM_FOR_ITEMS = Registry.register(SimpleJsonVillagerTradesMod.TRADE_OFFER_FACTORY_TYPE_REGISTRY, SimpleJsonVillagerTradesMod.id("sell_item_for_items"), new TradeOfferFactoryType(new SellItemForItemsOfferFactory.Serialiser()));

    static GsonBuilder getTradeOffersGsonBuilder() {
        return new GsonBuilder().registerTypeHierarchyAdapter(TradeOfferFactory.class, JsonSerializing.createSerializerBuilder(SimpleJsonVillagerTradesMod.TRADE_OFFER_FACTORY_TYPE_REGISTRY, "provider", "type", TradeOfferFactory::getType).build());
    }

    interface TradeOfferFactory extends TradeOffers.Factory {
        TradeOfferFactoryType getType();
    }

}
