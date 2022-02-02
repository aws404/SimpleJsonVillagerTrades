package com.github.aws404.sjvt.trade_offers;

import com.github.aws404.sjvt.SimpleJsonVillagerTradesMod;
import com.google.gson.*;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.map.MapIcon;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.*;
import net.minecraft.util.JsonSerializer;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerType;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.Map;
import java.util.stream.Collectors;

public interface TradeOfferFactorySerialiser<T extends TradeOfferFactorySerialiser.TradeOfferFactory> extends JsonSerializer<T> {

    TradeOfferFactoryType BUY_FOR_ONE_EMERALD = Registry.register(SimpleJsonVillagerTradesMod.TRADE_OFFER_FACTORY_TYPE_REGISTRY, new Identifier("buy_for_one_emerald"), new TradeOfferFactoryType(new BuyForOneEmeraldFactory.Serailiser()));
    TradeOfferFactoryType SELL_ITEM = Registry.register(SimpleJsonVillagerTradesMod.TRADE_OFFER_FACTORY_TYPE_REGISTRY, new Identifier("sell_item"), new TradeOfferFactoryType(new SellItemFactory.Serailiser()));
    TradeOfferFactoryType SELL_SUSPICIOUS_STEW = Registry.register(SimpleJsonVillagerTradesMod.TRADE_OFFER_FACTORY_TYPE_REGISTRY, new Identifier("sell_suspicious_stew"), new TradeOfferFactoryType(new SellSuspiciousStewFactory.Serailiser()));
    TradeOfferFactoryType PROCESS_ITEM = Registry.register(SimpleJsonVillagerTradesMod.TRADE_OFFER_FACTORY_TYPE_REGISTRY, new Identifier("process_item"), new TradeOfferFactoryType(new ProcessItemFactory.Serailiser()));
    TradeOfferFactoryType SELL_ENCHANTED_TOOL = Registry.register(SimpleJsonVillagerTradesMod.TRADE_OFFER_FACTORY_TYPE_REGISTRY, new Identifier("sell_enchanted_tool"), new TradeOfferFactoryType(new SellEnchantedToolFactory.Serailiser()));
    TradeOfferFactoryType TYPE_AWARE_BUY_FOR_ONE_EMERALD = Registry.register(SimpleJsonVillagerTradesMod.TRADE_OFFER_FACTORY_TYPE_REGISTRY, new Identifier("type_aware_buy_for_one_emerald"), new TradeOfferFactoryType(new TypeAwareBuyForOneEmeraldFactory.Serailiser()));
    TradeOfferFactoryType SELL_POTION_HOLDING_ITEM = Registry.register(SimpleJsonVillagerTradesMod.TRADE_OFFER_FACTORY_TYPE_REGISTRY, new Identifier("sell_potion_holding_item"), new TradeOfferFactoryType(new SellPotionHoldingItemFactory.Serailiser()));
    TradeOfferFactoryType ENCHANT_BOOK = Registry.register(SimpleJsonVillagerTradesMod.TRADE_OFFER_FACTORY_TYPE_REGISTRY, new Identifier("enchant_book"), new TradeOfferFactoryType(new EnchantBookFactory.Serailiser()));
    TradeOfferFactoryType SELL_MAP = Registry.register(SimpleJsonVillagerTradesMod.TRADE_OFFER_FACTORY_TYPE_REGISTRY, new Identifier("sell_map"), new TradeOfferFactoryType(new SellMapFactory.Serailiser()));
    TradeOfferFactoryType SELL_DYED_ARMOR = Registry.register(SimpleJsonVillagerTradesMod.TRADE_OFFER_FACTORY_TYPE_REGISTRY, new Identifier("sell_dyed_armor"), new TradeOfferFactoryType(new SellDyedArmorFactory.Serailiser()));

    static GsonBuilder getTradeOfferBuilder() {
        return new GsonBuilder().registerTypeHierarchyAdapter(TradeOfferFactory.class, JsonSerializing.createSerializerBuilder(SimpleJsonVillagerTradesMod.TRADE_OFFER_FACTORY_TYPE_REGISTRY, "provider", "type", TradeOfferFactory::getType).build());
    }

    interface TradeOfferFactory extends TradeOffers.Factory {
        TradeOfferFactoryType getType();
    }

