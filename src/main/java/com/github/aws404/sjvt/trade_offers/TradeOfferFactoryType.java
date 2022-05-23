package com.github.aws404.sjvt.trade_offers;

import com.mojang.serialization.Codec;

import com.github.aws404.sjvt.SimpleJsonVillagerTradesMod;
import com.github.aws404.sjvt.api.SerializableTradeOfferFactory;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.TradeOffers;

import static com.github.aws404.sjvt.SimpleJsonVillagerTradesMod.id;

public interface TradeOfferFactoryType<P extends TradeOffers.Factory> {
    TradeOfferFactoryType<TradeOffers.BuyForOneEmeraldFactory> BUY_FOR_ONE_EMERALD = register(new Identifier("buy_for_one_emerald"), VanillaTradeOfferFactories.BUY_FOR_ONE_EMERALD);
    TradeOfferFactoryType<TradeOffers.SellItemFactory> SELL_ITEM = register(new Identifier("sell_item"), VanillaTradeOfferFactories.SELL_ITEM);
    TradeOfferFactoryType<TradeOffers.SellSuspiciousStewFactory> SELL_SUSPICIOUS_STEW = register(new Identifier("sell_suspicious_stew"), VanillaTradeOfferFactories.SELL_SUSPICIOUS_STEW);
    TradeOfferFactoryType<TradeOffers.ProcessItemFactory> PROCESS_ITEM = register(new Identifier("process_item"), VanillaTradeOfferFactories.PROCESS_ITEM);
    TradeOfferFactoryType<TradeOffers.SellEnchantedToolFactory> SELL_ENCHANTED_TOOL = register(new Identifier("sell_enchanted_tool"), VanillaTradeOfferFactories.SELL_ENCHANTED_TOOL);
    TradeOfferFactoryType<TradeOffers.TypeAwareBuyForOneEmeraldFactory> TYPE_AWARE_BUY_FOR_ONE_EMERALD = register(new Identifier("type_aware_buy_for_one_emerald"), VanillaTradeOfferFactories.TYPE_AWARE_BUY_FOR_ONE_EMERALD);
    TradeOfferFactoryType<TradeOffers.SellPotionHoldingItemFactory> SELL_POTION_HOLDING_ITEM = register(new Identifier("sell_potion_holding_item"), VanillaTradeOfferFactories.SELL_POTION_HOLDING_ITEM);
    TradeOfferFactoryType<TradeOffers.EnchantBookFactory> ENCHANT_BOOK = register(new Identifier("enchant_book"), VanillaTradeOfferFactories.ENCHANT_BOOK);
    TradeOfferFactoryType<TradeOffers.SellMapFactory> SELL_MAP = register(new Identifier("sell_map"), VanillaTradeOfferFactories.SELL_MAP);
    TradeOfferFactoryType<TradeOffers.SellDyedArmorFactory> SELL_DYED_ARMOR = register(new Identifier("sell_dyed_armor"), VanillaTradeOfferFactories.SELL_DYED_ARMOR);

    TradeOfferFactoryType<SellItemForItemsOfferFactory> SELL_ITEM_FOR_ITEMS = register(id("sell_item_for_items"), SellItemForItemsOfferFactory.CODEC);
    TradeOfferFactoryType<TypeAwareSellItemForItemsOfferFactory> TYPE_AWARE_SELL_ITEMS_FOR_ITEM = register(id("type_aware_sell_item_for_items"), TypeAwareSellItemForItemsOfferFactory.CODEC);
    TradeOfferFactoryType<TypeAwareTradeOfferFactory> TYPE_AWARE = register(id("type_aware"), TypeAwareTradeOfferFactory.CODEC);

    Codec<TradeOffers.Factory> CODEC = SimpleJsonVillagerTradesMod.TRADE_OFFER_FACTORY_REGISTRY.getCodec().dispatch("type", factory -> ((TradeOfferFactoryTypeHolder) factory).getType(), TradeOfferFactoryType::codec);

    Codec<P> codec();

    static <P extends TradeOffers.Factory> TradeOfferFactoryType<P> register(Identifier id, Codec<P> codec) {
        return Registry.register(SimpleJsonVillagerTradesMod.TRADE_OFFER_FACTORY_REGISTRY, id, () -> codec);
    }

    static void bootstrap() {
    }
}
