package dev.vansen.wellstaff.commands.general;

import dev.vansen.commandutils.CommandUtils;
import dev.vansen.commandutils.argument.Argument;
import dev.vansen.commandutils.argument.CommandArgument;
import dev.vansen.commandutils.command.CheckType;
import dev.vansen.commandutils.command.ExecutableSender;
import dev.vansen.commandutils.info.CommandInfo;
import dev.vansen.commandutils.permission.CommandPermission;
import dev.vansen.commandutils.sender.SenderTypes;
import dev.vansen.utility.annotations.Init;
import dev.vansen.utility.command.Command;
import dev.vansen.utility.command.CommandRegistrar;
import dev.vansen.utility.player.PlayerUtils;
import dev.vansen.wellstaff.message.Messager;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import org.bukkit.GameMode;

@SuppressWarnings("all")
public class GamemodeCommands implements Command {

    @Override
    @Init
    public void register() {
        CommandRegistrar.register(CommandUtils.command("gamemode")
                        .info(CommandInfo.info()
                                .permission(CommandPermission.permission("wellstaff.gamemode"))
                                .aliases("gm"))
                        .argument(CommandArgument.of(new Argument("gamemode", ArgumentTypes.gameMode()))
                                .defaultExecute(context -> {
                                    Messager.sender()
                                            .who(context.sender())
                                            .player();
                                    PlayerUtils.player(context.player())
                                            .gameMode(context.argGameMode("gamemode"));
                                    context.check(CheckType.PLAYER);
                                })),
                "gamemode");

        // Utility
        CommandRegistrar.register(CommandUtils.command("gms")
                .info(CommandInfo.info()
                        .permission(CommandPermission.permission("wellstaff.gms")))
                .defaultExecute(context -> {
                    PlayerUtils.player(context.player())
                            .gameMode(GameMode.SURVIVAL);
                }, ExecutableSender.types(SenderTypes.PLAYER)), "gms");

        CommandRegistrar.register(CommandUtils.command("gma")
                .info(CommandInfo.info()
                        .permission(CommandPermission.permission("wellstaff.gma")))
                .defaultExecute(context -> {
                    PlayerUtils.player(context.player())
                            .gameMode(GameMode.ADVENTURE);
                }, ExecutableSender.types(SenderTypes.PLAYER)), "gma");

        CommandRegistrar.register(CommandUtils.command("gmc")
                .info(CommandInfo.info()
                        .permission(CommandPermission.permission("wellstaff.gmc")))
                .defaultExecute(context -> {
                    PlayerUtils.player(context.player())
                            .gameMode(GameMode.CREATIVE);
                }, ExecutableSender.types(SenderTypes.PLAYER)), "gmc");

        CommandRegistrar.register(CommandUtils.command("gmsp")
                .info(CommandInfo.info()
                        .permission(CommandPermission.permission("wellstaff.gmsp")))
                .defaultExecute(context -> {
                    PlayerUtils.player(context.player())
                            .gameMode(GameMode.SPECTATOR);
                }, ExecutableSender.types(SenderTypes.PLAYER)), "gmsp");
    }
}
