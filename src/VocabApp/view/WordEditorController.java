package VocabApp.view;

import VocabApp.Main;
import VocabApp.model.Word;
import VocabApp.util.AlertBox;
import VocabApp.util.Util;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class WordEditorController {

    @FXML private Label targetLanguageLabel;
    @FXML private TextField targetLanguageField;
    @FXML private Label baseLanguageLabel;
    @FXML private TextField baseLanguageField;
    @FXML private Label audioLocationLabel;
    @FXML private Button chooseAudioButton;
    @FXML private Button playSoundButton;
    @FXML private Label targetLanguageAnswersLabel;
    @FXML private Label baseLanguageAnswersLabel;
    @FXML private Label spokenAnswersLabel;
    @FXML private ListView<String> targetLanguageAnswersList;
    @FXML private ListView<String> baseLanguageAnswersList;
    @FXML private ListView<String> spokenLanguageAnswersList;
    @FXML private TextField targetLanguageAnswersField;
    @FXML private TextField baseLanguageAnswersField;
    @FXML private TextField spokenLanguageAnswersField;
    @FXML private Button targetLanguageAnswersAddButton;
    @FXML private Button baseLanguageAnswersAddButton;
    @FXML private Button spokenLanguageAnswersAddButton;
    @FXML private Button targetLanguageAnswersRemoveButton;
    @FXML private Button baseLanguageAnswersRemoveButton;
    @FXML private Button spokenLanguageAnswersRemoveButton;
    @FXML private Button addSaveButton;
    @FXML private Button cancelButton;
    @FXML private Pane spokenAnswersPane;

    private Stage window;
    private Word word;
    private SimpleStringProperty audioData;
    private AudioClip audioClip;

    public Word setup(Word word, String baseLanguage, String targetLanguage, Stage window) {
        this.word = word;
        this.audioData = new SimpleStringProperty();
        // target language display word
        this.targetLanguageLabel.setText("Word in " + Main.langNames.apply(baseLanguage, targetLanguage));
        if (this.word != null) this.targetLanguageField.setText(this.word.getTargetLanguageDisplayWord());
        // base language display word
        this.baseLanguageLabel.setText("Word in " + Main.langNames.apply(baseLanguage, baseLanguage));
        if (this.word != null) this.baseLanguageField.setText(this.word.getBaseLanguageDisplayWord());
        // audio location
        this.audioLocationLabel.setText("Audio file");
        if (this.word != null && this.word.getAudioData() != null) {
            this.audioData.setValue(this.word.getAudioData());
            loadAudioClip();
        }
        // this.audioData.addListener(((observable, oldValue, newValue) -> this.spokenAnswersPane.disableProperty().setValue(newValue == null)));
        // written target answers
        this.targetLanguageAnswersLabel.setText("Written Answers in " + Main.langNames.apply(baseLanguage, targetLanguage));
        if (this.word != null) this.targetLanguageAnswersList.setItems(FXCollections.observableArrayList(this.word.getTargetLanguageAnswers()));
        else this.targetLanguageAnswersList.setItems(FXCollections.observableArrayList());
        this.targetLanguageAnswersAddButton.setText("Add");
        this.targetLanguageAnswersAddButton.setOnAction(event -> {
            String answer = this.targetLanguageAnswersField.getText();
            String validity = validAnswer(answer,this.targetLanguageAnswersList.getItems());
            switch (validity) {
                case "valid": {this.targetLanguageAnswersList.getItems().add(answer.trim()); break;}
                case "contains comma": {AlertBox.display("Answer contains comma","Answers cannot contain ','."); break;}
                case "contains square bracket": {AlertBox.display("Answer contains square bracket","Answers cannot contain '[' or ']'."); break;}
                case "already exists": {AlertBox.display("Answer already exists","The answer is already in the list."); break;}
                case "empty": {AlertBox.display("Answer is empty","The answer must not be empty."); break;}
            }
        });
        this.targetLanguageAnswersRemoveButton.setText("Remove");
        this.targetLanguageAnswersRemoveButton.setOnAction(event -> {
            if (this.targetLanguageAnswersList.getSelectionModel().getSelectedItem() != null)
                this.targetLanguageAnswersList.getItems().remove(this.targetLanguageAnswersList.getSelectionModel().getSelectedItem());
            else
                AlertBox.display("No answer selected","Please select an answer first.");
        });
        // written base answers
        this.baseLanguageAnswersLabel.setText("Written Answers in " + Main.langNames.apply(baseLanguage, baseLanguage));
        if (this.word != null) this.baseLanguageAnswersList.setItems(FXCollections.observableArrayList(this.word.getBaseLanguageAnswers()));
        else this.baseLanguageAnswersList.setItems(FXCollections.observableArrayList());
        this.baseLanguageAnswersAddButton.setText("Add");
        this.baseLanguageAnswersAddButton.setOnAction(event -> {
            String answer = this.baseLanguageAnswersField.getText();
            String validity = validAnswer(answer,this.baseLanguageAnswersList.getItems());
            switch (validity) {
                case "valid": {this.baseLanguageAnswersList.getItems().add(answer.trim()); break;}
                case "contains comma": {AlertBox.display("Answer contains comma","Answers cannot contain ','."); break;}
                case "contains square bracket": {AlertBox.display("Answer contains square bracket","Answers cannot contain '[' or ']'."); break;}
                case "already exists": {AlertBox.display("Answer already exists","The answer is already in the list."); break;}
                case "empty": {AlertBox.display("Answer is empty","The answer must not be empty."); break;}
            }
        });
        this.baseLanguageAnswersRemoveButton.setText("Remove");
        this.baseLanguageAnswersRemoveButton.setOnAction(event -> {
            if (this.baseLanguageAnswersList.getSelectionModel().getSelectedItem() != null)
                this.baseLanguageAnswersList.getItems().remove(this.baseLanguageAnswersList.getSelectionModel().getSelectedItem());
            else
                AlertBox.display("No answer selected","Please select an answer first.");
        });
        // spoken target answers
        this.spokenAnswersLabel.setText("Spoken Answers in " + Main.langNames.apply(baseLanguage, targetLanguage));
        if (this.word != null && this.word.getSpokenAnswers() != null) this.spokenLanguageAnswersList.setItems(FXCollections.observableArrayList(this.word.getSpokenAnswers()));
        else this.spokenLanguageAnswersList.setItems(FXCollections.observableArrayList());
        this.spokenLanguageAnswersAddButton.setText("Add");
        this.spokenLanguageAnswersAddButton.setOnAction(event -> {
            String answer = this.spokenLanguageAnswersField.getText();
            String validity = validAnswer(answer,this.spokenLanguageAnswersList.getItems());
            switch (validity) {
                case "valid": {this.spokenLanguageAnswersList.getItems().add(answer.trim()); break;}
                case "contains comma": {AlertBox.display("Answer contains comma","Answers cannot contain ','."); break;}
                case "contains square bracket": {AlertBox.display("Answer contains square bracket","Answers cannot contain '[' or ']'."); break;}
                case "already exists": {AlertBox.display("Answer already exists","The answer is already in the list."); break;}
                case "empty": {AlertBox.display("Answer is empty","The answer must not be empty."); break;}
            }
        });
        this.spokenLanguageAnswersRemoveButton.setText("Remove");
        this.spokenLanguageAnswersRemoveButton.setOnAction(event -> {
            if (this.spokenLanguageAnswersList.getSelectionModel().getSelectedItem() != null)
                this.spokenLanguageAnswersList.getItems().remove(this.spokenLanguageAnswersList.getSelectionModel().getSelectedItem());
            else
                AlertBox.display("No answer selected","Please select an answer first.");
        });
        this.spokenAnswersPane.disableProperty().setValue(this.audioData.getValue() == null || this.audioData.getValue().length() == 0);
        this.audioData.addListener((observable,oldValue,newValue) -> {this.spokenAnswersPane.disableProperty().setValue(this.audioData.getValue() == null || this.audioData.getValue().length() == 0);});
        // add/save button
        this.addSaveButton.setText((this.word == null ? "Add" : "Save"));
        this.addSaveButton.setOnAction(event -> {
            String validity;
            String targetLanguageDisplayWord = this.targetLanguageField.getText();
            switch (validDisplayWord(targetLanguageDisplayWord)) {
                case "valid": break;
                case "empty": {AlertBox.display("Empty word",this.targetLanguageAnswersLabel + " is empty."); return;}
                case "contains quotation mark": {AlertBox.display("Contains '\"'",this.targetLanguageAnswersLabel + " cannot contain '\"'."); return;}
            }
            String baseLanguageDisplayWord = this.baseLanguageField.getText();
            switch (validDisplayWord(baseLanguageDisplayWord)) {
                case "valid": break;
                case "empty": {AlertBox.display("Empty word",this.baseLanguageAnswersLabel + " is empty."); return;}
                case "contains quotation mark": {AlertBox.display("Contains '\"'",this.baseLanguageAnswersLabel + " cannot contain '\"'."); return;}
            }
            if (this.audioData.getValue() != null) {
                try {
                    Main.fileNum++;
                    //Util.makeTempFile("checkAudio.mp3",this.audioData.getValue());
                    Util.makeTempFile("checkAudio" + Main.fileNum + ".mp3",this.audioData.getValue());
                    //AudioClip audioClip = new AudioClip(new File("checkAudio.mp3").toURI().toString());
                    AudioClip audioClip = new AudioClip(new File("checkAudio" + Main.fileNum + ".mp3").toURI().toString());
                } catch (Exception e) {
                    AlertBox.display("Invalid audio file","The file is not a valid audio file.");
                    return;
                }
            }
            if (this.targetLanguageAnswersList.getItems().size() == 0) {
                AlertBox.display("Empty answer list",this.targetLanguageAnswersLabel + " is empty.");
                return;
            }
            if (this.baseLanguageAnswersList.getItems().size() == 0) {
                AlertBox.display("Empty answer list",this.baseLanguageAnswersLabel + " is empty.");
                return;
            }
            if (audioData.getValue() != null && this.spokenLanguageAnswersList.getItems().size() == 0) {
                AlertBox.display("Empty answer list",this.spokenAnswersLabel + " is empty.");
                return;
            }
            if (this.word == null)
                this.word = new Word(baseLanguage,targetLanguage,baseLanguageDisplayWord,targetLanguageDisplayWord,new ArrayList<String>(this.baseLanguageAnswersList.getItems()),new ArrayList<String>(this.targetLanguageAnswersList.getItems()),(audioData == null || audioData.getValue() == null ? null : new ArrayList<String>(this.spokenLanguageAnswersList.getItems())),audioData.getValue(),new int[] {0,0,0,0},new int[] {0,0,0,0},new int[] {0,0,0,0},new GregorianCalendar[]{new GregorianCalendar(),new GregorianCalendar(),new GregorianCalendar(),new GregorianCalendar()},new GregorianCalendar[]{new GregorianCalendar(),new GregorianCalendar(),new GregorianCalendar(),new GregorianCalendar()});
            else {
                this.word.setBaseLanguageDisplayWord(baseLanguageDisplayWord);
                this.word.setTargetLanguageDisplayWord(targetLanguageDisplayWord);
                this.word.setBaseLanguageAnswers(new ArrayList<String>(this.baseLanguageAnswersList.getItems()));
                this.word.setTargetLanguageAnswers(new ArrayList<String>(this.targetLanguageAnswersList.getItems()));
                this.word.setSpokenAnswers(new ArrayList<>(this.spokenLanguageAnswersList.getItems()));
                this.word.setAudioData(audioData.getValue());
            }
            window.close();
        });
        // cancel button
        this.cancelButton.setText("Cancel");
        this.cancelButton.setOnAction(event -> {
            this.word = null;
            window.close();
        });
        window.showAndWait();
        return this.word;
    }

    public String validDisplayWord(String s) {
        String answer = s.trim();
        if (answer.length() == 0) return "empty";
        else if (answer.contains("\"")) return "contains quotation mark";
        return "valid";
    }

    public String validAnswer(String s, List<String> existingAnswers) {
        String answer = s.trim();
        if (answer.length() == 0) return "empty";
        else if (answer.contains(",")) return "contains comma";
        else if (answer.contains("[") || answer.contains("]")) return "contains square bracket";
        else for (String existingAnswer : existingAnswers)
                if (answer.equals(existingAnswer))
                    return "already exists";
        return "valid";
    }

    @FXML
    private void chooseAudioButton() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose audio file");
        fileChooser.setInitialDirectory(new File("src/VocabApp/model/words"));
        File file = fileChooser.showOpenDialog(this.window);
        if (file == null) return;
        String data = Util.fileInHex(file);
        System.out.println(file.getAbsolutePath());
        boolean valid = true;
        try {
            Main.fileNum++;
            //Util.makeTempFile("checkAudio.mp3",data);
            Util.makeTempFile("checkAudio" + Main.fileNum + ".mp3",data);
            //AudioClip audioClip = new AudioClip(new File("checkAudio.mp3").toURI().toString());
            AudioClip audioClip = new AudioClip(new File("checkAudio" + Main.fileNum + ".mp3").toURI().toString());
        } catch (Exception e) {
            System.out.println("exception");
            valid = false;
        }
        if (valid) {
            this.audioData.setValue(data);
            loadAudioClip();
        } else
            AlertBox.display("Invalid audio","The audio file was either invalid or could not be loaded.");
    }

    @FXML
    private void playSoundButton() {
        if (this.audioClip != null) {
            this.audioClip.play();
        }
    }

    @FXML
    private void loadAudioClip() {
        Main.fileNum++;
        if (this.audioData == null) throw new IllegalStateException();
        this.audioClip = null;
        //Util.makeTempFile("wordAudio.mp3",this.audioData.getValue());
        Util.makeTempFile("wordAudio" + Main.fileNum + ".mp3",this.audioData.getValue());
        //this.audioClip = new AudioClip(new File("wordAudio.mp3").toURI().toString());
        this.audioClip = new AudioClip(new File("wordAudio" + Main.fileNum + ".mp3").toURI().toString());
    }

}
