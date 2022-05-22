package com.github.aws404.sjvt.api;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;

import com.github.aws404.sjvt.DefaultMapCodec;

import net.minecraft.item.ItemStack;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.VillagerType;

import java.util.Map;

public class CodecHelper {

    /**
     * An ItemStack codec to allow both explicit creation or just an item Identifier
     */
    public static final Codec<ItemStack> SIMPLE_ITEM_STACK_CODEC = Codec.either(
            ItemStack.CODEC,
            Registry.ITEM.getCodec().xmap(ItemStack::new, ItemStack::getItem)
    ).xmap(either -> either.left().orElseGet(() -> either.right().orElse(ItemStack.EMPTY)), stack -> stack.hasNbt() ? Either.left(stack) : Either.right(stack));

    /**
     * Create a codec for a villager type -> element map, also allows the 'default' key to use for undefined villager types.
     * @param elementCodec a codec for the element of the map
     */
    public static <T> Codec<Map<VillagerType, T>> villagerTypeMap(Codec<T> elementCodec) {
        return DefaultMapCodec.of(Registry.VILLAGER_TYPE.getCodec(), elementCodec, Registry.VILLAGER_TYPE);
    }

    /**
     * Create a codec for the specified enum, will be case-insensitive.
     * @param enumClass the class of the enum to use
     */
    public static <T extends Enum<T>> Codec<T> forEnum(Class<T> enumClass) {
        return Codecs.orCompressed(Codecs.idChecked(Enum::name, value -> {
            try {
                return Enum.valueOf(enumClass, value.toUpperCase());
            } catch (IllegalArgumentException e) {
                return null;
            }
        }), Codecs.rawIdChecked(Enum::ordinal, ordinal -> ordinal >= 0 && ordinal < enumClass.getEnumConstants().length ? enumClass.getEnumConstants()[ordinal] : null, -1));
    }
}
