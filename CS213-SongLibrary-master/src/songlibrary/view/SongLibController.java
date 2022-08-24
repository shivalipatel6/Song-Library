package songlibrary.view;

import java.util.ArrayList;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import songlibrary.app.SongLinkedList;

public class SongLibController {

    @FXML ListView <String> songListDisplayUI;
    @FXML Text songDetailUI;
    @FXML Button addUI;
    @FXML Button deleteUI;
    @FXML Button editUI;
    @FXML TextField nameUI;
    @FXML TextField artistUI;
    @FXML TextField albumUI;
    @FXML TextField yearUI;

    private ObservableList<String> obSongListUI;

    private SongLinkedList songList = new SongLinkedList();

    // ----- Helper Methods -----
    // sends alert to user !!!
    public static void sendAlert(String title, String snippet, String body) {
        Alert warning = new Alert(AlertType.INFORMATION);
        // warning.initOwner(mainStage);
        warning.setTitle(title);
        warning.setHeaderText(snippet);
        warning.setContentText(body);
        warning.showAndWait();
    }

    // asks for confirmation from user !!!
    public String sendConfirmation(String title, String snippet, String body) {
        Alert confirm = new Alert(AlertType.CONFIRMATION);
        // warning.initOwner(mainStage);
        confirm.setTitle(title);
        confirm.setHeaderText(snippet);
        confirm.setContentText(body);
        Optional<ButtonType> result = confirm.showAndWait();

        // returns just a blank string if OK is pressed and null if not
        if (result.get() == ButtonType.OK) return " ";
        return null;
    }

    // update songListDisplayUI with current songs
    public void updateListUI() {
        ArrayList<String> songListNA = songList.returnSongNAList(songList);
        obSongListUI = FXCollections.observableArrayList(songListNA);
        songListDisplayUI.setItems(obSongListUI);
    }

    // collect song detail from selected item in list view
    public void displaySelectedSongDetail() {
        String songDetail = songListDisplayUI.getSelectionModel().getSelectedItem();

        // check if nothing is selected
        if (songDetail == null) return;

        // find song in songList
        String[] songResult = songDetail.split(" : ");
        songDetailUI.setText(songList.returnSongDetails(songList, songResult[0], songResult[1]));
    }

    // ----- On Load Method -----
    public void initialize() {
        updateListUI();
        songListDisplayUI.getSelectionModel().select(0);
        displaySelectedSongDetail();
    }

    // ----- Button Methods -----
    // event for when button is pressed
    public void buttonEvent(ActionEvent e) {
        // check which button was pressed and respond accordingly
        Button btn = (Button)e.getSource();
        if (btn == addUI) addSong();
        else if (btn == editUI) editSong();
        else deleteSong();
    }

    // add new song to list
    public void addSong() {
        // make sure name and artist inputs are filled (spaces are not valid inputs)
        if (nameUI.getText().trim() == "" && artistUI.getText().trim() == "") {
            sendAlert("Error Adding Song", "Missing Inputs:", "Song Name \nArtist Name");
            return;
        }
        else if (nameUI.getText().trim() == "") {
            sendAlert("Error Adding Song", "Missing Input:", "Song Name");
            return;
        }
        else if (artistUI.getText().trim() == "") {
            sendAlert("Error Adding Song", "Missing Input:", "Artist Name");
            return;
        }

        // confirm with user if they want to add song
        String confirmation = sendConfirmation("Adding New Song", "Are you sure you want to add this song?", " ");
        if (confirmation == null) return;

        // collect inputs
        String nameInput = nameUI.getText();
        String artistInput = artistUI.getText();
        String albumInput;
        String yearInput;
        if (albumUI.getText().trim() == "") albumInput = null;
        else albumInput = albumUI.getText();
        if (yearUI.getText().trim() == "") yearInput = null;
        else yearInput = yearUI.getText();

        // add song to song list
        songList = songList.addSong(songList, nameInput, artistInput, albumInput, yearInput);
        //songList.printList(songList);

        // update songList.txt
        songList.saveSongs(songList);

        // update songList UI and songDetail UI
        updateListUI();
        String addedSong = nameInput + " : " + artistInput;
        for(String each : obSongListUI) {
            if (each.compareTo(addedSong) == 0) {
                songListDisplayUI.getSelectionModel().select(addedSong);
            }
        }
        displaySelectedSongDetail();
    }

    // delete song from list
    public void deleteSong() {
        // delete
    }

    // edit song in list
    public void editSong() {
        // delete
    }
}