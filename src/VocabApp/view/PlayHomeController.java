package VocabApp.view;

import VocabApp.Main;
import VocabApp.model.Topic;
import VocabApp.model.Word;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.function.Function;

public class PlayHomeController {

    private Function<String,String> menuButtonText = baseLanguage -> {
        switch (baseLanguage) {
            case "dan": return "⟵ Menu";
            case "deu": return "⟵ Menü";
            case "eng": return "⟵ Menu"; // verified
            case "fin": return "⟵ Valikko";
            case "fra": return "⟵ Menu";
            case "ita": return "⟵ Menu";
            case "mri": return "⟵ Rārangi tono";
            case "nld": return "⟵ Menu";
            case "por": return "⟵ Menu";
            case "spa": return "⟵ Menú";
            case "swe": return "⟵ Meny";
            default: throw new IllegalArgumentException();
        }
    };

    private Function<String,String> selectLanguageButtonText = baseLanguage -> {
        switch(baseLanguage) {
            case "dan": return "";
            case "deu": return "";
            case "eng": return "Select language"; // verified
            case "fin": return "";
            case "fra": return "Sélectionner la langue";
            case "ita": return "";
            case "mri": return "";
            case "nld": return "";
            case "por": return "";
            case "spa": return "";
            case "swe": return "";
            default: throw new IllegalArgumentException();
        }
    };

    private Function<String,String> startButtonText = baseLanguage -> {
        switch(baseLanguage) {
            case "dan": return "";
            case "deu": return "";
            case "eng": return "Start"; // verified
            case "fin": return "";
            case "fra": return "Commencer";
            case "ita": return "";
            case "mri": return "";
            case "nld": return "";
            case "por": return "";
            case "spa": return "";
            case "swe": return "";
            default: throw new IllegalArgumentException();
        }
    };

    private Function<String,String> scoreLabelText = baseLanguage -> {
        switch (baseLanguage) {
            case "dan": return "";
            case "deu": return "";
            case "eng": return "Score: "; // verified
            case "fin": return "";
            case "fra": return "Score : ";
            case "ita": return "";
            case "mri": return "";
            case "nld": return "";
            case "por": return "";
            case "spa": return "";
            case "swe": return "";
            default: throw new IllegalArgumentException();
        }
    };

    @FXML private ImageView background;
    @FXML private Label scoreLabel;
    @FXML private Label langName;
    @FXML private ImageView flag;
    @FXML private Button startButton;
    @FXML private Button menuButton;
    @FXML private Button selectLanguageButton;

    private Main main;

    /**
     * To be called after main has been set.
     */
    public void setup() {
        // set all
        if (this.main.getAll() == null) {
            Topic all = new Topic("All", this.main.getBaseLanguage(), this.main.getTargetLanguage());
            for (Word word : this.main.getWords())
                if (word.getBaseLanguage().equals(all.baseLanguage) && word.getTargetLanguage().equals(all.targetLanguage))
                    all.add(word);
            this.main.setAll(all);
        }
        initialiseUI();
        this.main.getWindow().setWidth(1280);
        this.main.getWindow().setHeight(720);
    }

    private void initialiseUI() {
        // set buttons text
        menuButton.setText(menuButtonText.apply(this.main.getBaseLanguage()));
        selectLanguageButton.setText(selectLanguageButtonText.apply(this.main.getBaseLanguage()));
        startButton.setText(startButtonText.apply(this.main.getBaseLanguage()));
        // set background
        String uri = Main.backgroundImage.apply(main.getTargetLanguage());
        if (uri != null) background.setImage(new Image(uri));
        // set flag
        this.flag.setImage(new Image(Main.flags.apply(this.main.getTargetLanguage())));
        // set langName
        this.langName.setText(Main.langNames.apply(main.getBaseLanguage(),main.getTargetLanguage()));
        this.scoreLabel.setText(scoreLabelText.apply(this.main.getBaseLanguage()) + this.main.getAll().getScore());
    }

    public void setMain(Main main) {
        this.main = main;
    }

    @FXML
    private void menuButton() throws IOException {
        this.main.getWindow().hide();
        this.main.showLauncher();
    }

    @FXML
    private void selectLanguageButton() throws IOException {
        this.main.getWindow().hide();
        this.main.showSelectLanguagePlay();
    }

    @FXML
    private void startButton() throws IOException {
        this.main.showChooseWords();
    }

}
