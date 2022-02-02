package com.github.aws404.sjvt.mixin;

import com.github.aws404.sjvt.SimpleJsonVillagerTradesMod;
import com.github.aws404.sjvt.trade_offers.TradeOfferManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.TradeOffers;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(WanderingTraderEntity.class)
public abstract class WanderingTraderEntityMixin extends MerchantEntity {

    public WanderingTraderEntityMixin(EntityType<? extends MerchantEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "fillRecipes", at = @At("HEAD"), cancellable = true)
    public void addJsonRecipes(CallbackInfo ci) {
        Optional<TradeOffers.Factory[]> commonOffers = SimpleJsonVillagerTradesMod.TRADE_OFFER_MANAGER.getWanderingTraderOffers(TradeOfferManager.WanderingTraderTradeRarity.COMMON);
        Optional<TradeOffers.Factory[]> rareOffers = SimpleJsonVillagerTradesMod.TRADE_OFFER_MANAGER.getWanderingTraderOffers(TradeOfferManager.WanderingTraderTradeRarity.RARE);
        if (commonOffers.isPresent() && rareOffers.isPresent()) {
            TradeOfferList tradeOfferList = this.getOffers();
            this.fillRecipesFromPool(tradeOfferList, commonOffers.get(), 5);
            this.fillRecipesFromPool(tradeOfferList, rareOffers.get(), 1);
            ci.cancel();
            return;
        }
        SimpleJsonVillagerTradesMod.LOGGER.error("Could not find JSON trades for the Wandering Trader, using default behaviour.");
    }
}
