package com.github.aws404.sjvt.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.github.aws404.sjvt.trade_offers.TradeOfferFactoryType;
import com.github.aws404.sjvt.trade_offers.TradeOfferFactoryTypeHolder;
import com.github.aws404.sjvt.trade_offers.VanillaTradeOfferFactories;

import net.minecraft.village.TradeOffers;

@Mixin(TradeOffers.Factory.class)
public interface TradeOffersFactoryMixin extends TradeOfferFactoryTypeHolder {

    @Override
    default TradeOfferFactoryType<?> getType() {
        return VanillaTradeOfferFactories.getVanillaFactoryCodec((TradeOffers.Factory) this);
    }
}
