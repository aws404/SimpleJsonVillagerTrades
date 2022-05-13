package com.github.aws404.sjvt.mixin;

import com.github.aws404.sjvt.SimpleJsonVillagerTradesMod;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerData;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin extends MerchantEntity {

    @Shadow public abstract VillagerData getVillagerData();

    public VillagerEntityMixin(EntityType<? extends MerchantEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "fillRecipes", at = @At("HEAD"), cancellable = true)
    public void addJsonRecipes(CallbackInfo ci) {
        Optional<Int2ObjectMap<TradeOffers.Factory[]>> jsonTradeOffer = SimpleJsonVillagerTradesMod.TRADE_OFFER_MANAGER.getVillagerOffers(this.getVillagerData().getProfession());
        if (jsonTradeOffer.isPresent()) {
            Int2ObjectMap<TradeOffers.Factory[]> int2ObjectMap = jsonTradeOffer.get();
            if (!int2ObjectMap.isEmpty()) {
                TradeOffers.Factory[] factories = int2ObjectMap.get(this.getVillagerData().getLevel());
                if (factories != null) {
                    TradeOfferList tradeOfferList = this.getOffers();
                    this.fillRecipesFromPool(tradeOfferList, factories, 2);
                }
            }
            ci.cancel();
            return;
        }
    }
}
