/*
 *     <DESCRIPTION>
 *     Copyright (C) 2016 Tristan Deloche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package moe.tristan.Lyrical.model.integration.system;

import lombok.extern.slf4j.Slf4j;
import moe.tristan.Lyrical.model.integration.system.Windows.WindowsNT;
import moe.tristan.Lyrical.model.integration.system.macOS.macOS;
import org.jetbrains.annotations.NotNull;

/**
 * Class providing utilities for platform detections
 */
@Slf4j
public class SystemUtilities {

    public static final OperatingSystem CURRENT_PLATFORM =
            getCurrentOperatingSystem(System.getProperty("os.name"));

    private static OperatingSystem getCurrentOperatingSystem(@NotNull String platformName) {
        log.info("Current platform is : "+platformName);
        if (isOSX(platformName)) {
            log.info("Platform detected as macOS");
            return macOS.INSTANCE;
        } else if(isWindows(platformName)) {
            log.info("Platform detected as Windows");
            return WindowsNT.INSTANCE;
        } else {
            log.error("Platform unsupported as of right now => " + platformName);
            return new DummySystem(platformName);
        }
    }

    private static boolean isOSX(@NotNull String platformName) {
        return  platformName.toLowerCase().contains("mac") ||
                platformName.toLowerCase().contains("darwin") ||
                platformName.toLowerCase().contains("osx");
    }

    private static boolean isWindows(@NotNull String platformName) {
        return platformName.toLowerCase().contains("windows") ||
                platformName.toLowerCase().contains("nt");
    }

    private static boolean isLinux(@NotNull String platformName) {
        return platformName.toLowerCase().contains("linux");
    }
}
