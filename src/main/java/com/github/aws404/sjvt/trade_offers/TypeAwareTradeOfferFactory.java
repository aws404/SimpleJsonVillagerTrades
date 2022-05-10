package com.github.aws404.sjvt.trade_offers;

import com.github.aws404.sjvt.TradeOfferManager;
import com.github.aws404.sjvt.api.CodecHelper;
import com.github.aws404.sjvt.api.SerializableTradeOfferFactory;
import com.github.aws404.sjvt.api.TradeOfferFactories;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.village.*;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Random;

public record TypeAwareTradeOfferFactory(Map<VillagerType, TradeOffers.Factory> tradeOffers) implements SerializableTradeOfferFactory<TypeAwareTradeOfferFactory> {
    public static final Codec<TypeAwareTradeOfferFactory> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            CodecHelper.villagerTypeMap(TradeOfferFactories.CODEC).fieldOf("trades").forGetter(TypeAwareTradeOfferFactory::tradeOffers)
    ).apply(instance, TypeAwareTradeOfferFactory::new));

    @Override
    public Codec<TypeAwareTradeOfferFactory> getCodec() {
        return CODEC;
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