    class BuyForOneEmeraldFactory extends TradeOffers.BuyForOneEmeraldFactory implements TradeOfferFactory {

        // {item} x{price} = Emerald x1
        public BuyForOneEmeraldFactory(ItemConvertible item, int price, int maxUses, int experience) {
            super(item, price, maxUses, experience);
        }

        @Override
        public TradeOfferFactoryType getType() {
            return BUY_FOR_ONE_EMERALD;
        }

        public static class Serailiser implements TradeOfferFactorySerialiser<BuyForOneEmeraldFactory> {

            @Override
            public void toJson(JsonObject json, BuyForOneEmeraldFactory object, JsonSerializationContext context) {
                // TODO - Implement serialisation
            }

            @Override
            public BuyForOneEmeraldFactory fromJson(JsonObject json, JsonDeserializationContext context) {
                Item item = ShapedRecipe.getItem(json);
                int price = JsonHelper.getInt(json, "price");
                int maxUses = JsonHelper.getInt(json, "max_uses", 12);
                int experience = JsonHelper.getInt(json, "experience", 2);
                return new BuyForOneEmeraldFactory(item, price, maxUses, experience);
            }
        }
    }

    class SellItemFactory extends TradeOffers.SellItemFactory implements TradeOfferFactory {

        // Emeralds x{price} = {item} x{count}
        public SellItemFactory(Item item, int price, int count, int maxUses, int experience) {
            super(item, price, count, maxUses, experience);
        }

        @Override
        public TradeOfferFactoryType getType() {
            return SELL_ITEM;
        }

        public static class Serailiser implements TradeOfferFactorySerialiser<SellItemFactory> {

            @Override
            public void toJson(JsonObject json, SellItemFactory object, JsonSerializationContext context) {
                // TODO - Implement serialisation
            }

            @Override
            public SellItemFactory fromJson(JsonObject json, JsonDeserializationContext context) {
                Item item = ShapedRecipe.getItem(json);
                int count = JsonHelper.getInt(json, "count", 1);
                int price = JsonHelper.getInt(json, "price");
                int maxUses = JsonHelper.getInt(json, "max_uses", 12);
                int experience = JsonHelper.getInt(json, "experience", 2);
                return new SellItemFactory(item, count, price, maxUses, experience);
            }
        }
    }

    class SellSuspiciousStewFactory extends TradeOffers.SellSuspiciousStewFactory implements TradeOfferFactory {

        // Emerald x1 = Suspicious Stew ({effect}) x1
        public SellSuspiciousStewFactory(StatusEffect effect, int duration, int experience) {
            super(effect, duration, experience);
        }

        @Override
        public TradeOfferFactoryType getType() {
            return SELL_SUSPICIOUS_STEW;
        }

        public static class Serailiser implements TradeOfferFactorySerialiser<SellSuspiciousStewFactory> {

            @Override
            public void toJson(JsonObject json, SellSuspiciousStewFactory object, JsonSerializationContext context) {
                // TODO - Implement serialisation
            }

            @Override
            public SellSuspiciousStewFactory fromJson(JsonObject json, JsonDeserializationContext context) {
                StatusEffect effect = Registry.STATUS_EFFECT.get(new Identifier(JsonHelper.getString(json, "effect")));
                int duration = JsonHelper.getInt(json, "duration");
                int experience = JsonHelper.getInt(json, "experience", 2);
                return new SellSuspiciousStewFactory(effect, duration, experience);
            }
        }
    }

    class ProcessItemFactory extends TradeOffers.ProcessItemFactory implements TradeOfferFactory {

        // Emeralds x{price} + {item} x{secondCount} = {sellItem} x{sellCount}
        public ProcessItemFactory(ItemConvertible item, int secondCount, int price, Item sellItem, int sellCount, int maxUses, int experience) {
            super(item, secondCount, price, sellItem, sellCount, maxUses, experience);
        }

        @Override
        public TradeOfferFactoryType getType() {
            return PROCESS_ITEM;
        }

        public static class Serailiser implements TradeOfferFactorySerialiser<ProcessItemFactory> {

            @Override
            public void toJson(JsonObject json, ProcessItemFactory object, JsonSerializationContext context) {
                // TODO - Implement serialisation
            }

