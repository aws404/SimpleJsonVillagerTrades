package com.github.aws404.sjvt.api;

import net.minecraft.village.TradeOffers;

/**
 * The interface that custom trade offers should implement.
 * @param <T> this class
 */
public interface SerializableTradeOfferFactory<T extends TradeOffers.Factory> extends TradeOffers.Factory, RegistryWithOptionsCodec.CodecHolder<T> {
}
