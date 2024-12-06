package dev.vansen.wellstaff.commands.debugging;

import dev.vansen.commandutils.CommandUtils;
import dev.vansen.commandutils.argument.CommandArgument;
import dev.vansen.configutils.Configer;
import dev.vansen.utility.annotations.Init;
import dev.vansen.utility.command.Command;
import dev.vansen.utility.resource.ResourceSaver;
import dev.vansen.welldevelopment.Holder;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

@SuppressWarnings("all")
public class DebugCommands implements Command {

    @Override
    @Init
    public void register() {
        if (Configer.load("config.yml").getBoolean("Staff.configuration.debug")) {
            CommandUtils.command("deletefile")
                    .permission("wellstaff.debug")
                    .aliases("df")
                    .argument(CommandArgument.greedy("path")
                            .completion((context, wrapper) -> {
                                try {
                                    wrapper.suggest(
                                            Arrays.stream(Objects.requireNonNull(new File(Holder.get().getDataFolder(), "").listFiles()))
                                                    .filter(file -> file.isFile())
                                                    .map(file -> file.getName())
                                                    .toList()
                                    );
                                } catch (@NotNull Exception e) {
                                    wrapper.suggest("There's something wrong, did you delete the data folder?");
                                    Holder.get()
                                            .getComponentLogger()
                                            .error("Failed to list files", e);
                                }
                                return wrapper.build();
                            })
                            .defaultExecute(context -> {
                                File file = new File(Holder.get().getDataFolder(), context.arg(1));
                                if (file.exists()) {
                                    file.delete();
                                    context.response("<green>Successfully deleted the file");
                                    if (context.hasFlag("--remake-all-files")) {
                                        ResourceSaver.save();
                                        context.response("<green>Successfully remade all files");
                                    }
                                } else {
                                    context.response("<red>File does not exist!");
                                }
                            }))
                    .register();
            CommandUtils.command("remakefiles")
                    .permission("wellstaff.debug")
                    .aliases("rf")
                    .defaultExecute(context -> {
                        ResourceSaver.save();
                        context.response("<green>Successfully remade all files");
                    })
                    .register();
        }
    }
}
