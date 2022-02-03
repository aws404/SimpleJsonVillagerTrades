package com.github.aws404.sjvt.trade_offers;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.util.JsonHelper;
import net.minecraft.village.TradeOffer;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class SellItemForItemsOfferFactory implements TradeOfferFactorySerialiser.TradeOfferFactory {

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
        return TradeOfferFactorySerialiser.SELL_ITEM_FOR_ITEMS;
    }

    @Nullable
    @Override
    public TradeOffer create(Entity entity, Random random) {
        return new TradeOffer(this.buy1.copy(), this.buy2.copy(), this.sell.copy(), this.maxUses, this.experience, 0.05F);
    }

    public static class Serialiser implements TradeOfferFactorySerialiser<SellItemForItemsOfferFactory> {

        @Override
        public void toJson(JsonObject json, SellItemForItemsOfferFactory object, JsonSerializationContext context) {

        }

        @Override
        public SellItemForItemsOfferFactory fromJson(JsonObject json, JsonDeserializationContext context) {
            ItemStack buy1 = getItemStack(json, "buy_1");
            ItemStack buy2 = getItemStack(json, "buy_2", ItemStack.EMPTY);
            ItemStack sell = getItemStack(json, "sell");
            int maxUses = JsonHelper.getInt(json, "max_uses", 12);
            int experience = JsonHelper.getInt(json, "experience", 2);
            return new SellItemForItemsOfferFactory(buy1, buy2, sell, maxUses, experience);
        }

        private static ItemStack getItemStack(JsonObject object, String element, ItemStack defaultStack) {
            return object.has(element) ? asItemStack(object.get(element), element): defaultStack;
        }

        private static ItemStack getItemStack(JsonObject object, String element) {
            if (object.has(element)) {
                return asItemStack(object.get(element), element);
            } else {
                throw new JsonSyntaxException("Missing " + element + ", expected to find a ItemStack");
            }
        }

        private static ItemStack asItemStack(JsonElement object, String element) {
            if (object.isJsonPrimitive()) {
                return new ItemStack(JsonHelper.asItem(object, element));
            } else {
                try {
                    NbtCompound compound = StringNbtReader.parse(object.toString());
                    return ItemStack.fromNbt(compound);
                } catch (CommandSyntaxException e) {
                    throw new JsonSyntaxException("Failed to parse " + element + " as NBT, " + e.getMessage(), e);
                }
            }
        }
    }
}
