package com.github.aws404.sjvt.datagen;

import com.github.aws404.sjvt.api.datagen.TradeOfferBuilder;
import com.github.aws404.sjvt.api.datagen.TradeOfferDataProvider;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataOutput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.village.TradeOffers;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class HardcodedTradeOfferDataProvider extends TradeOfferDataProvider {
    public HardcodedTradeOfferDataProvider(DataOutput root, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookupFuture) {
        super(root, registryLookupFuture);
    }

    @Override
    protected void generateTradeOffers(Consumer<TradeOfferBuilder> exporter, RegistryWrapper.WrapperLookup lookup) {
        TradeOffers.PROFESSION_TO_LEVELED_TRADE.forEach((profession, int2ObjectMap) -> {
            Identifier id = new Identifier(profession.id());
            TradeOfferBuilder builder = new TradeOfferBuilder(id, id).replace();
            int2ObjectMap.forEach(builder::addTrade);
            exporter.accept(builder);
        });
    }
}