            @Override
            public ProcessItemFactory fromJson(JsonObject json, JsonDeserializationContext context) {
                Item item = ShapedRecipe.getItem(json);
                int secondCount = JsonHelper.getInt(json, "secondCount", 1);
                int price = JsonHelper.getInt(json, "price");
                Item sellItem = JsonHelper.getItem(json, "sellItem");
                int sellCount = JsonHelper.getInt(json, "sellCount", 1);
                int maxUses = JsonHelper.getInt(json, "max_uses", 12);
                int experience = JsonHelper.getInt(json, "experience", 2);
                return new ProcessItemFactory(item, secondCount, price, sellItem, sellCount, maxUses, experience);
            }
        }
    }

    class SellEnchantedToolFactory extends TradeOffers.SellEnchantedToolFactory implements TradeOfferFactory {

        // Emeralds x{basePrice} = {item} (randomly enchanted) x1
        public SellEnchantedToolFactory(Item item, int basePrice, int maxUses, int experience, float multiplier) {
            super(item, basePrice, maxUses, experience, multiplier);
        }

        @Override
        public TradeOfferFactoryType getType() {
            return SELL_ENCHANTED_TOOL;
        }

        public static class Serailiser implements TradeOfferFactorySerialiser<SellEnchantedToolFactory> {

            @Override
            public void toJson(JsonObject json, SellEnchantedToolFactory object, JsonSerializationContext context) {
                // TODO - Implement serialisation
            }

            @Override
            public SellEnchantedToolFactory fromJson(JsonObject json, JsonDeserializationContext context) {
                Item item = ShapedRecipe.getItem(json);
                int basePrice = JsonHelper.getInt(json, "base_price");
                int maxUses = JsonHelper.getInt(json, "max_uses", 12);
                int experience = JsonHelper.getInt(json, "experience", 2);
                float priceMultiplier = JsonHelper.getFloat(json, "price_multiplier", 0.05F);
                return new SellEnchantedToolFactory(item, basePrice, maxUses, experience, priceMultiplier);
            }
        }
    }

    class TypeAwareBuyForOneEmeraldFactory extends TradeOffers.TypeAwareBuyForOneEmeraldFactory implements TradeOfferFactory {

        // {map[VillagerType]} x{count} = Emerald x1
        public TypeAwareBuyForOneEmeraldFactory(int count, int maxUses, int experience, Map<VillagerType, Item> map) {
            super(count, maxUses, experience, map);
        }

        @Override
        public TradeOfferFactoryType getType() {
            return TYPE_AWARE_BUY_FOR_ONE_EMERALD;
        }

        public static class Serailiser implements TradeOfferFactorySerialiser<TypeAwareBuyForOneEmeraldFactory> {

            @Override
            public void toJson(JsonObject json, TypeAwareBuyForOneEmeraldFactory object, JsonSerializationContext context) {
                // TODO - Implement serialisation
            }

            @Override
            public TypeAwareBuyForOneEmeraldFactory fromJson(JsonObject json, JsonDeserializationContext context) {
                int count = JsonHelper.getInt(json, "count", 1);
                int maxUses = JsonHelper.getInt(json, "max_uses", 12);
                int experience = JsonHelper.getInt(json, "experience", 2);
                Map<VillagerType, Item> map = JsonHelper.getObject(json, "items").entrySet().stream().map(entry -> new Pair<>(Registry.VILLAGER_TYPE.get(new Identifier((entry.getKey()))), JsonHelper.asItem(entry.getValue(), entry.getKey() + "$item"))).collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
                return new TypeAwareBuyForOneEmeraldFactory(count, maxUses, experience, map);
            }
        }
    }

    class SellPotionHoldingItemFactory extends TradeOffers.SellPotionHoldingItemFactory implements TradeOfferFactory {

        // {arrow} x{secondCount} + Emeralds x{price} = {tippedArrow} x{sellCount}
        public SellPotionHoldingItemFactory(Item arrow, int secondCount, Item tippedArrow, int sellCount, int price, int maxUses, int experience) {
            super(arrow, secondCount, tippedArrow, sellCount, price, maxUses, experience);
        }

        @Override
        public TradeOfferFactoryType getType() {
            return SELL_POTION_HOLDING_ITEM;
        }

        public static class Serailiser implements TradeOfferFactorySerialiser<SellPotionHoldingItemFactory> {

