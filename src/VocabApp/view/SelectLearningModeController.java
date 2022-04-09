package VocabApp.view;

import VocabApp.Main;
import VocabApp.model.Word;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static VocabApp.util.Util.capitalise;

public class SelectLearningModeController {

    private Function<String,String> homeButtonText = baseLanguage -> {
        switch (baseLanguage) {
            case "dan": return "⟵ ";
            case "deu": return "⟵ ";
            case "eng": return "⟵ Home"; // verified
            case "fin": return "⟵ ";
            case "fra": return "⟵ Accueil";
            case "ita": return "⟵ ";
            case "mri": return "⟵ ";
            case "nld": return "⟵ ";
            case "por": return "⟵ ";
            case "spa": return "⟵ ";
            case "swe": return "⟵ ";
            default: throw new IllegalArgumentException();
        }
    };
    private Function<String,String> chooseWordsButtonText = baseLanguage -> {
        switch (baseLanguage) {
            case "dan": return "";
            case "deu": return "";
            case "eng": return "Choose words"; // verified
            case "fin": return "";
            case "fra": return "Choisir les mots";
            case "ita": return "";
            case "mri": return "";
            case "nld": return "";
            case "por": return "";
            case "spa": return "";
            case "swe": return "";
            default: throw new IllegalArgumentException();
        }
    };
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
    private BiFunction<String,String,String> writtenTargetToBaseButtonText = (baseLanguage, targetLanguage) -> {
        String base = Main.langNames.apply(baseLanguage,baseLanguage);
        String target = Main.langNames.apply(baseLanguage,targetLanguage);
        switch (baseLanguage) {
            case "dan": return "";
            case "deu": return "";
            case "eng": return "Written " + target + " to " + base; // verified
            case "fin": return "";
            case "fra": return capitalise(target) + " écrit à " + base;
            case "ita": return "";
            case "mri": return "";
            case "nld": return "";
            case "por": return "";
            case "spa": return "";
            case "swe": return "";
            default: throw new IllegalArgumentException();
        }
    };
    private BiFunction<String,String,String> writtenBaseToTargetButtonText = (baseLanguage, targetLanguage) -> {
        String base = Main.langNames.apply(baseLanguage,baseLanguage);
        String target = Main.langNames.apply(baseLanguage,targetLanguage);
        switch (baseLanguage) {
            case "dan": return "";
            case "deu": return "";
            case "eng": return "Written " + base + " to " + target; // verified
            case "fin": return "";
            case "fra": return capitalise(base) + " écrit à " + target;
            case "ita": return "";
            case "mri": return "";
            case "nld": return "";
            case "por": return "";
            case "spa": return "";
            case "swe": return "";
            default: throw new IllegalArgumentException();
        }
    };
    private BiFunction<String,String,String> spokenTargetToTargetButtonText = (baseLanguage, targetLanguage) -> {
        String target = Main.langNames.apply(baseLanguage,targetLanguage);
        switch (baseLanguage) {
            case "dan": return "";
            case "deu": return "";
            case "eng": return "Spoken " + target + " to " + target; // verified
            case "fin": return "";
            case "fra": return capitalise(target) + " oral à " + target;
            case "ita": return "";
            case "mri": return "";
            case "nld": return "";
            case "por": return "";
            case "spa": return "";
            case "swe": return "";
            default: throw new IllegalArgumentException();
        }
    };
    private BiFunction<String,String,String> spokenTargetToBaseButtonText = (baseLanguage, targetLanguage) -> {
        String base = Main.langNames.apply(baseLanguage,baseLanguage);
        String target = Main.langNames.apply(baseLanguage,targetLanguage);
        switch (baseLanguage) {
            case "dan": return "";
            case "deu": return "";
            case "eng": return "Spoken " + target + " to " + base; // verified
            case "fin": return "";
            case "fra": return capitalise(target) + " oral à " + base;
            case "ita": return "";
            case "mri": return "";
            case "nld": return "";
            case "por": return "";
            case "spa": return "";
            case "swe": return "";
            default: throw new IllegalArgumentException();
        }
    };
    private Function<String,String> sliderText = baseLanguage -> {
        switch (baseLanguage) {
            case "dan": return "";
            case "deu": return "";
            case "eng": return "Words per game: "; // verified
            case "fin": return "";
            case "fra": return "Mots du jeu : ";
            case "ita": return "";
            case "mri": return "";
            case "nld": return "";
            case "por": return "";
            case "spa": return "";
            case "swe": return "";
            default: throw new IllegalArgumentException();
        }
    };

