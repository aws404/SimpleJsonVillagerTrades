package com.github.aws404.sjvt.mixin;

import com.github.aws404.sjvt.api.RegistryWithOptionsCodec;
import com.github.aws404.sjvt.trade_offers.VanillaTradeOfferFactories;
import com.mojang.serialization.Codec;
import net.minecraft.village.TradeOffers;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TradeOffers.Factory.class)
public interface TradeOffersFactoryMixin<T extends TradeOffers.Factory> extends RegistryWithOptionsCodec.CodecHolder<T> {

    @SuppressWarnings("unchecked")
    @Override
    default Codec<T> getCodec() {
        return (Codec<T>) VanillaTradeOfferFactories.getVanillaFactoryCodec((TradeOffers.Factory) this);
    }
}
