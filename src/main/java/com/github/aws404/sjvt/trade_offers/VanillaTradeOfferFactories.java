package com.github.aws404.sjvt.trade_offers;

import com.github.aws404.sjvt.SJVTJsonHelpers;
import com.github.aws404.sjvt.mixin.TradeOffersAccessor;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.item.map.MapIcon;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.JsonSerializer;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerType;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.Map;

public class VanillaTradeOfferFactories {
    public static class BuyForOneEmeraldFactory extends TradeOffers.BuyForOneEmeraldFactory implements TradeOfferFactory {

        public BuyForOneEmeraldFactory(ItemConvertible item, int price, int maxUses, int experience) {
            super(item, price, maxUses, experience);
        }

        @Override
        public TradeOfferFactoryType getType() {
            return TradeOfferFactoryType.BUY_FOR_ONE_EMERALD;
        }

        public static class Serialiser implements JsonSerializer<BuyForOneEmeraldFactory> {

            @Override
            public void toJson(JsonObject json, BuyForOneEmeraldFactory object, JsonSerializationContext context) {
                json.addProperty("item", Registry.ITEM.getId(((TradeOffersAccessor.BuyForOneEmeraldFactoryAccessor)object).getBuy()).toString());
                json.addProperty("price", ((TradeOffersAccessor.BuyForOneEmeraldFactoryAccessor)object).getPrice());
                json.addProperty("max_uses", ((TradeOffersAccessor.BuyForOneEmeraldFactoryAccessor)object).getMaxUses());
                json.addProperty("experience", ((TradeOffersAccessor.BuyForOneEmeraldFactoryAccessor)object).getExperience());
            }

            @Override
            public BuyForOneEmeraldFactory fromJson(JsonObject json, JsonDeserializationContext context) {
                Item item = JsonHelper.getItem(json, "item");
                int price = JsonHelper.getInt(json, "price", 1);
                int maxUses = JsonHelper.getInt(json, "max_uses", 12);
                int experience = JsonHelper.getInt(json, "experience", 2);
                return new BuyForOneEmeraldFactory(item, price, maxUses, experience);
            }
        }
    }

    public static class SellItemFactory extends TradeOffers.SellItemFactory implements TradeOfferFactory {

        public SellItemFactory(Item item, int price, int count, int maxUses, int experience) {
            super(item, price, count, maxUses, experience);
        }

        @Override
        public TradeOfferFactoryType getType() {
            return TradeOfferFactoryType.SELL_ITEM;
        }

        public static class Serialiser implements JsonSerializer<SellItemFactory> {

            @Override
            public void toJson(JsonObject json, SellItemFactory object, JsonSerializationContext context) {
                json.addProperty("item", Registry.ITEM.getId(((TradeOffersAccessor.SellItemFactoryAccessor)object).getSell().getItem()).toString());
                json.addProperty("count", ((TradeOffersAccessor.SellItemFactoryAccessor)object).getCount());
                json.addProperty("price", ((TradeOffersAccessor.SellItemFactoryAccessor)object).getPrice());
                json.addProperty("max_uses", ((TradeOffersAccessor.SellItemFactoryAccessor)object).getMaxUses());
                json.addProperty("experience", ((TradeOffersAccessor.SellItemFactoryAccessor)object).getExperience());
            }

            @Override
            public SellItemFactory fromJson(JsonObject json, JsonDeserializationContext context) {
                Item item = JsonHelper.getItem(json, "item");
                int count = JsonHelper.getInt(json, "count", 1);
                int price = JsonHelper.getInt(json, "price", 1);
                int maxUses = JsonHelper.getInt(json, "max_uses", 12);
                int experience = JsonHelper.getInt(json, "experience", 2);
                return new SellItemFactory(item, price, count, maxUses, experience);
            }
        }
    }

    public static class SellSuspiciousStewFactory extends TradeOffers.SellSuspiciousStewFactory implements TradeOfferFactory {

        public SellSuspiciousStewFactory(StatusEffect effect, int duration, int experience) {
            super(effect, duration, experience);
        }

        @Override
        public TradeOfferFactoryType getType() {
            return TradeOfferFactoryType.SELL_SUSPICIOUS_STEW;
        }

        public static class Serialiser implements JsonSerializer<SellSuspiciousStewFactory> {

