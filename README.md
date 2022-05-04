# Simple JSON Villager Trades
As simple as it sounds, a small jij-able/standalone library to modify/add merchant trades via datapacks.

## For Datapackers
You are able to modify merchant trades through JSON files the `data/<namespace>/trade_offers/` directory.
For example:
```json
{
  "__COMMENT": "This adds a ruined portal map to the Cartographer's trades.",

  "profession": "minecraft:cartographer",
  "replace": false,
  "offers": {
    "apprentice": [
      {
        "type": "minecraft:sell_map",
        "price": 5,
        "name_key": "filled_map.ruined_portal",
        "feature_tag": "minecraft:ruined_portal",
        "icon": "target_x",
        "max_uses": 10,
        "experience": 2
      }
    ]
  }
}
```
For more tutorials and more information on modifying merchant trades, see [the wiki](https://github.com/aws404/SimpleJsonVillagerTrades/wiki).

## Compatibility
SJVT uses Minecraft's hardcoded trade map as a base for the datapacks to build on top of, so it should work with most mods, even those who add new villager professions.  
You can also export the hardcoded map (including non-native support mods) as a datapack by running the `sjvt:build` command.

## For Mod Developers
You can include the mod using the following repository and dependency in your `build.gradle` (where `[TAG]` is the latest version):
```gradle
repositories {
  maven {
    name = "Modrinth"
    url = "https://api.modrinth.com/maven"
    content {
      includeGroup "maven.modrinth"
    }
  }
}

dependencies {
  modImplementation include("maven.modrinth:sjvt:[TAG]") //To include (jij) in your mod
  modImplementation "maven.modrinth:sjvt:[TAG]" // To require the mod to be installed seperatly
}
```

### Custom Trade Offer Factories
To create a trade offer factory, implement the `SerializableTradeOfferFactory` interface.  
Then register the codec to the `TradeOfferFactories.TRADE_OFFER_FACTORY_REGISTRY` registry.  
You can also use methods and fields in the `CodecHelper` class to assist in the creation of codecs.

Then see 'For Datapackers'

