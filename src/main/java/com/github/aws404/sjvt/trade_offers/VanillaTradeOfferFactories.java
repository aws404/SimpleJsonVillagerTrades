package com.github.aws404.sjvt.trade_offers;

import com.github.aws404.sjvt.api.CodecHelper;
import com.github.aws404.sjvt.mixin.TradeOffersAccessor;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.Items;
import net.minecraft.item.map.MapIcon;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.TradeOffers;

public class VanillaTradeOfferFactories {
    public static final Codec<TradeOffers.BuyForOneEmeraldFactory> BUY_FOR_ONE_EMERALD = RecordCodecBuilder.create(instance -> instance.group(
            Registry.ITEM.getCodec().fieldOf("item").forGetter(factory -> ((TradeOffersAccessor.BuyForOneEmeraldFactoryAccessor) factory).getBuy()),
            Codec.INT.fieldOf("price").forGetter(factory -> ((TradeOffersAccessor.BuyForOneEmeraldFactoryAccessor) factory).getPrice()),
            Codec.INT.optionalFieldOf("max_uses", 12).forGetter(factory -> ((TradeOffersAccessor.BuyForOneEmeraldFactoryAccessor) factory).getMaxUses()),
            Codec.INT.optionalFieldOf("experience", 2).forGetter(factory -> ((TradeOffersAccessor.BuyForOneEmeraldFactoryAccessor) factory).getExperience())
    ).apply(instance, TradeOffers.BuyForOneEmeraldFactory::new));

    public static final Codec<TradeOffers.SellItemFactory> SELL_ITEM = RecordCodecBuilder.create(instance -> instance.group(
            Registry.ITEM.getCodec().fieldOf("item").forGetter(factory -> ((TradeOffersAccessor.SellItemFactoryAccessor) factory).getSell().getItem()),
            Codec.INT.optionalFieldOf("price", 1).forGetter(factory -> ((TradeOffersAccessor.SellItemFactoryAccessor) factory).getPrice()),
            Codec.INT.optionalFieldOf("count", 1).forGetter(factory -> ((TradeOffersAccessor.SellItemFactoryAccessor) factory).getCount()),
            Codec.INT.optionalFieldOf("max_uses", 12).forGetter(factory -> ((TradeOffersAccessor.SellItemFactoryAccessor) factory).getMaxUses()),
            Codec.INT.optionalFieldOf("experience", 2).forGetter(factory -> ((TradeOffersAccessor.SellItemFactoryAccessor) factory).getExperience())
    ).apply(instance, TradeOffers.SellItemFactory::new));


    public static final Codec<TradeOffers.SellSuspiciousStewFactory> SELL_SUSPICIOUS_STEW = RecordCodecBuilder.create(instance -> instance.group(
            Registry.STATUS_EFFECT.getCodec().fieldOf("effect").forGetter(factory -> ((TradeOffersAccessor.SellSuspiciousStewFactoryAccessor) factory).getEffect()),
            Codec.INT.fieldOf("duration").forGetter(factory -> ((TradeOffersAccessor.SellSuspiciousStewFactoryAccessor) factory).getDuration()),
            Codec.INT.optionalFieldOf("experience", 2).forGetter(factory -> ((TradeOffersAccessor.SellSuspiciousStewFactoryAccessor) factory).getExperience())
    ).apply(instance, TradeOffers.SellSuspiciousStewFactory::new));

    public static final Codec<TradeOffers.ProcessItemFactory> PROCESS_ITEM = RecordCodecBuilder.create(instance -> instance.group(
            Registry.ITEM.getCodec().fieldOf("item").forGetter(factory -> ((TradeOffersAccessor.ProcessItemFactoryAccessor) factory).getSecondBuy().getItem()),
            Codec.INT.optionalFieldOf("second_count", 1).forGetter(factory -> ((TradeOffersAccessor.ProcessItemFactoryAccessor) factory).getSecondCount()),
            Codec.INT.optionalFieldOf("price", 1).forGetter(factory -> ((TradeOffersAccessor.ProcessItemFactoryAccessor) factory).getPrice()),
            Registry.ITEM.getCodec().fieldOf("sell_item").forGetter(factory -> ((TradeOffersAccessor.ProcessItemFactoryAccessor) factory).getSell().getItem()),
            Codec.INT.optionalFieldOf("sell_count", 1).forGetter(factory -> ((TradeOffersAccessor.ProcessItemFactoryAccessor) factory).getSellCount()),
            Codec.INT.optionalFieldOf("max_uses", 12).forGetter(factory -> ((TradeOffersAccessor.ProcessItemFactoryAccessor) factory).getMaxUses()),
            Codec.INT.optionalFieldOf("experience", 2).forGetter(factory -> ((TradeOffersAccessor.ProcessItemFactoryAccessor) factory).getExperience())
    ).apply(instance, TradeOffers.ProcessItemFactory::new));

