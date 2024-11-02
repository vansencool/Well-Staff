package dev.vansen.wellstaff.registrar;

import dev.vansen.utility.command.CommandRegistrar;
import dev.vansen.utility.debugging.Debug;
import dev.vansen.utility.resource.ResourceSaver;
import net.kyori.adventure.text.Component;

public final class Registrar {

    public static void register() {
        Debug.debug(Component.text(
                "Registering commands"
        ));
        CommandRegistrar.registerAll();
        Debug.debug(Component.text(
                "Saving resources"
        ));
        ResourceSaver.save();
    }
}
