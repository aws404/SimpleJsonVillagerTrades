package com.github.aws404.sjvt.trade_offers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.Nullable;

import com.github.aws404.sjvt.api.CodecHelper;
import com.github.aws404.sjvt.api.SerializableTradeOfferFactory;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.VillagerDataContainer;
import net.minecraft.village.VillagerType;

import java.util.Map;
import java.util.Optional;

public class TypeAwareSellItemForItemsOfferFactory implements SerializableTradeOfferFactory {
    public static final Codec<TypeAwareSellItemForItemsOfferFactory> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            CodecHelper.villagerTypeMap(CodecHelper.SIMPLE_ITEM_STACK_CODEC).fieldOf("buy_1").forGetter(typeAwareSellItemForItemsOfferFactory -> typeAwareSellItemForItemsOfferFactory.buyMap1),
            CodecHelper.villagerTypeMap(CodecHelper.SIMPLE_ITEM_STACK_CODEC).optionalFieldOf("buy_2").forGetter(typeAwareSellItemForItemsOfferFactory -> Optional.ofNullable(typeAwareSellItemForItemsOfferFactory.buyMap2)),
            CodecHelper.villagerTypeMap(CodecHelper.SIMPLE_ITEM_STACK_CODEC).fieldOf("sell").forGetter(typeAwareSellItemForItemsOfferFactory -> typeAwareSellItemForItemsOfferFactory.sellMap),
            Codec.INT.optionalFieldOf("max_uses", 12).forGetter(factory -> factory.maxUses),
            Codec.INT.optionalFieldOf("experience", 2).forGetter(factory -> factory.experience)
    ).apply(instance, TypeAwareSellItemForItemsOfferFactory::new));

    public final Map<VillagerType, ItemStack> buyMap1;
    public final Map<VillagerType, ItemStack> buyMap2;
    public final Map<VillagerType, ItemStack> sellMap;
    public final int maxUses;
    public final int experience;

    public TypeAwareSellItemForItemsOfferFactory(Map<VillagerType, ItemStack> buyMap1, Optional<Map<VillagerType, ItemStack>> buyMap2, Map<VillagerType, ItemStack> sellMap, int maxUses, int experience) {
        Registries.VILLAGER_TYPE.stream().filter((villagerType) -> !buyMap1.containsKey(villagerType) || (buyMap2.isPresent() && !buyMap2.get().containsKey(villagerType)) || !sellMap.containsKey(villagerType)).findAny().ifPresent((villagerType) -> {
            throw new IllegalStateException("Missing trade for villager type: " + Registries.VILLAGER_TYPE.getId(villagerType));
        });
        this.buyMap1 = buyMap1;
        this.buyMap2 = buyMap2.orElse(null);
        this.sellMap = sellMap;
        this.maxUses = maxUses;
        this.experience = experience;
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

    @Override
    public TradeOfferFactoryType<?> getType() {
        return TradeOfferFactoryType.TYPE_AWARE_SELL_ITEMS_FOR_ITEM;
    }
}
