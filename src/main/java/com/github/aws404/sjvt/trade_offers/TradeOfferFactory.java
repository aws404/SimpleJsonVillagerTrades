package com.github.aws404.sjvt.trade_offers;

import net.minecraft.village.TradeOffers;

public interface TradeOfferFactory extends TradeOffers.Factory {
    TradeOfferFactoryType getType();
}
