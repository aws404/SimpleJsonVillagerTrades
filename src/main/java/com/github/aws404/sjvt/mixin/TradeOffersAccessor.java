package com.github.aws404.sjvt.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapIcon;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerType;
import net.minecraft.world.gen.structure.Structure;

import java.util.Map;

@Mixin(TradeOffers.class)
public interface TradeOffersAccessor {

    @Mixin(TradeOffers.BuyForOneEmeraldFactory.class)
    interface BuyForOneEmeraldFactoryAccessor {

        @Accessor
        Item getBuy();

        @Accessor
        int getPrice();

        @Accessor
        int getMaxUses();

        @Accessor
        int getExperience();
    }

    @Mixin(TradeOffers.SellItemFactory.class)
    interface SellItemFactoryAccessor {

        @Accessor
        ItemStack getSell();

        @Accessor
        int getPrice();

        @Accessor
        int getCount();

        @Accessor
        int getMaxUses();

        @Accessor
        int getExperience();
    }

    @Mixin(TradeOffers.SellSuspiciousStewFactory.class)
    interface SellSuspiciousStewFactoryAccessor {

        @Accessor
        StatusEffect getEffect();

        @Accessor
        int getDuration();

        @Accessor
        int getExperience();
    }

    @Mixin(TradeOffers.ProcessItemFactory.class)
    interface ProcessItemFactoryAccessor {

        @Accessor
        ItemStack getSecondBuy();

        @Accessor
        int getSecondCount();

        @Accessor
        int getPrice();

        @Accessor
        ItemStack getSell();

        @Accessor
        int getSellCount();

        @Accessor
        int getMaxUses();

        @Accessor
        int getExperience();
    }

    @Mixin(TradeOffers.SellEnchantedToolFactory.class)
    interface SellEnchantedToolFactoryAccessor {

        @Accessor
        ItemStack getTool();

        @Accessor
        int getBasePrice();

        @Accessor
        int getMaxUses();

        @Accessor
        int getExperience();

        @Accessor
        float getMultiplier();
    }

    @Mixin(TradeOffers.TypeAwareBuyForOneEmeraldFactory.class)
    interface TypeAwareBuyForOneEmeraldFactoryAccessor {

        @Accessor
        Map<VillagerType, Item> getMap();

        @Accessor
        int getCount();

        @Accessor
        int getMaxUses();

        @Accessor
        int getExperience();
    }

    @Mixin(TradeOffers.SellPotionHoldingItemFactory.class)
    interface SellPotionHoldingItemFactoryAccessor {

        @Accessor
        ItemStack getSell();

        @Accessor
        int getSellCount();

        @Accessor
        int getPrice();

        @Accessor
        int getMaxUses();

        @Accessor
        int getExperience();

        @Accessor
        Item getSecondBuy();

        @Accessor
        int getSecondCount();
    }

    @Mixin(TradeOffers.EnchantBookFactory.class)
    interface EnchantBookFactoryAccessor {

        @Accessor
        int getExperience();
    }

    @Mixin(TradeOffers.SellMapFactory.class)
    interface SellMapFactoryAccessor {

        @Accessor
        int getPrice();

        @Accessor
        TagKey<Structure> getStructure();

        @Accessor
        String getNameKey();

        @Accessor
        MapIcon.Type getIconType();

        @Accessor
        int getMaxUses();

        @Accessor
        int getExperience();
    }

    @Mixin(TradeOffers.SellDyedArmorFactory.class)
    interface SellDyedArmorFactoryAccessor {

        @Accessor
        Item getSell();

        @Accessor
        int getPrice();

        @Accessor
        int getMaxUses();

        @Accessor
        int getExperience();
    }
}
