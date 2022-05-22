package com.github.aws404.sjvt.trade_offers;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface TradeOfferFactoryTypeHolder {
    TradeOfferFactoryType<?> getType();
}
