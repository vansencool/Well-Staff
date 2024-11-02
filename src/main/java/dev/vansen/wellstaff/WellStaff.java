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
import dev.vansen.welldevelopment.WellDevelopment;
import dev.vansen.wellstaff.registrar.Registrar;

@SuppressWarnings("unused")
public final class WellStaff extends WellDevelopment {

    @Override
    protected void start() {
        Debug.debug(getConfig().getBoolean("Staff.configuration.debug"));
        Registrar.register();
    }
}