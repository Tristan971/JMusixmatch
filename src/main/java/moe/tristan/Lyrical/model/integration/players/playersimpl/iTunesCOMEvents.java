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

package moe.tristan.Lyrical.model.integration.players.playersimpl;

import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import lombok.extern.slf4j.Slf4j;
import moe.tristan.Lyrical.model.integration.players.PlayerSong;

/**
 * Listener class to iTunes COM events
 */
@SuppressWarnings("unused")
@Slf4j
public final class iTunesCOMEvents {
    public static PlayerSong lastComEventSong = PlayerSong.dummyPlayerSong();

    public iTunesCOMEvents() {
        log.info("Created an instance of "+this.getClass().getSimpleName());
    }

    public void OnPlayerPlayEvent(Variant[] args) {
        log.debug("OnPlayerPlayEvent");
        lastComEventSong = extractArtistAndTrackName(args);
    }

    public void OnPlayerPlayingTrackChangedEvent(Variant[] args) {
        log.debug("OnPlayerPlayingTrackChangedEvent");
        lastComEventSong = extractArtistAndTrackName(args);
    }

    private static PlayerSong extractArtistAndTrackName(Variant[] args) {
        if (args[0].getvt() == Variant.VariantDispatch) {
            Dispatch event = args[0].getDispatch();
            String artist = Dispatch.get(event, "Artist").toString();
            String name = Dispatch.get(event, "Name").toString();

            return PlayerSong.builder()
                    .artist(artist)
                    .title(name)
                    .build();
        }

        log.error("Grave COM interface error. "
                + "The variant received was not of type VT_DISPATCH : "+
                args[0].toString()
        );
        return PlayerSong.dummyPlayerSong();
    }
}
