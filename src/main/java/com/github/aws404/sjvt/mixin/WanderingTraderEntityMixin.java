package com.github.aws404.sjvt.mixin;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.github.aws404.sjvt.SimpleJsonVillagerTradesMod;

import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.village.TradeOffers;

@Mixin(WanderingTraderEntity.class)
public class WanderingTraderEntityMixin {

    @SuppressWarnings("unchecked")
    @Redirect(method = "fillRecipes", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;get(I)Ljava/lang/Object;"))
    public <T> T extra_professions_redirectTradeMap(Int2ObjectMap<T> int2ObjectMap, int i) {
        return (T) SimpleJsonVillagerTradesMod.TRADE_OFFER_MANAGER.getWanderingTraderOffers(i).orElse(new TradeOffers.Factory[0]);
    }
}
