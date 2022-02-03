package com.github.aws404.sjvt;

import com.github.aws404.sjvt.trade_offers.TradeOfferFactoryType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleJsonVillagerTradesMod implements ModInitializer {
	public static final String MOD_ID = "sjvt";
	public static final Logger LOGGER = LoggerFactory.getLogger("SimpleJsonVillagerTrades");
	public static final Registry<TradeOfferFactoryType> TRADE_OFFER_FACTORY_TYPE_REGISTRY = FabricRegistryBuilder.createSimple(TradeOfferFactoryType.class, SimpleJsonVillagerTradesMod.id("trade_offer_factory_type")).buildAndRegister();
	public static final TradeOfferManager TRADE_OFFER_MANAGER = new TradeOfferManager();

	@Override
	public void onInitialize() {
		LOGGER.info("Starting SimpleJsonVillagerTrades!");
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
}
