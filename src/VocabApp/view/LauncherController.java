package VocabApp.view;

import VocabApp.Main;
import VocabApp.model.Word;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.media.AudioTrack;

import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.function.Function;

public class LauncherController {

    private Function<String,String> playButtonText = baseLanguage -> {
        switch (baseLanguage) {
            case "dan": return "Spil";
            case "deu": return "Spielen";
            case "eng": return "Play"; // verified
            case "fin": return "Pelaa";
            case "fra": return "Jouer";
            case "ita": return "Gioca";
            case "mri": return "";
            case "nld": return "Spelen";
            case "por": return "Jogar";
            case "spa": return "Jugar";
            case "swe": return "Spela";
            default: throw new IllegalArgumentException();
        }
    };

    private Function<String,String> editorButtonText = baseLanguage -> {
        switch (baseLanguage) {
            case "dan": return "";
            case "deu": return "Editor";
            case "eng": return "Editor"; // verified
            case "fin": return "Editori";
            case "fra": return "Éditeur";
            case "ita": return "Editor";
            case "mri": return "";
            case "nld": return "Editor";
            case "por": return "Editor";
            case "spa": return "Editor";
            case "swe": return "";
            default: throw new IllegalArgumentException();
        }
    };

    private Function<String,String> quitButtonText = baseLanguage -> {
        switch (baseLanguage) {
            case "dan": return "";
            case "deu": return "Beenden";
            case "eng": return "Quit"; // verified
            case "fin": return "";
            case "fra": return "Quitter";
            case "ita": return "";
            case "mri": return "";
            case "nld": return "";
            case "por": return "";
            case "spa": return "";
            case "swe": return "";
            default: throw new IllegalArgumentException();
        }
    };

    Main main;

    @FXML private Button playButton;
    @FXML private Button editorButton;
    @FXML private Button quitButton;
    @FXML private ChoiceBox choiceBox;

    public void setup() {
        // Initialise languages in choice box
        ArrayList<String> languages = new ArrayList<String>();
        for (String lang : Main.SUPPORTED_LANGUAGES) {
            languages.add(Main.langNames.apply(lang,lang));
        }
        languages.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (o1 == o2 || o1.equalsIgnoreCase(o2)) return 0;
                if (o1 == null || o1.length() == 0) return -1;
                if (o2 == null || o2.length() == 0) return 1;
                if ((""+o1.charAt(0)).equalsIgnoreCase(""+o2.charAt(0))) return compare(o1.substring(1),o2.substring(1));
                else return (""+o1.charAt(0)).toLowerCase().compareTo((""+o2.charAt(0)).toLowerCase());
            }
        });
        for (String lang : languages)
            choiceBox.getItems().add(lang);
        // Set default of choice box to baseLanguage if not null
        if (this.main.getBaseLanguage() != null)
            choiceBox.setValue(Main.langNames.apply(this.main.getBaseLanguage(),this.main.getBaseLanguage()));
        // Else set default value as English
        else {choiceBox.setValue("English"); main.setBaseLanguage("eng");}
        // Add Listener to choice box
        choiceBox.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    for (String lang : Main.SUPPORTED_LANGUAGES) {
                        if (Main.langNames.apply(lang,lang).equals(newValue))
                            this.main.setBaseLanguage(lang);
                    }
                    updateText();
                });
        updateText();
        this.main.getWindow().setWidth(300);
        this.main.getWindow().setHeight(300);
    }

    private void updateText() {
        this.playButton.setText(playButtonText.apply(this.main.getBaseLanguage()));
        this.editorButton.setText(editorButtonText.apply(this.main.getBaseLanguage()));
        this.quitButton.setText(quitButtonText.apply(this.main.getBaseLanguage()));
    }

    public void setMain(Main main) {
        this.main = main;
    }

    @FXML
    private void play() throws IOException {
        this.main.getWindow().hide();
        this.main.showSelectLanguagePlay();
    }

    @FXML
    private void editor() throws IOException {
        this.main.getWindow().hide();
        this.main.showSelectLanguageEditor();
    }

    @FXML
    private void quit() {
        try {
            for (Word word : this.main.getWords()) {
                word.updateStarsFromDate(Word.TARGET_TO_BASE_WRITTEN);
                word.updateStarsFromDate(Word.BASE_TO_TARGET_WRITTEN);
                word.updateStarsFromDate(Word.TARGET_TO_TARGET_SPOKEN);
                word.updateStarsFromDate(Word.TARGET_TO_BASE_SPOKEN);
            }
            this.main.saveWords();
            this.main.getWindow().close();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}