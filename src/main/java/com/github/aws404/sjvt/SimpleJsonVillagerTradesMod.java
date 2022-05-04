package com.github.aws404.sjvt;

import com.github.aws404.sjvt.api.TradeOfferFactories;
import com.github.aws404.sjvt.trade_offers.SellItemForItemsOfferFactory;
import com.github.aws404.sjvt.trade_offers.TypeAwareSellItemForItemsOfferFactory;
import com.github.aws404.sjvt.trade_offers.VanillaTradeOfferFactories;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleJsonVillagerTradesMod implements ModInitializer {
	public static final String MOD_ID = "sjvt";
	public static final Logger LOGGER = LoggerFactory.getLogger("SimpleJsonVillagerTrades");
	public static final TradeOfferManager TRADE_OFFER_MANAGER = new TradeOfferManager();

	@Override
	public void onInitialize() {
		LOGGER.info("Starting SimpleJsonVillagerTrades!");
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(TRADE_OFFER_MANAGER);
		SimpleJsonVillagerTradesMod.registerBuiltinFactories();

		if (FabricLoader.getInstance().isModLoaded("advanced_runtime_resource_pack")) {
			CommandRegistrationCallback.EVENT.register(BuildCommand::register);
		}
	}

	private static void registerBuiltinFactories() {
		Registry.register(TradeOfferFactories.TRADE_OFFER_FACTORY_REGISTRY, new Identifier("buy_for_one_emerald"), VanillaTradeOfferFactories.BUY_FOR_ONE_EMERALD);
		Registry.register(TradeOfferFactories.TRADE_OFFER_FACTORY_REGISTRY, new Identifier("sell_item"), VanillaTradeOfferFactories.SELL_ITEM);
		Registry.register(TradeOfferFactories.TRADE_OFFER_FACTORY_REGISTRY, new Identifier("sell_suspicious_stew"), VanillaTradeOfferFactories.SELL_SUSPICIOUS_STEW);
		Registry.register(TradeOfferFactories.TRADE_OFFER_FACTORY_REGISTRY, new Identifier("process_item"), VanillaTradeOfferFactories.PROCESS_ITEM);
		Registry.register(TradeOfferFactories.TRADE_OFFER_FACTORY_REGISTRY, new Identifier("sell_enchanted_tool"), VanillaTradeOfferFactories.SELL_ENCHANTED_TOOL);
		Registry.register(TradeOfferFactories.TRADE_OFFER_FACTORY_REGISTRY, new Identifier("type_aware_buy_for_one_emerald"), VanillaTradeOfferFactories.TYPE_AWARE_BUY_FOR_ONE_EMERALD);
		Registry.register(TradeOfferFactories.TRADE_OFFER_FACTORY_REGISTRY, new Identifier("sell_potion_holding_item"), VanillaTradeOfferFactories.SELL_POTION_HOLDING_ITEM);
		Registry.register(TradeOfferFactories.TRADE_OFFER_FACTORY_REGISTRY, new Identifier("enchant_book"), VanillaTradeOfferFactories.ENCHANT_BOOK);
		Registry.register(TradeOfferFactories.TRADE_OFFER_FACTORY_REGISTRY, new Identifier("sell_map"), VanillaTradeOfferFactories.SELL_MAP);
		Registry.register(TradeOfferFactories.TRADE_OFFER_FACTORY_REGISTRY, new Identifier("sell_dyed_armor"), VanillaTradeOfferFactories.SELL_DYED_ARMOR);

		Registry.register(TradeOfferFactories.TRADE_OFFER_FACTORY_REGISTRY, SimpleJsonVillagerTradesMod.id("sell_item_for_items"), SellItemForItemsOfferFactory.CODEC);
		Registry.register(TradeOfferFactories.TRADE_OFFER_FACTORY_REGISTRY, SimpleJsonVillagerTradesMod.id("type_aware_sell_item_for_items"), TypeAwareSellItemForItemsOfferFactory.CODEC);
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
