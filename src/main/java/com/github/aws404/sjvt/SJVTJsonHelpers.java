package com.github.aws404.sjvt;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.VillagerType;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SJVTJsonHelpers {
    public static ItemStack getItemStack(JsonObject object, String element, ItemStack defaultStack) {
        return object.has(element) ? asItemStack(object.get(element), element): defaultStack;
    }

    public static ItemStack getItemStack(JsonObject object, String element) {
        if (object.has(element)) {
            return asItemStack(object.get(element), element);
        } else {
            throw new JsonSyntaxException("Missing " + element + ", expected to find a ItemStack");
        }
    }

    public static ItemStack asItemStack(JsonElement object, String element) {
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

    public static JsonObject itemStackToJson(ItemStack stack) {
        return JsonHelper.deserialize(stack.writeNbt(new NbtCompound()).toString());
    }

    public static <T> Map<VillagerType, T> getVillagerTypeMap(JsonObject object, String name, Map<VillagerType, T> defaultMap, BiFunction<JsonElement, String, T> mapper) {
        if (object.has(name)) {
            if (object.get(name).isJsonObject()) {
                return asVillagerTypeMap(object.get(name).getAsJsonObject(), name, mapper);
            } else {
                throw new JsonSyntaxException("Expected " + name + " to be a villager type map, was " + JsonHelper.getType(object));
            }
        } else {
            return defaultMap;
        }
    }

    public static <T> Map<VillagerType, T> getVillagerTypeMap(JsonObject object, String name, BiFunction<JsonElement, String, T> mapper) {
        if (object.has(name)) {
            if (object.get(name).isJsonObject()) {
                return asVillagerTypeMap(object.get(name).getAsJsonObject(), name, mapper);
            } else {
                throw new JsonSyntaxException("Expected " + name + " to be a villager type map, was " + JsonHelper.getType(object));
            }
        } else {
            throw new JsonSyntaxException("Missing " + name + ", expected to find a villager type map");
        }
    }

    public static <T> Map<VillagerType, T> asVillagerTypeMap(JsonObject object, String element, BiFunction<JsonElement, String, T> mapper) {
        T defaultObject = object.has("default") ? mapper.apply(object.get("default"), element + "$default") : null;
        return Registry.VILLAGER_TYPE.stream().map(villagerType -> {
            Identifier id = Registry.VILLAGER_TYPE.getId(villagerType);
            if (defaultObject == null && !object.has(id.toString())) {
                throw new JsonSyntaxException("Missing villager type " + id + " and no default provided in map " + element);
            }
            return new Pair<>(villagerType, object.has(id.toString()) ? mapper.apply(object.get(id.toString()), element + "$" + id) : defaultObject);
        }).collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
    }

    public static <T> JsonObject villagerTypeMapToJson(Map<VillagerType, T> map, Function<T, JsonElement> mapper) {
        return map.entrySet().stream().collect(
                JsonObject::new,
                (jsonObj, entry) -> jsonObj.add(Registry.VILLAGER_TYPE.getId(entry.getKey()).toString(), mapper.apply(entry.getValue())),
                (jsonObj1, jsonObj2) -> jsonObj2.entrySet().forEach(entry -> jsonObj1.add(entry.getKey(), entry.getValue()))
        );
    }
}
