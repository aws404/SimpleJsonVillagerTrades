package com.github.aws404.sjvt.trade_offers;

import net.minecraft.util.JsonSerializableType;
import net.minecraft.util.JsonSerializer;

public class TradeOfferFactoryType extends JsonSerializableType<TradeOfferFactorySerialiser.TradeOfferFactory> {
    public TradeOfferFactoryType(JsonSerializer<? extends TradeOfferFactorySerialiser.TradeOfferFactory> jsonSerializer) {
        super(jsonSerializer);
    }
}
