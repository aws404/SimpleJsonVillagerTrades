package com.github.aws404.sjvt.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import com.github.aws404.sjvt.SimpleJsonVillagerTradesMod;

import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class Commands {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        LiteralArgumentBuilder<ServerCommandSource> baseCommand = CommandManager.literal(SimpleJsonVillagerTradesMod.MOD_ID).requires(source -> source.hasPermissionLevel(2));

        if (FabricLoader.getInstance().isModLoaded("advanced_runtime_resource_pack")) {
            BuildCommand.registerBuildCommand(baseCommand);
        }

        dispatcher.register(baseCommand);
    }

}
