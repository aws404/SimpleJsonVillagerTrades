package com.github.aws404.sjvt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.aws404.sjvt.trade_offers.TradeOfferFactoryType;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

public class SimpleJsonVillagerTradesMod implements ModInitializer {
	public static final String MOD_ID = "sjvt";
	public static final Logger LOGGER = LoggerFactory.getLogger("SimpleJsonVillagerTrades");
	public static final RegistryKey<Registry<TradeOfferFactoryType<?>>> TRADE_OFFER_FACTORY_KEY = RegistryKey.ofRegistry(id("trade_offer_factory"));
	public static final Registry<TradeOfferFactoryType<?>> TRADE_OFFER_FACTORY_REGISTRY = FabricRegistryBuilder.createSimple(TRADE_OFFER_FACTORY_KEY).buildAndRegister();
	public static final TradeOfferManager TRADE_OFFER_MANAGER = new TradeOfferManager();

	@Override
	public void onInitialize() {
		LOGGER.info("Starting SimpleJsonVillagerTrades!");
		TradeOfferFactoryType.init();
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(TRADE_OFFER_MANAGER);
	}

	/**
	 * Create an identifier with the mod ID as the namespace.
	 * @param string the identifier's path
	 * @return the identifier
	 */
	public static Identifier id(String string) {
		return new Identifier(MOD_ID, string);
	}

	private static Class<TradeOfferFactoryType<?>> getType() {
		return null;
	}
}
