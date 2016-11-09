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

package moe.tristan.Lyrical.model.monitoring;

import moe.tristan.Lyrical.model.entity.Song;
import moe.tristan.Lyrical.model.integration.players.Player;
import moe.tristan.Lyrical.model.integration.players.PlayerSong;
import moe.tristan.Lyrical.model.lyricsproviders.LyricsServicesManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This class acts as a decorator for Player monitoring
 */
public final class Monitor<T extends Player> {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Monitor.class);
    private final Class<? extends Player> currentClass;
    private final T monitoredPlayer;
    private boolean shouldMonitor = false;
    @Nullable
    private PlayerSong lastKnownSong = null;

    public Monitor(T playerToMonitor) {
        this.monitoredPlayer = playerToMonitor;
        currentClass = this.monitoredPlayer.getClass();
        Runtime.getRuntime().addShutdownHook(new Thread(this::stopMonitoring));
    }

    private void checkSong() {
        PlayerSong currentSong = monitoredPlayer
                .getCurrentlyPlayedSong();

        if (!currentSong.equals(lastKnownSong)) {
            songChangedTo(currentSong);
        }
    }

    private void songChangedTo(@NotNull PlayerSong newSong) {
        lastKnownSong = newSong;
        Song lyricizedSong = LyricsServicesManager.identifySong(
                newSong.getTitle(),
                newSong.getArtist()
        );

        PlayerMonitorService.setCurrentSong(lyricizedSong);
    }

    public void beginMonitoring() {
        shouldMonitor = true;
        this.monitoredPlayer.startMonitoring();
        new Timer(true).scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        if (shouldMonitor) {
                            checkSong();
                        } else {
                            this.cancel();
                        }
                    }
                },
                0,
                1000
        );
        log.info("Correctly spawned a Monitor for " + monitoredPlayer.getClass().getSimpleName());
    }

    public void stopMonitoring() {
        log.info("Correctly killed the monitor for " + monitoredPlayer.getClass().getSimpleName());
        this.monitoredPlayer.stopMonitoring();
        shouldMonitor = false;
    }

    public Class<? extends Player> getMonitoredPlayer() {
        return monitoredPlayer.getClass();
    }

    public Class<? extends Player> getCurrentClass() {
        return this.currentClass;
    }
}
