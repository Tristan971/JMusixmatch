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

package moe.tristan.Lyrical.view.views;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import moe.tristan.Lyrical.view.UIBridge;

/**
 * Created by Tristan Deloche on 09/07/2016.
 */
public class RootViewController extends AnchorPane {
    @FXML
    private AnchorPane parentPane;

    @FXML
    private Text title_text;

    @FXML
    private Text artist_text;

    @FXML
    private Label lyrics_label;

    public RootViewController() {}


    // It is called by JavaFX
    @SuppressWarnings("unused")
    public void initialize() {
        title_text.setText(UIBridge.getInstance().title.get());
        UIBridge.getInstance().title.addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                title_text.setText(newValue);
                System.out.println(isTextClipped(title_text));
            });
        });
        artist_text.setText(UIBridge.getInstance().artist.get());
        UIBridge.getInstance().artist.addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> artist_text.setText(newValue));
        });
        lyrics_label.setText(UIBridge.getInstance().lyrics.get());
        UIBridge.getInstance().lyrics.addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> lyrics_label.setText(newValue));
        });

        //TranslateTransition transition = new TranslateTransition(new Duration(5000), title_text);
        //transition.setFromX(title_text.getX()-250);
        //transition.setToX(title_text.getX()+150);
        //transition.playFromStart();
    }

    private boolean isTextClipped(Text text) {
        return text.getBoundsInParent().getWidth() > 230;
    }

}