            @Override
            public void toJson(JsonObject json, SellSuspiciousStewFactory object, JsonSerializationContext context) {
                json.addProperty("effect", Registry.STATUS_EFFECT.getId(((TradeOffersAccessor.SellSuspiciousStewFactoryAccessor)object).getEffect()).toString());
                json.addProperty("duration", ((TradeOffersAccessor.SellSuspiciousStewFactoryAccessor)object).getDuration());
                json.addProperty("experience", ((TradeOffersAccessor.SellSuspiciousStewFactoryAccessor)object).getExperience());
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

    public static class ProcessItemFactory extends TradeOffers.ProcessItemFactory implements TradeOfferFactory {

        public ProcessItemFactory(ItemConvertible item, int secondCount, int price, Item sellItem, int sellCount, int maxUses, int experience) {
            super(item, secondCount, price, sellItem, sellCount, maxUses, experience);
        }

        @Override
        public TradeOfferFactoryType getType() {
            return TradeOfferFactoryType.PROCESS_ITEM;
        }

        public static class Serialiser implements JsonSerializer<ProcessItemFactory> {

            @Override
            public void toJson(JsonObject json, ProcessItemFactory object, JsonSerializationContext context) {
                json.addProperty("item", Registry.ITEM.getId(((TradeOffersAccessor.ProcessItemFactoryAccessor)object).getSecondBuy().getItem()).toString());
                json.addProperty("second_count", ((TradeOffersAccessor.ProcessItemFactoryAccessor)object).getSecondCount());
                json.addProperty("price", ((TradeOffersAccessor.ProcessItemFactoryAccessor)object).getPrice());
                json.addProperty("sell_item", Registry.ITEM.getId(((TradeOffersAccessor.ProcessItemFactoryAccessor)object).getSell().getItem()).toString());
                json.addProperty("sell_count", ((TradeOffersAccessor.ProcessItemFactoryAccessor)object).getSellCount());
                json.addProperty("max_uses", ((TradeOffersAccessor.ProcessItemFactoryAccessor)object).getMaxUses());
                json.addProperty("experience", ((TradeOffersAccessor.ProcessItemFactoryAccessor)object).getExperience());
            }

            @Override
            public ProcessItemFactory fromJson(JsonObject json, JsonDeserializationContext context) {
                Item item = JsonHelper.getItem(json, "item");
                int secondCount = JsonHelper.getInt(json, "second_count", 1);
                int price = JsonHelper.getInt(json, "price", 1);
                Item sellItem = JsonHelper.getItem(json, "sell_item");
                int sellCount = JsonHelper.getInt(json, "sell_count", 1);
                int maxUses = JsonHelper.getInt(json, "max_uses", 12);
                int experience = JsonHelper.getInt(json, "experience", 2);
                return new ProcessItemFactory(item, secondCount, price, sellItem, sellCount, maxUses, experience);
            }
        }
    }

    public static class SellEnchantedToolFactory extends TradeOffers.SellEnchantedToolFactory implements TradeOfferFactory {

        public SellEnchantedToolFactory(Item item, int basePrice, int maxUses, int experience, float multiplier) {
            super(item, basePrice, maxUses, experience, multiplier);
        }

        @Override
        public TradeOfferFactoryType getType() {
            return TradeOfferFactoryType.SELL_ENCHANTED_TOOL;
        }

        public static class Serialiser implements JsonSerializer<SellEnchantedToolFactory> {

            @Override
            public void toJson(JsonObject json, SellEnchantedToolFactory object, JsonSerializationContext context) {
                json.addProperty("item", Registry.ITEM.getId(((TradeOffersAccessor.SellEnchantedToolFactoryAccessor)object).getTool().getItem()).toString());
                json.addProperty("base_price", ((TradeOffersAccessor.SellEnchantedToolFactoryAccessor)object).getBasePrice());
                json.addProperty("max_uses", ((TradeOffersAccessor.SellEnchantedToolFactoryAccessor)object).getMaxUses());
                json.addProperty("experience", ((TradeOffersAccessor.SellEnchantedToolFactoryAccessor)object).getExperience());
                json.addProperty("price_multiplier", ((TradeOffersAccessor.SellEnchantedToolFactoryAccessor)object).getMultiplier());
            }

            @Override
            public SellEnchantedToolFactory fromJson(JsonObject json, JsonDeserializationContext context) {
                Item item = JsonHelper.getItem(json, "item");
                int basePrice = JsonHelper.getInt(json, "base_price");
                int maxUses = JsonHelper.getInt(json, "max_uses", 12);
                int experience = JsonHelper.getInt(json, "experience", 2);
                float priceMultiplier = JsonHelper.getFloat(json, "price_multiplier", 0.05F);
                return new SellEnchantedToolFactory(item, basePrice, maxUses, experience, priceMultiplier);
            }
        }
    }

    public static class TypeAwareBuyForOneEmeraldFactory extends TradeOffers.TypeAwareBuyForOneEmeraldFactory implements TradeOfferFactory {

        public TypeAwareBuyForOneEmeraldFactory(int count, int maxUses, int experience, Map<VillagerType, Item> map) {
            super(count, maxUses, experience, map);
        }

        @Override
        public TradeOfferFactoryType getType() {
            return TradeOfferFactoryType.TYPE_AWARE_BUY_FOR_ONE_EMERALD;
        }

        public static class Serialiser implements JsonSerializer<TypeAwareBuyForOneEmeraldFactory> {

            @Override
            public void toJson(JsonObject json, TypeAwareBuyForOneEmeraldFactory object, JsonSerializationContext context) {
                json.addProperty("count", ((TradeOffersAccessor.TypeAwareBuyForOneEmeraldFactoryAccessor)object).getCount());
                json.addProperty("max_uses", ((TradeOffersAccessor.TypeAwareBuyForOneEmeraldFactoryAccessor)object).getMaxUses());
                json.addProperty("experience", ((TradeOffersAccessor.TypeAwareBuyForOneEmeraldFactoryAccessor)object).getExperience());
                json.add("items", SJVTJsonHelpers.villagerTypeMapToJson(((TradeOffersAccessor.TypeAwareBuyForOneEmeraldFactoryAccessor)object).getMap(), item -> new JsonPrimitive(Registry.ITEM.getId(item).toString())));
            }

            @Override
            public TypeAwareBuyForOneEmeraldFactory fromJson(JsonObject json, JsonDeserializationContext context) {
                int count = JsonHelper.getInt(json, "count", 1);
                int maxUses = JsonHelper.getInt(json, "max_uses", 12);
                int experience = JsonHelper.getInt(json, "experience", 2);
                Map<VillagerType, Item> map = SJVTJsonHelpers.getVillagerTypeMap(json, "items", JsonHelper::asItem);
                return new TypeAwareBuyForOneEmeraldFactory(count, maxUses, experience, map);
            }
        }
    }

    public static class SellPotionHoldingItemFactory extends TradeOffers.SellPotionHoldingItemFactory implements TradeOfferFactory {

        public SellPotionHoldingItemFactory(Item arrow, int secondCount, Item tippedArrow, int sellCount, int price, int maxUses, int experience) {
            super(arrow, secondCount, tippedArrow, sellCount, price, maxUses, experience);
        }

        @Override
        public TradeOfferFactoryType getType() {
            return TradeOfferFactoryType.SELL_POTION_HOLDING_ITEM;
        }

        public static class Serialiser implements JsonSerializer<SellPotionHoldingItemFactory> {

            @Override
            public void toJson(JsonObject json, SellPotionHoldingItemFactory object, JsonSerializationContext context) {
                json.addProperty("arrow", Registry.ITEM.getId(((TradeOffersAccessor.SellPotionHoldingItemFactoryAccessor)object).getSecondBuy()).toString());
                json.addProperty("second_count", ((TradeOffersAccessor.SellPotionHoldingItemFactoryAccessor)object).getSecondCount());
                json.addProperty("tipped_arrow", Registry.ITEM.getId(((TradeOffersAccessor.SellPotionHoldingItemFactoryAccessor)object).getSell().getItem()).toString());
                json.addProperty("sell_count", ((TradeOffersAccessor.SellPotionHoldingItemFactoryAccessor)object).getSellCount());
                json.addProperty("price", ((TradeOffersAccessor.SellPotionHoldingItemFactoryAccessor)object).getPrice());
                json.addProperty("max_uses", ((TradeOffersAccessor.SellPotionHoldingItemFactoryAccessor)object).getMaxUses());
                json.addProperty("experience", ((TradeOffersAccessor.SellPotionHoldingItemFactoryAccessor)object).getExperience());
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

    public static class EnchantBookFactory extends TradeOffers.EnchantBookFactory implements TradeOfferFactory {

        public EnchantBookFactory(int experience) {
            super(experience);
        }

        @Override
        public TradeOfferFactoryType getType() {
            return TradeOfferFactoryType.ENCHANT_BOOK;
        }

        public static class Serialiser implements JsonSerializer<EnchantBookFactory> {

            @Override
            public void toJson(JsonObject json, EnchantBookFactory object, JsonSerializationContext context) {
                json.addProperty("experience", ((TradeOffersAccessor.EnchantBookFactoryAccessor)object).getExperience());
            }

            @Override
            public EnchantBookFactory fromJson(JsonObject json, JsonDeserializationContext context) {
                int experience = JsonHelper.getInt(json, "experience", 2);
                return new EnchantBookFactory(experience);
            }
        }
    }

    public static class SellMapFactory extends TradeOffers.SellMapFactory implements TradeOfferFactory {

        public SellMapFactory(int price, StructureFeature<?> feature, MapIcon.Type iconType, int maxUses, int experience) {
            super(price, feature, iconType, maxUses, experience);
        }

        @Override
        public TradeOfferFactoryType getType() {
            return TradeOfferFactoryType.SELL_MAP;
        }

        public static class Serialiser implements JsonSerializer<SellMapFactory> {

            @Override
            public void toJson(JsonObject json, SellMapFactory object, JsonSerializationContext context) {
                json.addProperty("price", ((TradeOffersAccessor.SellMapFactoryAccessor)object).getPrice());
                json.addProperty("feature", Registry.STRUCTURE_FEATURE.getId(((TradeOffersAccessor.SellMapFactoryAccessor)object).getStructure()).toString());
                json.addProperty("icon", ((TradeOffersAccessor.SellMapFactoryAccessor)object).getIconType().name().toLowerCase());
                json.addProperty("max_uses", ((TradeOffersAccessor.SellMapFactoryAccessor)object).getMaxUses());
                json.addProperty("experience", ((TradeOffersAccessor.SellMapFactoryAccessor)object).getExperience());
            }

            @Override
            public SellMapFactory fromJson(JsonObject json, JsonDeserializationContext context) {
                int price = JsonHelper.getInt(json, "price", 1);
                StructureFeature<?> feature = Registry.STRUCTURE_FEATURE.getOrEmpty(new Identifier(JsonHelper.getString(json, "feature"))).orElse(StructureFeature.MANSION);
                MapIcon.Type icon = MapIcon.Type.valueOf(JsonHelper.getString(json, "icon", "RED_X").toUpperCase());
                int maxUses = JsonHelper.getInt(json, "max_uses", 12);
                int experience = JsonHelper.getInt(json, "experience", 2);
                return new SellMapFactory(price, feature, icon, maxUses, experience);
            }
        }
    }

    public static class SellDyedArmorFactory extends TradeOffers.SellDyedArmorFactory implements TradeOfferFactory {

        public SellDyedArmorFactory(Item item, int price, int maxUses, int experience) {
            super(item, price, maxUses, experience);
        }

        @Override
        public TradeOfferFactoryType getType() {
            return TradeOfferFactoryType.SELL_DYED_ARMOR;
        }

        public static class Serialiser implements JsonSerializer<SellDyedArmorFactory> {

            @Override
            public void toJson(JsonObject json, SellDyedArmorFactory object, JsonSerializationContext context) {
                json.addProperty("item", Registry.ITEM.getId(((TradeOffersAccessor.SellDyedArmorFactoryAccessor)object).getSell()).toString());
                json.addProperty("price", ((TradeOffersAccessor.SellDyedArmorFactoryAccessor)object).getPrice());
                json.addProperty("max_uses", ((TradeOffersAccessor.SellDyedArmorFactoryAccessor)object).getMaxUses());
                json.addProperty("experience", ((TradeOffersAccessor.SellDyedArmorFactoryAccessor)object).getExperience());
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
