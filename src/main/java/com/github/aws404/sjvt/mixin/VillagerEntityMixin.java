package com.github.aws404.sjvt.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.github.aws404.sjvt.SimpleJsonVillagerTradesMod;

import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.village.VillagerProfession;

import java.util.Map;

@Mixin(VillagerEntity.class)
public class VillagerEntityMixin {

    @SuppressWarnings("unchecked")
    @Redirect(method = "fillRecipes", at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"))
    public <K, V> V extra_professions_redirectTradeMap(Map<K, V> map, Object key) {
        return (V) SimpleJsonVillagerTradesMod.TRADE_OFFER_MANAGER.getVillagerOffers((VillagerProfession) key).orElse(null);
    }
}
