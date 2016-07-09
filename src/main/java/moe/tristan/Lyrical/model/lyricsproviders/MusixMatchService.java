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

package moe.tristan.Lyrical.model.lyricsproviders;

import moe.tristan.Lyrical.model.configuration.ApplicationConfiguration;
import moe.tristan.Lyrical.model.configuration.ApplicationConfiguration.ConfigurationKey;
import moe.tristan.Lyrical.model.entity.Song;
import org.jetbrains.annotations.NotNull;
import org.jmusixmatch.MusixMatch;
import org.jmusixmatch.MusixMatchException;
import org.jmusixmatch.entity.track.TrackData;

/**
 * Created by Tristan Deloche on 05/07/2016.
 */

public final class MusixMatchService implements Service {

    private final MusixMatch musixMatch;

    public MusixMatchService() {
        this(ApplicationConfiguration.getINSTANCE().get(ConfigurationKey.MUSIXMATCH_API_KEY));
    }

    public MusixMatchService(String apiKey) {
        musixMatch = registerWithKey(apiKey);
    }

    @NotNull
    private MusixMatch registerWithKey(String apiKey) {
        return new MusixMatch(apiKey);
    }

    @Override
    public Song identifySong(String title, String artist) {
        TrackData bestGuess = new TrackData();
        try {
            bestGuess = musixMatch.getMatchingTrack(title, artist).getTrack();
        } catch (MusixMatchException e) {
            e.printStackTrace();
        }

        String lyrics = "Lyrics couldn't be fetched. Please verify your internet connection !";
        try {
            lyrics = musixMatch.getLyrics(bestGuess.getTrackId()).getLyricsBody();
        } catch (MusixMatchException e) {
            e.printStackTrace();
        }
        return Song.builder()
                .title(bestGuess.getTrackName())
                .artist(bestGuess.getArtistName())
                .lyrics(lyrics)
                .build();
    }
}
