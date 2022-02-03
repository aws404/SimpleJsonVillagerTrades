package com.github.aws404.sjvt.trade_offers;

import com.github.aws404.sjvt.SJVTJsonHelpers;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.JsonSerializer;
import net.minecraft.village.TradeOffer;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class SellItemForItemsOfferFactory implements TradeOfferFactory {

    public final ItemStack buy1;
    public final ItemStack buy2;
    public final ItemStack sell;
    public final int maxUses;
    public final int experience;

    public SellItemForItemsOfferFactory(ItemStack buy1, ItemStack buy2, ItemStack sell, int maxUses, int experience) {
        this.buy1 = buy1;
        this.buy2 = buy2;
        this.sell = sell;
        this.maxUses = maxUses;
        this.experience = experience;
    }

    @Override
    public TradeOfferFactoryType getType() {
        return TradeOfferFactoryType.SELL_ITEM_FOR_ITEMS;
    }

    @Nullable
    @Override
    public TradeOffer create(Entity entity, Random random) {
        return new TradeOffer(this.buy1.copy(), this.buy2.copy(), this.sell.copy(), this.maxUses, this.experience, 0.05F);
    }

    public static class Serialiser implements JsonSerializer<SellItemForItemsOfferFactory> {

        @Override
        public void toJson(JsonObject json, SellItemForItemsOfferFactory object, JsonSerializationContext context) {
            json.add("buy_1", SJVTJsonHelpers.itemStackToJson(object.buy1));
            json.add("buy_2", SJVTJsonHelpers.itemStackToJson(object.buy2));
            json.add("sell", SJVTJsonHelpers.itemStackToJson(object.sell));
            json.addProperty("max_uses", object.maxUses);
            json.addProperty("experience", object.experience);
        }

        @Override
        public SellItemForItemsOfferFactory fromJson(JsonObject json, JsonDeserializationContext context) {
            ItemStack buy1 = SJVTJsonHelpers.getItemStack(json, "buy_1");
            ItemStack buy2 = SJVTJsonHelpers.getItemStack(json, "buy_2", ItemStack.EMPTY);
            ItemStack sell = SJVTJsonHelpers.getItemStack(json, "sell");
            int maxUses = JsonHelper.getInt(json, "max_uses", 12);
            int experience = JsonHelper.getInt(json, "experience", 2);
            return new SellItemForItemsOfferFactory(buy1, buy2, sell, maxUses, experience);
        }
    }
}