    public Main main;

    private TableColumn<Word,String> targetLanguageColumn;
    private TableColumn<Word,String> baseLanguageColumn;
    private TableColumn<Word,ImageView> writtenTargetToBaseStarColumn;
    private TableColumn<Word,ImageView> writtenBaseToTargetStarColumn;
    private TableColumn<Word,ImageView> spokenTargetToTargetStarColumn;
    private TableColumn<Word,ImageView> spokenTargetToBaseStarColumn;

    @FXML private Button playButton;
    @FXML private Button homeButton;
    @FXML private Button chooseWordsButton;
    @FXML private TableView<Word> table;
    @FXML private ImageView flag;
    @FXML private Label langName;
    @FXML private RadioButton writtenTargetToBaseButton;
    @FXML private Label writtenTargetToBaseLabel;
    @FXML private RadioButton writtenBaseToTargetButton;
    @FXML private Label writtenBaseToTargetLabel;
    @FXML private RadioButton spokenTargetToTargetButton;
    @FXML private Label spokenTargetToTargetLabel;
    @FXML private RadioButton spokenTargetToBaseButton;
    @FXML private Label spokenTargetToBaseLabel;
    @FXML private ToggleGroup modeToggleGroup;
    @FXML private Slider slider;
    @FXML private Label sliderLabel;

    public void setMain(Main main) {
        this.main = main;
    }

