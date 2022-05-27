package com.github.aws404.sjvt.trade_offers;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.Nullable;

import com.github.aws404.sjvt.api.CodecHelper;
import com.github.aws404.sjvt.api.SerializableTradeOfferFactory;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerDataContainer;
import net.minecraft.village.VillagerType;

import java.util.Map;

public record TypeAwareTradeOfferFactory(Map<VillagerType, TradeOffers.Factory> tradeOffers) implements SerializableTradeOfferFactory {
    public static final Codec<TypeAwareTradeOfferFactory> CODEC = CodecHelper.villagerTypeMap(TradeOfferFactoryType.CODEC).fieldOf("trades").xmap(TypeAwareTradeOfferFactory::new, TypeAwareTradeOfferFactory::tradeOffers).codec();

    @Override
    public TradeOfferFactoryType<?> getType() {
        return TradeOfferFactoryType.TYPE_AWARE;
    }

    @Nullable
    @Override
    public TradeOffer create(Entity entity, Random random) {
        if (entity instanceof VillagerDataContainer villager) {
            return this.tradeOffers.get(villager.getVillagerData().getType()).create(entity, random);
        }
        return null;
    }
}
