package com.github.aws404.sjvt.trade_offers;

import com.github.aws404.sjvt.SJVTJsonHelpers;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.JsonSerializer;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.VillagerDataContainer;
import net.minecraft.village.VillagerType;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Random;

public class TypeAwareSellItemForItemsOfferFactory implements TradeOfferFactory {

    public final Map<VillagerType, ItemStack> buyMap1;
    public final Map<VillagerType, ItemStack> buyMap2;
    public final Map<VillagerType, ItemStack> sellMap;
    public final int maxUses;
    public final int experience;

    public TypeAwareSellItemForItemsOfferFactory(Map<VillagerType, ItemStack> buyMap1, Map<VillagerType, ItemStack> buyMap2, Map<VillagerType, ItemStack> sellMap, int maxUses, int experience) {
        Registry.VILLAGER_TYPE.stream().filter((villagerType) -> !buyMap1.containsKey(villagerType) || (buyMap2 != null && !buyMap2.containsKey(villagerType)) || !sellMap.containsKey(villagerType)).findAny().ifPresent((villagerType) -> {
            throw new IllegalStateException("Missing trade for villager type: " + Registry.VILLAGER_TYPE.getId(villagerType));
        });
        this.buyMap1 = buyMap1;
        this.buyMap2 = buyMap2;
        this.sellMap = sellMap;
        this.maxUses = maxUses;
        this.experience = experience;
    }

    @Override
    public TradeOfferFactoryType getType() {
        return TradeOfferFactoryType.TYPE_AWARE_SELL_ITEM_FOR_ITEMS;
    }

    @Nullable
    @Override
    public TradeOffer create(Entity entity, Random random) {
        if (entity instanceof VillagerDataContainer) {
            ItemStack buy1 = this.buyMap1.get(((VillagerDataContainer)entity).getVillagerData().getType()).copy();
            ItemStack buy2 = this.buyMap2 != null ? this.buyMap2.get(((VillagerDataContainer)entity).getVillagerData().getType()).copy() : ItemStack.EMPTY;
            ItemStack sell = this.sellMap.get(((VillagerDataContainer)entity).getVillagerData().getType()).copy();
            return new TradeOffer(buy1, buy2, sell, this.maxUses, this.experience, 0.05F);
        } else {
            return null;
        }
    }

    public static class Serialiser implements JsonSerializer<TypeAwareSellItemForItemsOfferFactory> {

        @Override
        public void toJson(JsonObject json, TypeAwareSellItemForItemsOfferFactory object, JsonSerializationContext context) {
            json.add("buy_1", SJVTJsonHelpers.villagerTypeMapToJson(object.buyMap1, SJVTJsonHelpers::itemStackToJson));
            if (object.buyMap2 != null) {
                json.add("buy_2", SJVTJsonHelpers.villagerTypeMapToJson(object.buyMap2, SJVTJsonHelpers::itemStackToJson));
            }
            json.add("sell", SJVTJsonHelpers.villagerTypeMapToJson(object.sellMap, SJVTJsonHelpers::itemStackToJson));
            json.addProperty("max_uses", object.maxUses);
            json.addProperty("experience", object.experience);
        }

        @Override
        public TypeAwareSellItemForItemsOfferFactory fromJson(JsonObject json, JsonDeserializationContext context) {
            Map<VillagerType, ItemStack> buyMap1 = SJVTJsonHelpers.getVillagerTypeMap(json, "buy_1", jsonElement -> SJVTJsonHelpers.asItemStack(jsonElement, "VillagerType$ItemStack"));
            Map<VillagerType, ItemStack> buyMap2 = SJVTJsonHelpers.getVillagerTypeMap(json, "buy_2", null, jsonElement -> SJVTJsonHelpers.asItemStack(jsonElement, "VillagerType$ItemStack"));
            Map<VillagerType, ItemStack> sellMap = SJVTJsonHelpers.getVillagerTypeMap(json, "sell", jsonElement -> SJVTJsonHelpers.asItemStack(jsonElement, "VillagerType$ItemStack"));
            int maxUses = JsonHelper.getInt(json, "max_uses", 12);
            int experience = JsonHelper.getInt(json, "experience", 2);
            return new TypeAwareSellItemForItemsOfferFactory(buyMap1, buyMap2, sellMap, maxUses, experience);
        }
    }
}
