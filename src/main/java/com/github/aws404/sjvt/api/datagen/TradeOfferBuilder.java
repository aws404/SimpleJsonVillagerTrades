package com.github.aws404.sjvt.api.datagen;

import com.github.aws404.sjvt.TradeOfferManager;

import net.minecraft.util.Identifier;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TradeOfferBuilder {
    private final Identifier id;
    private final Identifier profession;
    private boolean replace = false;
    private final Map<Integer, List<TradeOffers.Factory>> trades = new HashMap<>();

    public TradeOfferBuilder(Identifier id, Identifier profession) {
        this.id = id;
        this.profession = profession;
    }

    public TradeOfferBuilder(Identifier id, VillagerProfession profession) {
        this(id, new Identifier(profession.id()));
    }

    public TradeOfferBuilder replace() {
        this.replace = true;
        return this;
    }

    public TradeOfferBuilder addTrade(TradeOfferManager.MerchantLevel level, TradeOffers.Factory... factory) {
        return this.addTrade(level.id, factory);
    }

    public TradeOfferBuilder addTrade(int level, TradeOffers.Factory... factory) {
        this.trades.computeIfAbsent(level, integer -> new ArrayList<>());
        for (TradeOffers.Factory factory1 : factory) {
            this.trades.get(level).add(factory1);
        }
        return this;
    }

    public TradeOfferManager.VillagerTrades build() {
        return new TradeOfferManager.VillagerTrades(this.profession, this.replace, this.trades);
    }

    public Identifier getId() {
        return this.id;
    }
}
