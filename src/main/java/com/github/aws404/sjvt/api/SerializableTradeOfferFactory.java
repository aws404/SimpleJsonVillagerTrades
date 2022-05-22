package com.github.aws404.sjvt.api;

import com.github.aws404.sjvt.trade_offers.TradeOfferFactoryTypeHolder;

import net.minecraft.village.TradeOffers;

/**
 * The interface that custom trade offers should implement.
 */
public interface SerializableTradeOfferFactory extends TradeOffers.Factory, TradeOfferFactoryTypeHolder {
}
