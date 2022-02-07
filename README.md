# Simple JSON Villager Trades
As simple as it sounds, a small jij-able/standalone library to modify/add merchant trades via datapacks.

## For Datapackers
You are able to modify merchant trades through JSON files the `data/<namespace>/trade_offers/` directory.
For example:
```json
{
  "__COMMENT": "This adds an Igloo map to the Cartographer's trades.",

  "profession": "minecraft:cartographer",
  "replace": false,
  "offers": {
    "apprentice": [
      {
        "type": "minecraft:sell_map",
        "price": 5,
        "feature": "minecraft:igloo",
        "icon": "target_x",
        "max_uses": 10,
        "experience": 2
      }
    ]
  }
}
```
For more tutorials and more information on modifying merchant trades, see [the wiki](https://github.com/aws404/SimpleJsonVillagerTrades/wiki).

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

Then see 'For Datapackers'

## Compatibility
SJVT uses Minecraft's hardcoded trade map as a base for the datapacks to build on top of, so it should work with most mods, even those who add new villager professions.