    public void setup() {
        // update stars
        for (Word word : this.main.getSelectedWords()) {
            word.updateStarsFromDate(Word.TARGET_TO_BASE_WRITTEN);
            word.updateStarsFromDate(Word.BASE_TO_TARGET_WRITTEN);
            word.updateStarsFromDate(Word.TARGET_TO_TARGET_SPOKEN);
            word.updateStarsFromDate(Word.TARGET_TO_BASE_SPOKEN);
        }
        // set spoken list
        this.main.setSelectedSpokenWords(FXCollections.observableArrayList());
        for (Word word : this.main.getSelectedWords())
            if (word.getAudioData() != null)
                this.main.getSelectedSpokenWords().add(word);
        // disable play button until mode is selected
        this.playButton.disableProperty().setValue(true);
        // setup table
        this.targetLanguageColumn = new TableColumn<Word,String>("");
        this.baseLanguageColumn = new TableColumn<Word,String>("");
        this.writtenTargetToBaseStarColumn = new TableColumn<Word,ImageView>("");
        this.writtenBaseToTargetStarColumn = new TableColumn<Word,ImageView>("");
        this.spokenTargetToTargetStarColumn = new TableColumn<Word,ImageView>("");
        this.spokenTargetToBaseStarColumn = new TableColumn<Word,ImageView>("");
        this.targetLanguageColumn.setCellValueFactory(word -> new SimpleStringProperty(word.getValue().getTargetLanguageDisplayWord()));
        this.targetLanguageColumn.setSortable(false);
        this.targetLanguageColumn.setEditable(false);
        this.baseLanguageColumn.setCellValueFactory(word -> new SimpleStringProperty(word.getValue().getBaseLanguageDisplayWord()));
        this.baseLanguageColumn.setSortable(false);
        this.baseLanguageColumn.setEditable(false);
        this.writtenTargetToBaseStarColumn.setCellValueFactory(word -> new SimpleObjectProperty<ImageView>(new ImageView(word.getValue().getBlackStar(Word.TARGET_TO_BASE_WRITTEN))));
        this.writtenTargetToBaseStarColumn.setSortable(false);
        this.writtenTargetToBaseStarColumn.setEditable(false);
        this.writtenBaseToTargetStarColumn.setCellValueFactory(word -> new SimpleObjectProperty<ImageView>(new ImageView(word.getValue().getBlackStar(Word.BASE_TO_TARGET_WRITTEN))));
        this.writtenBaseToTargetStarColumn.setSortable(false);
        this.writtenBaseToTargetStarColumn.setEditable(false);
        this.spokenTargetToTargetStarColumn.setCellValueFactory(word -> new SimpleObjectProperty<ImageView>(new ImageView(word.getValue().getBlackStar(Word.TARGET_TO_TARGET_SPOKEN))));
        this.spokenTargetToTargetStarColumn.setSortable(false);
        this.spokenTargetToTargetStarColumn.setEditable(false);
        this.spokenTargetToBaseStarColumn.setCellValueFactory(word -> new SimpleObjectProperty<ImageView>(new ImageView(word.getValue().getBlackStar(Word.TARGET_TO_BASE_SPOKEN))));
        this.spokenTargetToBaseStarColumn.setSortable(false);
        this.spokenTargetToBaseStarColumn.setEditable(false);

        this.table.getColumns().add(this.targetLanguageColumn);
        this.table.getColumns().add(this.baseLanguageColumn);

        this.table.setItems(this.main.getSelectedWords());
        // set up slider
        slider.valueProperty().addListener((observable,oldValue,newValue) -> {
            this.main.setWordsPerGame((int) Math.round((double) newValue));
            this.sliderLabel.setText(sliderText.apply(this.main.getBaseLanguage()) + (int) Math.round((double) newValue));
        });
        // if possible, grab info from main
        if (this.main.getLearningMode() != -1) {
            switch (this.main.getLearningMode()) {
                case Word.TARGET_TO_BASE_WRITTEN: {this.modeToggleGroup.selectToggle(writtenTargetToBaseButton); writtenTargetToBaseButton(); break;}
                case Word.BASE_TO_TARGET_WRITTEN: {this.modeToggleGroup.selectToggle(writtenBaseToTargetButton); writtenBaseToTargetButton(); break;}
                case Word.TARGET_TO_TARGET_SPOKEN: {this.modeToggleGroup.selectToggle(spokenTargetToTargetButton); spokenTargetToTargetButton(); break;}
                case Word.TARGET_TO_BASE_SPOKEN: {this.modeToggleGroup.selectToggle(spokenTargetToBaseButton); spokenTargetToBaseButton(); break;}
                default: throw new AssertionError();
            }
            this.playButton.disableProperty().setValue(this.table.getItems().size() == 0);
        }
        if (this.main.getWordsPerGame() >= 20 && this.main.getWordsPerGame() <= 100) {
            this.slider.setValue(this.main.getWordsPerGame());
            this.sliderLabel.setText(sliderText.apply(this.main.getBaseLanguage()) + this.main.getWordsPerGame());
        } else {
            this.main.setWordsPerGame(20);
            this.sliderLabel.setText(sliderText.apply(this.main.getBaseLanguage()) + this.main.getWordsPerGame());
        }
        if (this.main.getWordsPerGame() == 0) throw new AssertionError();
        initialiseUI();
    }

    private void initialiseUI() {
        // set lang name and flags
        this.flag.setImage(new Image(Main.flags.apply(this.main.getTargetLanguage())));
        this.langName.setText(Main.langNames.apply(this.main.getBaseLanguage(),this.main.getTargetLanguage()));
        // set button text
        homeButton.setText(homeButtonText.apply(this.main.getBaseLanguage()));
        chooseWordsButton.setText(chooseWordsButtonText.apply(this.main.getBaseLanguage()));
        playButton.setText(playButtonText.apply(this.main.getBaseLanguage()));
        // set radio button text
        writtenTargetToBaseButton.setText(writtenTargetToBaseButtonText.apply(this.main.getBaseLanguage(),this.main.getTargetLanguage()));
        writtenBaseToTargetButton.setText(writtenBaseToTargetButtonText.apply(this.main.getBaseLanguage(),this.main.getTargetLanguage()));
        spokenTargetToTargetButton.setText(spokenTargetToTargetButtonText.apply(this.main.getBaseLanguage(),this.main.getTargetLanguage()));
        spokenTargetToBaseButton.setText(spokenTargetToBaseButtonText.apply(this.main.getBaseLanguage(),this.main.getTargetLanguage()));
        // set up radio button labels
        writtenTargetToBaseLabel.setText(percentageDone(this.main.getSelectedWords(),Word.TARGET_TO_BASE_WRITTEN) + "%");
        writtenBaseToTargetLabel.setText(percentageDone(this.main.getSelectedWords(),Word.BASE_TO_TARGET_WRITTEN) + "%");
        spokenTargetToTargetLabel.setText(percentageDone(this.main.getSelectedSpokenWords(),Word.TARGET_TO_TARGET_SPOKEN) + "%");
        spokenTargetToBaseLabel.setText(percentageDone(this.main.getSelectedSpokenWords(),Word.TARGET_TO_BASE_SPOKEN) + "%");
        // table columns
        this.targetLanguageColumn.setText(Main.langNames.apply(this.main.getBaseLanguage(),this.main.getTargetLanguage()));
        this.baseLanguageColumn.setText(Main.langNames.apply(this.main.getBaseLanguage(),this.main.getBaseLanguage()));
        this.writtenTargetToBaseStarColumn.setText(writtenTargetToBaseButtonText.apply(this.main.getBaseLanguage(),this.main.getTargetLanguage()) + " stars");
        this.writtenBaseToTargetStarColumn.setText(writtenBaseToTargetButtonText.apply(this.main.getBaseLanguage(),this.main.getTargetLanguage()) + " stars");
        this.spokenTargetToTargetStarColumn.setText(spokenTargetToTargetButtonText.apply(this.main.getBaseLanguage(),this.main.getTargetLanguage()) + " stars");
        this.spokenTargetToBaseStarColumn.setText(spokenTargetToBaseButtonText.apply(this.main.getBaseLanguage(),this.main.getTargetLanguage()) + " stars");
    }

