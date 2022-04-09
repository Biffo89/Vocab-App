package VocabApp.view;

import VocabApp.Main;
import VocabApp.model.Results;
import VocabApp.model.Word;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ResultsController {

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
    private Function<String,String> playAgainButtonText = baseLanguage -> {
        switch (baseLanguage) {
            case "dan": return "";
            case "deu": return "";
            case "eng": return "Play again"; // verified
            case "fin": return "";
            case "fra": return "";
            case "ita": return "";
            case "mri": return "";
            case "nld": return "";
            case "por": return "";
            case "spa": return "";
            case "swe": return "";
            default: throw new IllegalArgumentException();
        }
    };
    private Function<String,String> percentageRightColumnText = baseLanguage -> {
        switch (baseLanguage) {
            case "dan": return "";
            case "deu": return "";
            case "eng": return "Percentage right"; // verified
            case "fin": return "";
            case "fra": return "";
            case "ita": return "";
            case "mri": return "";
            case "nld": return "";
            case "por": return "";
            case "spa": return "";
            case "swe": return "";
            default: throw new IllegalArgumentException();
        }
    };
    private BiFunction<String,String,String> targetLanguageColumnText = (baseLanguage, targetLanguage) -> {
        String target = Main.langNames.apply(baseLanguage,targetLanguage);
        switch (baseLanguage) {
            case "dan": return "";
            case "deu": return "";
            case "eng": return "Word in " + target; // verified
            case "fin": return "";
            case "fra": return "";
            case "ita": return "";
            case "mri": return "";
            case "nld": return "";
            case "por": return "";
            case "spa": return "";
            case "swe": return "";
            default: throw new IllegalArgumentException();
        }
    };
    private Function<String,String> baseLanguageColumnText = baseLanguage -> {
        String base = Main.langNames.apply(baseLanguage,baseLanguage);
        switch (baseLanguage) {
            case "dan": return "";
            case "deu": return "";
            case "eng": return "Word in " + base; // verified
            case "fin": return "";
            case "fra": return "";
            case "ita": return "";
            case "mri": return "";
            case "nld": return "";
            case "por": return "";
            case "spa": return "";
            case "swe": return "";
            default: throw new IllegalArgumentException();
        }
    };
    private Function<String,String> starColumnText = baseLanguage -> {
        switch (baseLanguage) {
            case "dan": return "";
            case "deu": return "";
            case "eng": return "Stars"; // verified
            case "fin": return "";
            case "fra": return "";
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
    @FXML private Button chooseWordsButton;
    @FXML private Button homeButton;
    @FXML private TableView<Word> wordTable;
    @FXML private ImageView flag;
    @FXML private Label langName;
    @FXML private Label percentageRightLabel;
    @FXML private TableColumn<Word,String> percentageRightColumn;
    @FXML private TableColumn<Word,String> targetLanguageColumn;
    @FXML private TableColumn<Word,String> baseLanguageColumn;
    @FXML private TableColumn<Word,ImageView> starColumn;

    public void setMain(Main main) {
        this.main = main;
    }

    public void setup(Results results) {
        initialiseUI();
        this.wordTable.setItems(FXCollections.observableArrayList(results.toWords()));
        this.wordTable.setRowFactory(table -> new TableRow<Word>() {
            @Override
            public void updateItem(Word word, boolean empty) {
                super.updateItem(word, empty);
                if (word != null) {
                    double percentageRight = results.percentageRight(word);
                    int red = (int) (255*Math.min(-2*percentageRight+2,1));
                    int green = (int) (255*Math.min(2*percentageRight,1));
                    setStyle("-fx-background-color: rgb(" + red + ", " + green + ", 0);");
                }
            }
        });
        this.percentageRightColumn.setCellValueFactory(word -> {
            int percentValue = (int) (results.percentageRight(word.getValue()) * 100);
            return new SimpleStringProperty(percentValue + "%");
        });
        this.targetLanguageColumn.setCellValueFactory(word -> new SimpleStringProperty(word.getValue().getTargetLanguageDisplayWord()));
        this.baseLanguageColumn.setCellValueFactory(word -> new SimpleStringProperty(word.getValue().getBaseLanguageDisplayWord()));
        this.starColumn.setCellValueFactory(word -> new SimpleObjectProperty<ImageView>(new ImageView(word.getValue().getBlackStar(this.main.getLearningMode()))));
        int percentageValue = (int)(results.percentageRight() * 100);
        this.percentageRightLabel.setText(percentageValue + "%");
        this.percentageRightLabel.setTextFill(new Color(Math.min(-2*results.percentageRight()+2,1),Math.min(2*results.percentageRight(),1),0,1));
    }

    private void initialiseUI() {
        this.playButton.setText(playAgainButtonText.apply(this.main.getBaseLanguage()));
        this.homeButton.setText(homeButtonText.apply(this.main.getBaseLanguage()));
        this.chooseWordsButton.setText(chooseWordsButtonText.apply(this.main.getBaseLanguage()));
        this.flag.setImage(new Image(Main.flags.apply(this.main.getTargetLanguage())));
        this.langName.setText(Main.langNames.apply(this.main.getBaseLanguage(),this.main.getTargetLanguage()));
        this.percentageRightColumn.setText(percentageRightColumnText.apply(this.main.getBaseLanguage()));
        this.targetLanguageColumn.setText(targetLanguageColumnText.apply(this.main.getBaseLanguage(),this.main.getTargetLanguage()));
        this.baseLanguageColumn.setText(baseLanguageColumnText.apply(this.main.getBaseLanguage()));
        this.starColumn.setText(starColumnText.apply(this.main.getBaseLanguage()));
    }

    @FXML
    private void playButton() throws IOException {
        this.main.showSelectLearningMode();
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