    public static final Codec<TradeOffers.SellEnchantedToolFactory> SELL_ENCHANTED_TOOL = RecordCodecBuilder.create(instance -> instance.group(
            Registry.ITEM.getCodec().fieldOf("item").forGetter(factory -> ((TradeOffersAccessor.SellEnchantedToolFactoryAccessor) factory).getTool().getItem()),
            Codec.INT.optionalFieldOf("base_price", 1).forGetter(factory -> ((TradeOffersAccessor.SellEnchantedToolFactoryAccessor) factory).getBasePrice()),
            Codec.INT.optionalFieldOf("max_uses", 12).forGetter(factory -> ((TradeOffersAccessor.SellEnchantedToolFactoryAccessor) factory).getMaxUses()),
            Codec.INT.optionalFieldOf("experience", 2).forGetter(factory -> ((TradeOffersAccessor.SellEnchantedToolFactoryAccessor) factory).getExperience()),
            Codec.FLOAT.optionalFieldOf("price_multiplier", 0.05F).forGetter(factory -> ((TradeOffersAccessor.SellEnchantedToolFactoryAccessor) factory).getMultiplier())
    ).apply(instance, TradeOffers.SellEnchantedToolFactory::new));

    public static final Codec<TradeOffers.TypeAwareBuyForOneEmeraldFactory> TYPE_AWARE_BUY_FOR_ONE_EMERALD = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("count", 1).forGetter(factory -> ((TradeOffersAccessor.TypeAwareBuyForOneEmeraldFactoryAccessor) factory).getCount()),
            Codec.INT.optionalFieldOf("max_uses", 12).forGetter(factory -> ((TradeOffersAccessor.TypeAwareBuyForOneEmeraldFactoryAccessor) factory).getMaxUses()),
            Codec.INT.optionalFieldOf("experience", 2).forGetter(factory -> ((TradeOffersAccessor.TypeAwareBuyForOneEmeraldFactoryAccessor) factory).getExperience()),
            CodecHelper.villagerTypeMap(Registry.ITEM.getCodec()).fieldOf("items").forGetter(factory -> ((TradeOffersAccessor.TypeAwareBuyForOneEmeraldFactoryAccessor) factory).getMap())
    ).apply(instance, TradeOffers.TypeAwareBuyForOneEmeraldFactory::new));

    public static final Codec<TradeOffers.SellPotionHoldingItemFactory> SELL_POTION_HOLDING_ITEM = RecordCodecBuilder.create(instance -> instance.group(
            Registry.ITEM.getCodec().optionalFieldOf("arrow", Items.ARROW).forGetter(factory -> ((TradeOffersAccessor.SellPotionHoldingItemFactoryAccessor) factory).getSecondBuy()),
            Codec.INT.optionalFieldOf("second_count", 1).forGetter(factory -> ((TradeOffersAccessor.SellPotionHoldingItemFactoryAccessor) factory).getSecondCount()),
            Registry.ITEM.getCodec().optionalFieldOf("tipped_arrow", Items.TIPPED_ARROW).forGetter(factory -> ((TradeOffersAccessor.SellPotionHoldingItemFactoryAccessor) factory).getSell().getItem()),
            Codec.INT.fieldOf("price").forGetter(factory -> ((TradeOffersAccessor.SellPotionHoldingItemFactoryAccessor) factory).getPrice()),
            Codec.INT.optionalFieldOf("sell_count", 1).forGetter(factory -> ((TradeOffersAccessor.SellPotionHoldingItemFactoryAccessor) factory).getSellCount()),
            Codec.INT.optionalFieldOf("max_uses", 12).forGetter(factory -> ((TradeOffersAccessor.SellPotionHoldingItemFactoryAccessor) factory).getMaxUses()),
            Codec.INT.optionalFieldOf("experience", 2).forGetter(factory -> ((TradeOffersAccessor.SellPotionHoldingItemFactoryAccessor) factory).getExperience())
    ).apply(instance, TradeOffers.SellPotionHoldingItemFactory::new));

    public static final Codec<TradeOffers.EnchantBookFactory> ENCHANT_BOOK = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("experience", 2).forGetter(factory -> ((TradeOffersAccessor.EnchantBookFactoryAccessor) factory).getExperience())
    ).apply(instance, TradeOffers.EnchantBookFactory::new));

    public static final Codec<TradeOffers.SellMapFactory> SELL_MAP = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("price", 1).forGetter(factory -> ((TradeOffersAccessor.SellMapFactoryAccessor) factory).getPrice()),
            Identifier.CODEC.fieldOf("feature_tag").xmap(identifier -> TagKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, identifier), TagKey::id).forGetter(factory -> ((TradeOffersAccessor.SellMapFactoryAccessor) factory).getStructure()),
            Codec.STRING.fieldOf("name_key").forGetter(factory -> ((TradeOffersAccessor.SellMapFactoryAccessor) factory).getNameKey()),
            CodecHelper.forEnum(MapIcon.Type.class).optionalFieldOf("icon", MapIcon.Type.TARGET_X).forGetter(factory -> ((TradeOffersAccessor.SellMapFactoryAccessor) factory).getIconType()),
            Codec.INT.optionalFieldOf("max_uses", 12).forGetter(factory -> ((TradeOffersAccessor.SellMapFactoryAccessor) factory).getMaxUses()),
            Codec.INT.optionalFieldOf("experience", 2).forGetter(factory -> ((TradeOffersAccessor.SellMapFactoryAccessor) factory).getExperience())
    ).apply(instance, TradeOffers.SellMapFactory::new));

    public static final Codec<TradeOffers.SellDyedArmorFactory> SELL_DYED_ARMOR = RecordCodecBuilder.create(instance -> instance.group(
            Registry.ITEM.getCodec().fieldOf("item").forGetter(factory -> ((TradeOffersAccessor.SellDyedArmorFactoryAccessor) factory).getSell()),
            Codec.INT.optionalFieldOf("price", 1).forGetter(factory -> ((TradeOffersAccessor.SellDyedArmorFactoryAccessor) factory).getPrice()),
            Codec.INT.optionalFieldOf("max_uses", 12).forGetter(factory -> ((TradeOffersAccessor.SellDyedArmorFactoryAccessor) factory).getMaxUses()),
            Codec.INT.optionalFieldOf("experience", 2).forGetter(factory -> ((TradeOffersAccessor.SellDyedArmorFactoryAccessor) factory).getExperience())
    ).apply(instance, TradeOffers.SellDyedArmorFactory::new));

    public static Codec<? extends TradeOffers.Factory> getVanillaFactoryCodec(TradeOffers.Factory factory) {
        if (factory instanceof TradeOffers.BuyForOneEmeraldFactory) {
            return VanillaTradeOfferFactories.BUY_FOR_ONE_EMERALD;
        }
        if (factory instanceof TradeOffers.SellItemFactory) {
            return VanillaTradeOfferFactories.SELL_ITEM;
        }
        if (factory instanceof TradeOffers.SellSuspiciousStewFactory) {
            return VanillaTradeOfferFactories.SELL_SUSPICIOUS_STEW;
        }
        if (factory instanceof TradeOffers.ProcessItemFactory) {
            return VanillaTradeOfferFactories.PROCESS_ITEM;
        }
        if (factory instanceof TradeOffers.SellEnchantedToolFactory) {
            return VanillaTradeOfferFactories.SELL_ENCHANTED_TOOL;
        }
        if (factory instanceof TradeOffers.TypeAwareBuyForOneEmeraldFactory) {
            return VanillaTradeOfferFactories.TYPE_AWARE_BUY_FOR_ONE_EMERALD;
        }
        if (factory instanceof TradeOffers.SellPotionHoldingItemFactory) {
            return VanillaTradeOfferFactories.SELL_POTION_HOLDING_ITEM;
        }
        if (factory instanceof TradeOffers.EnchantBookFactory) {
            return VanillaTradeOfferFactories.ENCHANT_BOOK;
        }
        if (factory instanceof TradeOffers.SellMapFactory) {
            return VanillaTradeOfferFactories.SELL_MAP;
        }
        if (factory instanceof TradeOffers.SellDyedArmorFactory) {
            return VanillaTradeOfferFactories.SELL_DYED_ARMOR;
        }

        throw new IllegalStateException("Could not find codec for factory " + factory.getClass());
    }
}
