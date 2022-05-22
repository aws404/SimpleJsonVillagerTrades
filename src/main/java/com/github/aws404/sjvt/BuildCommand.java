package com.github.aws404.sjvt;

import com.google.gson.JsonElement;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.util.UnsafeByteArrayOutputStream;

import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.TradeOffers;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BuildCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal(SimpleJsonVillagerTradesMod.MOD_ID)
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("build")
                        .executes(context -> {
                            RuntimeResourcePack resourcePack = RuntimeResourcePack.create(new Identifier("json_villager_trades"));

                            TradeOffers.PROFESSION_TO_LEVELED_TRADE.forEach((profession, int2ObjectMap) -> encodeToResourcePack(Registry.VILLAGER_PROFESSION.getId(profession), int2ObjectMap, resourcePack));
                            encodeToResourcePack(TradeOfferManager.WANDERING_TRADER_PROFESSION_ID, TradeOffers.WANDERING_TRADER_TRADES, resourcePack);

                            Path file = FabricLoader.getInstance().getGameDir().resolve("sjvt_generated_resource_pack").toAbsolutePath();
                            resourcePack.dump(file);
                            context.getSource().sendFeedback(Text.literal("").append(Text.literal("Hardcoded trades exported to ")).append(Text.literal(file.toString()).formatted(Formatting.GRAY).styled(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, file.toString())))), false);

                            return 1;
                        })
                )
        );
    }

    private static void encodeToResourcePack(Identifier id, Int2ObjectMap<TradeOffers.Factory[]> int2ObjectMap, RuntimeResourcePack pack) {
        Map<Integer, List<TradeOffers.Factory>> tradeMap = int2ObjectMap.int2ObjectEntrySet().stream().map(entry -> Pair.of(entry.getIntKey(), List.of(entry.getValue()))).collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
        TradeOfferManager.VillagerTrades trades = new TradeOfferManager.VillagerTrades(id, false, tradeMap);
        JsonElement tradeJson = TradeOfferManager.VillagerTrades.CODEC.encodeStart(JsonOps.INSTANCE, trades).getOrThrow(false, s -> SimpleJsonVillagerTradesMod.LOGGER.error("Could not serialize hardcoded trades for profession {}: {}", id, s));
        pack.addData(new Identifier(id.getNamespace(), "trade_offers/" + id.getPath() + ".json"), serialize(tradeJson));
    }

    private static byte[] serialize(Object object) {
        UnsafeByteArrayOutputStream ubaos = new UnsafeByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(ubaos);
        TradeOfferManager.GSON.toJson(object, writer);
        try {
            writer.close();
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        return ubaos.getBytes();
    }
}