            @Override
            public void toJson(JsonObject json, SellPotionHoldingItemFactory object, JsonSerializationContext context) {
                // TODO - Implement serialisation
            }

            @Override
            public SellPotionHoldingItemFactory fromJson(JsonObject json, JsonDeserializationContext context) {
                Item arrow = JsonHelper.getItem(json, "arrow", Items.ARROW);
                int secondCount = JsonHelper.getInt(json, "second_count", 1);
                Item tippedArrow = JsonHelper.getItem(json, "tipped_arrow", Items.TIPPED_ARROW);
                int sellCount = JsonHelper.getInt(json, "sell_count", secondCount);
                int price = JsonHelper.getInt(json, "price", 1);
                int maxUses = JsonHelper.getInt(json, "max_uses", 12);
                int experience = JsonHelper.getInt(json, "experience", 2);
                return new SellPotionHoldingItemFactory(arrow, secondCount, tippedArrow, sellCount, price, maxUses, experience);
            }
        }
    }

    class EnchantBookFactory extends TradeOffers.EnchantBookFactory implements TradeOfferFactory {

        // Book x1 + Emeralds xRandom (Scaled to Enchant level) = Enchanted Book x1
        public EnchantBookFactory(int experience) {
            super(experience);
        }

        @Override
        public TradeOfferFactoryType getType() {
            return ENCHANT_BOOK;
        }

        public static class Serailiser implements TradeOfferFactorySerialiser<EnchantBookFactory> {

            @Override
            public void toJson(JsonObject json, EnchantBookFactory object, JsonSerializationContext context) {
                // TODO - Implement serialisation
            }

            @Override
            public EnchantBookFactory fromJson(JsonObject json, JsonDeserializationContext context) {
                int experience = JsonHelper.getInt(json, "experience", 2);
                return new EnchantBookFactory(experience);
            }
        }
    }

    class SellMapFactory extends TradeOffers.SellMapFactory implements TradeOfferFactory {

        public SellMapFactory(int price, StructureFeature<?> feature, MapIcon.Type iconType, int maxUses, int experience) {
            super(price, feature, iconType, maxUses, experience);
        }

        @Override
        public TradeOfferFactoryType getType() {
            return SELL_MAP;
        }

        public static class Serailiser implements TradeOfferFactorySerialiser<SellMapFactory> {

            @Override
            public void toJson(JsonObject json, SellMapFactory object, JsonSerializationContext context) {
                // TODO - Implement serialisation
            }

            @Override
            public SellMapFactory fromJson(JsonObject json, JsonDeserializationContext context) {
                int price = JsonHelper.getInt(json, "price", 1);
                StructureFeature<?> feature = Registry.STRUCTURE_FEATURE.get(new Identifier(JsonHelper.getString(json, "feature")));
                MapIcon.Type icon = MapIcon.Type.valueOf(JsonHelper.getString(json, "icon", "TARGET_X").toUpperCase());
                int maxUses = JsonHelper.getInt(json, "max_uses", 12);
                int experience = JsonHelper.getInt(json, "experience", 2);
                return new SellMapFactory(price, feature, icon, maxUses, experience);
            }
        }
    }

    class SellDyedArmorFactory extends TradeOffers.SellDyedArmorFactory implements TradeOfferFactory {

        public SellDyedArmorFactory(Item item, int price, int maxUses, int experience) {
            super(item, price, maxUses, experience);
        }

        @Override
        public TradeOfferFactoryType getType() {
            return SELL_DYED_ARMOR;
        }

        public static class Serailiser implements TradeOfferFactorySerialiser<SellDyedArmorFactory> {

            @Override
            public void toJson(JsonObject json, SellDyedArmorFactory object, JsonSerializationContext context) {
                // TODO - Implement serialisation
            }

            @Override
            public SellDyedArmorFactory fromJson(JsonObject json, JsonDeserializationContext context) {
                Item item = JsonHelper.getItem(json, "item");
                int price = JsonHelper.getInt(json, "price", 1);
                int maxUses = JsonHelper.getInt(json, "max_uses", 12);
                int experience = JsonHelper.getInt(json, "experience", 2);
                return new SellDyedArmorFactory(item, price, maxUses, experience);
            }
        }
    }

}
