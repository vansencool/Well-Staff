/*
 * Copyright (c) 2024 vansencool
 * By using, modifying, or distributing this software, you agree to the following terms:
 * Free Usage and Distribution:
 * You may use, copy, and distribute this plugin and its source code freely for personal or public use,
 * provided no fees are charged.
 * Modifications:
 * You are free to modify the plugin's source code and share your modifications, as long as the modified
 * plugin is also shared under these same terms and remains non-commercial.
 * Non-Commercial Use Only:
 * You may not sell, license, or otherwise charge for this plugin, modified versions of this plugin,
 * or any services directly involving this plugin, including paid access, subscriptions, or any other commercial use.
 * Attribution:
 * You must credit the original author(s) in any public distribution or modification of this plugin.
 * This attribution must include a link back to the original GitHub repository (or the URL specified by the author)
 * if distributed online.
 * No Warranty:
 * This plugin is provided "as is" without any warranties, and the author(s) are not liable for any damages
 * arising from its use.
 */
package dev.vansen.wellstaff;

import dev.vansen.utility.debugging.Debug;
import dev.vansen.utility.version.VersionChecker;
import dev.vansen.welldevelopment.WellDevelopment;
import dev.vansen.wellstaff.registrar.Registrar;
import net.kyori.adventure.text.Component;

@SuppressWarnings("unused")
public final class WellStaff extends WellDevelopment {

    @Override
    protected void start() {
        try {
            Class.forName("io.papermc.paper.configuration.Configuration");
        } catch (ClassNotFoundException ignored) {
            getLogger()
                    .severe("=".repeat(50));
            getLogger()
                    .severe("Spigot detected!");
            getLogger()
                    .severe(" ");
            getLogger()
                    .severe("Spigot is not supported by this plugin.");
            getLogger()
                    .severe("However, Paper offers several benefits, including:");
            getLogger()
                    .severe(" ");
            getLogger()
                    .severe("Performance improvements: faster server performance and reduced lag");
            getLogger()
                    .severe("Bug fixes: resolve issues and improves overall server stability");
            getLogger()
                    .severe("Security enhancements: protect your server from potential threats");
            getLogger()
                    .severe("Timings v2: a powerful tool for diagnosing and resolving lag issues");
            getLogger()
                    .severe(" ");
            getLogger()
                    .severe("The plugin will be disabled. Consider switching to Paper for a better experience.");
            getLogger()
                    .severe("Learn how to migrate to Paper at https://docs.papermc.io/paper/migration");
            getLogger()
                    .severe("=".repeat(50));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (VersionChecker.isLower("1.20.6")) {
            getComponentLogger()
                    .error(Component.text("=".repeat(50)));
            getComponentLogger()
                    .error(Component.text("Your server is running on a Minecraft version lower than 1.20.6."));
            getComponentLogger()
                    .error(Component.text(" "));
            getComponentLogger()
                    .error(Component.text("This plugin is not compatible with versions lower than 1.20.6."));
            getComponentLogger()
                    .error(Component.text("Please update your server to a newer version to use this plugin."));
            getComponentLogger()
                    .error(Component.text("Learn more about updating your server at https://docs.papermc.io/paper/updating"));
            getComponentLogger()
                    .error(Component.text("=".repeat(50)));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        Debug.debug(getConfig().getBoolean("Staff.configuration.debug"));
        Registrar.register();
    }
}