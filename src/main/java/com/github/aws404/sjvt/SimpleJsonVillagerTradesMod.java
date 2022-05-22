package com.github.aws404.sjvt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.aws404.sjvt.trade_offers.TradeOfferFactoryType;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SimpleJsonVillagerTradesMod implements DedicatedServerModInitializer {
	public static final String MOD_ID = "sjvt";
	public static final Logger LOGGER = LoggerFactory.getLogger("SimpleJsonVillagerTrades");
	public static final Registry<TradeOfferFactoryType<?>> TRADE_OFFER_FACTORY_REGISTRY = FabricRegistryBuilder.<TradeOfferFactoryType<?>>createSimple(null, SimpleJsonVillagerTradesMod.id("trade_offer_factories")).buildAndRegister();
	public static final TradeOfferManager TRADE_OFFER_MANAGER = new TradeOfferManager();

	@Override
	public void onInitializeServer() {
		LOGGER.info("Starting SimpleJsonVillagerTrades!");
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(TRADE_OFFER_MANAGER);

		if (FabricLoader.getInstance().isModLoaded("advanced_runtime_resource_pack")) {
			CommandRegistrationCallback.EVENT.register(BuildCommand::register);
		} else {
			LOGGER.info("ARRP mod not found, build commands could not be registered.");
		}
	}

	/**
	 * Create an identifier with the mod ID as the namespace.
	 * @param string the identifier's path
	 * @return the identifier
	 */
	public static Identifier id(String string) {
		return new Identifier(MOD_ID, string);
	}

}