    public int percentageDone(List<Word> list, int learningMode) {
        double score = 0;
        double potentialScore = 0;
        for (Word word : list) {
            score += word.getScore(learningMode);
            potentialScore += word.getPotentialScore(learningMode);
        }
        return (int) (100*score/potentialScore);
    }

    private void removeStarColumns() {
        if (this.table.getColumns().contains(this.writtenTargetToBaseStarColumn))
            this.table.getColumns().remove(this.writtenTargetToBaseStarColumn);
        if (this.table.getColumns().contains(this.writtenBaseToTargetStarColumn))
            this.table.getColumns().remove(this.writtenBaseToTargetStarColumn);
        if (this.table.getColumns().contains(this.spokenTargetToTargetStarColumn))
            this.table.getColumns().remove(this.spokenTargetToTargetStarColumn);
        if (this.table.getColumns().contains(this.spokenTargetToBaseStarColumn))
            this.table.getColumns().remove(this.spokenTargetToBaseStarColumn);
    }

    @FXML
    private void writtenTargetToBaseButton() {
        removeStarColumns();
        this.table.getColumns().add(this.writtenTargetToBaseStarColumn);
        this.table.setItems(this.main.getSelectedWords());
        this.main.setLearningMode(Word.TARGET_TO_BASE_WRITTEN);
        this.playButton.disableProperty().setValue(this.table.getItems().size() == 0);
    }

    @FXML
    private void writtenBaseToTargetButton() {
        removeStarColumns();
        this.table.getColumns().add(this.writtenBaseToTargetStarColumn);
        this.table.setItems(this.main.getSelectedWords());
        this.main.setLearningMode(Word.BASE_TO_TARGET_WRITTEN);
        this.playButton.disableProperty().setValue(this.table.getItems().size() == 0);
    }

    @FXML
    private void spokenTargetToTargetButton() {
        removeStarColumns();
        this.table.getColumns().add(this.spokenTargetToTargetStarColumn);
        this.table.setItems(this.main.getSelectedSpokenWords());
        this.main.setLearningMode(Word.TARGET_TO_TARGET_SPOKEN);
        this.playButton.disableProperty().setValue(this.table.getItems().size() == 0);
    }

    @FXML
    private void spokenTargetToBaseButton() {
        removeStarColumns();
        this.table.getColumns().add(this.spokenTargetToBaseStarColumn);
        this.table.setItems(this.main.getSelectedSpokenWords());
        this.main.setLearningMode(Word.TARGET_TO_BASE_SPOKEN);
        this.playButton.disableProperty().setValue(this.table.getItems().size() == 0);
    }

    @FXML
    private void playButton() throws IOException {
        this.main.showPlay();
    }

    @FXML
    private void homeButton() throws IOException {
        this.main.showPlayHome();
    }

    @FXML
    private void chooseWordsButton() throws IOException {
        this.main.showChooseWords();
    }

}
