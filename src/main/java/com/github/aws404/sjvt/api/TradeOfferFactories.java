package com.github.aws404.sjvt.api;

import com.github.aws404.sjvt.SimpleJsonVillagerTradesMod;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.TradeOffers;

public class TradeOfferFactories {
    public static final Registry<Codec<? extends TradeOffers.Factory>> TRADE_OFFER_FACTORY_REGISTRY = FabricRegistryBuilder.createSimple(RegistryWithOptionsCodec.getTypeForRegistry(TradeOffers.Factory.class), SimpleJsonVillagerTradesMod.id("trade_offer_factories")).buildAndRegister();
}
