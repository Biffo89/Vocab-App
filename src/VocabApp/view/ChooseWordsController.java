package VocabApp.view;

import VocabApp.Main;
import VocabApp.model.Topic;
import VocabApp.model.Word;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;

public class ChooseWordsController {

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
    private Function<String,String> learningModeButtonText = baseLanguage -> {
        switch (baseLanguage) {
            case "dan": return "";
            case "deu": return "";
            case "eng": return "Choose learning mode"; // verified
            case "fin": return "";
            case "fra": return "Choisir la mode d'apprentissage";
            case "ita": return "";
            case "mri": return "";
            case "nld": return "";
            case "por": return "";
            case "spa": return "";
            case "swe": return "";
            default: throw new IllegalArgumentException();
        }
    };
    private Function<String,String> topicsText = baseLanguage -> {
        switch (baseLanguage) {
            case "dan": return "";
            case "deu": return "";
            case "eng": return "Topics"; // verified
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
    private Function<String,String> progressText = baseLanguage -> {
        switch (baseLanguage) {
            case "dan": return "";
            case "deu": return "";
            case "eng": return "Progress: "; // verified
            case "fin": return "";
            case "fra": return "Avancement : ";
            case "ita": return "";
            case "mri": return "";
            case "nld": return "";
            case "por": return "";
            case "spa": return "";
            case "swe": return "";
            default: throw new IllegalArgumentException();
        }
    };

    private Main main;

    private ObservableList<String> topics;

    @FXML private Button homeButton;
    @FXML private Button learningModeButton;
    @FXML private ListView topicList;
    @FXML private TitledPane topicPane;
    @FXML private TableColumn<Word,String> targetLanguageWord;
    @FXML private TableColumn<Word,String> baseLanguageWord;
    @FXML private TableView<Word> wordTable;
    @FXML private Label progressLabel;
    @FXML private Label langName;
    @FXML private ImageView flag;

    public void setMain(Main main) {
        this.main = main;
    }

    public void setup() {
        // Setup list of all topics for selected languages and sort
        this.topics = FXCollections.observableArrayList();
        for (Topic topic : this.main.getTopics()) {
            if (topic.baseLanguage.equals(this.main.getBaseLanguage()) && topic.targetLanguage.equals(this.main.getTargetLanguage())) {
                this.topics.add(topic.nameAsString() + " (" + topic.percentageDone() + "%)");
            }
        }
        this.topics.sort(Comparator.naturalOrder());
        this.topics.add(0, "All (" + this.main.getAll().percentageDone() + "%)");
        // set list view of topics to contain all topics
        this.topicList.setItems(this.topics);
        Function<String, Topic> findTopic = s -> {
            if (s == null || s.equals("All" + " (" + this.main.getAll().percentageDone() + "%)")) return this.main.getAll();
            for (Topic topic : this.main.getTopics()) {
                if (topic.baseLanguage.equals(this.main.getBaseLanguage()) && topic.targetLanguage.equals(this.main.getTargetLanguage()) && s.equals(topic.nameAsString() + " (" + topic.percentageDone() + "%)")) {
                    return topic;
                }
            }
            throw new NoSuchElementException(s);
        };
        // allow multiple selections
        this.topicList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        // action when selection changed
        this.topicList.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            this.main.setSelectedTopics(FXCollections.observableArrayList());
            for (Object topicName : this.topicList.getSelectionModel().getSelectedItems()) {
                this.main.getSelectedTopics().add(findTopic.apply((String) topicName));
            }
            this.main.setSelectedWords(FXCollections.observableArrayList());
            for (Topic topic : this.main.getSelectedTopics()) {
                if (topic == null) continue;
                for (Word word : topic) {
                    if (!this.main.getSelectedWords().contains(word)) {
                        this.main.getSelectedWords().add(word);
                    }
                }
            }
            this.main.getSelectedWords().sort((o1, o2) -> -(
                    o1.progressCompareTo(o2, Word.TARGET_TO_BASE_WRITTEN) +
                            o1.progressCompareTo(o2, Word.BASE_TO_TARGET_WRITTEN) +
                            o1.progressCompareTo(o2, Word.TARGET_TO_TARGET_SPOKEN) +
                            o1.progressCompareTo(o2, Word.TARGET_TO_BASE_SPOKEN)));
            this.wordTable.setItems(this.main.getSelectedWords());
            if (this.main.getSelectedWords() == null || this.main.getSelectedWords().isEmpty()) {
                this.learningModeButton.disableProperty().setValue(true);
            } else {
                this.learningModeButton.disableProperty().setValue(false);
            }
        }));
        // set up word table
        this.targetLanguageWord.setCellValueFactory(word -> new SimpleStringProperty(word.getValue().getTargetLanguageDisplayWord()));
        this.baseLanguageWord.setCellValueFactory(word -> new SimpleStringProperty(word.getValue().getBaseLanguageDisplayWord()));
        this.targetLanguageWord.setEditable(false);
        this.targetLanguageWord.setSortable(false);
        this.baseLanguageWord.setEditable(false);
        this.baseLanguageWord.setSortable(false);
        // Load from main if exists, else disable play button
        if (this.main.getSelectedTopics() != null && this.main.getSelectedWords() != null) {
            for (Topic topic : this.main.getSelectedTopics()) {
                this.topicList.getSelectionModel().select(topic.nameAsString() + " (" + topic.percentageDone() + "%)");
            }
            this.wordTable.setItems(this.main.getSelectedWords());
        }
        if (this.topicList.getSelectionModel().getSelectedItems().isEmpty()) {
            this.learningModeButton.disableProperty().setValue(true);
        }
        initialiseUI();
    }

    private void initialiseUI() {
        // set text for buttons
        this.homeButton.setText(this.homeButtonText.apply(this.main.getBaseLanguage()));
        this.learningModeButton.setText(this.learningModeButtonText.apply(this.main.getBaseLanguage()));
        // setup flag and lang name
        this.flag.setImage(new Image(Main.flags.apply(this.main.getTargetLanguage())));
        this.langName.setText(Main.langNames.apply(this.main.getBaseLanguage(),this.main.getTargetLanguage()));
        // Set progress label
        this.progressLabel.setText(this.progressText.apply(this.main.getBaseLanguage()) + this.main.getAll().percentageDone() + "%");
        // set list and column names
        this.baseLanguageWord.setText(Main.langNames.apply(this.main.getBaseLanguage(),this.main.getBaseLanguage()));
        this.targetLanguageWord.setText(Main.langNames.apply(this.main.getBaseLanguage(),this.main.getTargetLanguage()));
        this.topicPane.setText(this.topicsText.apply(this.main.getBaseLanguage()));
    }

    @FXML
    private void homeButton() throws IOException {
        this.main.showPlayHome();
    }

    @FXML
    private void modeButton() throws IOException {
        this.main.showSelectLearningMode();
    }

}
