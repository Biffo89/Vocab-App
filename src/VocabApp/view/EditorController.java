package VocabApp.view;

import VocabApp.Main;
import VocabApp.model.Topic;
import VocabApp.model.Word;
import VocabApp.util.AlertBox;
import VocabApp.util.ConfirmBox;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static VocabApp.view.TopicEditor.display;


public class EditorController {

    @FXML private Button menuButton;
    @FXML private Button selectLanguageButton;
    @FXML private Label title;
    @FXML private Label languageName;
    @FXML private ImageView flag;
    @FXML private TitledPane topicPane;
    @FXML private ListView<String> topicView;
    @FXML private TitledPane wordPane;
    @FXML private TableView<Word> wordView;
    @FXML private TableColumn<Word,String> targetColumn;
    @FXML private TableColumn<Word,String> baseColumn;
    @FXML private TableColumn<Word,String> audioColumn;
    @FXML private Button addTopicButton;
    @FXML private Button editTopicButton;
    @FXML private Button importTopicButton;
    @FXML private Button exportTopicButton;
    @FXML private Button deleteTopicButton;
    @FXML private Button addWordButton;
    @FXML private Button editWordButton;
    @FXML private Button removeWordsButton;
    @FXML private Button deleteWordsButton;
    @FXML private Button addSelectedWordsButton;

    private Main main;
    private ArrayList<Topic> topics;
    private Topic selectedTopic;
    private ObservableList<String> topicNames;

    public void setMain(Main main) {
        this.main = main;
    }

    public void setup() {
        // all
        this.main.setAll(new Topic("All",this.main.getBaseLanguage(),this.main.getTargetLanguage()));
        for (Word word : this.main.getWords())
            if (word.getBaseLanguage().equals(this.main.getBaseLanguage()) && word.getTargetLanguage().equals(this.main.getTargetLanguage()))
                this.main.getAll().add(word);
        // topics
        this.topics = new ArrayList<Topic>();
        for (Topic topic : this.main.getTopics())
            if (topic.baseLanguage.equals(this.main.getBaseLanguage()) && topic.targetLanguage.equals(this.main.getTargetLanguage()))
                this.topics.add(topic);
        this.topics.sort(Comparator.comparing(Topic::nameAsString));
        this.topics.add(0,this.main.getAll());
        // topicNames
        this.topicNames = FXCollections.observableArrayList();
        for (Topic topic : this.topics)
            this.topicNames.add(topic.nameAsString());
        // menuButton
        this.menuButton.setText("⟵ Menu");
        // selectLanguageButton
        this.selectLanguageButton.setText("Select Language");
        // title
        this.title.setText("Word Editor");
        // languageName
        this.languageName.setText(Main.langNames.apply(this.main.getBaseLanguage(),this.main.getTargetLanguage()));
        // flag
        this.flag.setImage(new Image(Main.flags.apply(this.main.getTargetLanguage())));
        // topicPane
        this.topicPane.setText("Topics");
        // topicView
        this.topicView.setItems(this.topicNames);
        this.topicView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue == null) {
                this.selectedTopic = null;
            } else if (newValue.equals("All")) {
                this.selectedTopic = this.main.getAll();
            } else {
                for (Topic topic : this.topics)
                    if (newValue.equals(topic.nameAsString())) {
                        this.selectedTopic = topic;
                        break;
                    }
            }
            this.wordView.setItems(FXCollections.observableArrayList());
            if (this.selectedTopic != null)
                for (Word word : this.selectedTopic)
                    this.wordView.getItems().add(word);
            this.wordView.getItems().sort(Word::compareTo);
        }));
        // wordPane
        this.wordPane.setText("Words");
        // wordView
        this.targetColumn.setText(Main.langNames.apply(this.main.getBaseLanguage(),this.main.getTargetLanguage()) + " Word");
        this.baseColumn.setText(Main.langNames.apply(this.main.getBaseLanguage(),this.main.getBaseLanguage()) + " Word");
        this.audioColumn.setText("Audio File");
        this.targetColumn.setCellValueFactory(word -> new SimpleStringProperty(word.getValue().getTargetLanguageDisplayWord()));
        this.baseColumn.setCellValueFactory(word -> new SimpleStringProperty(word.getValue().getBaseLanguageDisplayWord()));
        //this.audioColumn.setCellValueFactory(word -> new SimpleStringProperty(word.getValue().getTargetAudioLocation()));
        this.wordView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        // buttons
        this.addTopicButton.setText("Add New Topic...");
        this.editTopicButton.setText("Edit Topic...");
        this.importTopicButton.setText("Import Topic");
        this.exportTopicButton.setText("Export Topic");
        this.deleteTopicButton.setText("Delete Topic");
        this.addWordButton.setText("Add New Word...");
        this.editWordButton.setText("Edit Word...");
        this.removeWordsButton.setText("Remove Word(s)");
        this.deleteWordsButton.setText("Delete Word(s)");
        this.addSelectedWordsButton.setText("Add Selected Words to...");
        this.main.getWindow().setWidth(1280);
        this.main.getWindow().setHeight(720);
    }

    @FXML
    private void menuButton() throws IOException {
        this.main.showLauncher();
    }

    @FXML
    private void selectLanguageButton() throws IOException {
        this.main.showSelectLanguageEditor();
    }

    @FXML
    private void handleAddTopicButton() throws IOException {
        String newTopicName = display(this.topicNames,null);
        if (newTopicName != null) {
            Topic newTopic = new Topic(newTopicName,this.main.getBaseLanguage(),this.main.getTargetLanguage());
            this.main.getTopics().add(newTopic);
            this.topics.add(newTopic);
            this.topicNames.add(newTopicName);
            this.topicNames.remove("All");
            this.topicNames.sort(Comparator.naturalOrder());
            this.topicNames.add(0,"All");
        }
    }

    @FXML
    private void handleEditTopicButton() throws IOException {
        Topic editingTopic = this.selectedTopic;
        if (editingTopic == null) {
            AlertBox.display("No Topic Selected","Please pick a topic first.");
        } else if (editingTopic.equals(this.main.getAll())) {
            AlertBox.display("All Selected","All cannot be modified. Please pick another topic.");
        } else {
            ArrayList<Topic> listWithoutSelected = new ArrayList<Topic>(this.topics);
            listWithoutSelected.remove(editingTopic);
            ArrayList<String> forbiddenStrings = new ArrayList<String>();
            for (Topic topic : listWithoutSelected)
                forbiddenStrings.add(topic.nameAsString());
            String newTopicName = TopicEditor.display(forbiddenStrings,editingTopic.nameAsString());
            if (newTopicName != null) {
                this.topicNames.remove(editingTopic.nameAsString());
                editingTopic.setName(newTopicName);
                this.topicNames.remove("All");
                this.topicNames.add(newTopicName);
                this.topicNames.sort(Comparator.naturalOrder());
                this.topicNames.add(0,"All");
            }
        }
    }

    @FXML
    private void handleDeleteTopicButton() {
        if (this.selectedTopic == null) {
            AlertBox.display("No Topic Selected","Please pick a topic first.");
        } else if (this.selectedTopic.equals(this.main.getAll())) {
            AlertBox.display("All Selected", "All cannot be deleted. Please pick another topic.");
        } else {
            this.main.getTopics().remove(this.selectedTopic);
            this.topics.remove(this.selectedTopic);
            this.topicNames.remove(this.selectedTopic.getName());
        }
    }

    @FXML
    private void handleAddWordButton() throws IOException {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/WordEditor.fxml"));
        Scene scene = new Scene(loader.load(), 640, 480);
        stage.setScene(scene);
        WordEditorController controller = loader.getController();
        Word word = controller.setup(null, this.main.getBaseLanguage(), this.main.getTargetLanguage(), stage);
        if (word != null) {
            this.main.getWords().add(word);
            this.main.getAll().add(word);
            if (this.selectedTopic != null)
                if (!this.selectedTopic.equals(this.main.getAll()))
                    this.selectedTopic.add(word);
                this.wordView.getItems().add(word);
        }
    }

    @FXML
    private void handleEditWordButton() throws IOException {
        if (this.wordView.getSelectionModel().getSelectedItems().size() == 1) {
            Word word = this.wordView.getSelectionModel().getSelectedItems().get(0);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/WordEditor.fxml"));
            Scene scene = new Scene(loader.load(), 640, 480);
            stage.setScene(scene);
            WordEditorController controller = loader.getController();
            controller.setup(word, this.main.getBaseLanguage(), this.main.getTargetLanguage(), stage);
        } else if (this.wordView.getSelectionModel().getSelectedItems().size() == 0) {
            AlertBox.display("No word selected","Please select a word first.");
        } else {
            AlertBox.display("Multiple words selected","Please select only one word.");
        }
    }

    @FXML
    private void handleRemoveWordsButton() {
        if (this.selectedTopic == null) {
            AlertBox.display("No topic selected","Please select a topic first.");
        } else if (this.wordView.getSelectionModel().getSelectedItems().size() == 0) {
            AlertBox.display("No words selected","Please select words to remove first.");
        } else if (this.selectedTopic.equals(this.main.getAll())) {
            AlertBox.display("All selected","All cannot be modified. Please select a different topic.");
        } else {
            boolean remove = ConfirmBox.display("Confirm remove words","Remove selected words from topic " + this.selectedTopic.nameAsString() + "?");
            if (remove) {
                ArrayList<Word> wordsToRemove = new ArrayList<Word>(this.wordView.getSelectionModel().getSelectedItems());
                this.selectedTopic.removeAll(wordsToRemove);
                this.wordView.getItems().removeAll(wordsToRemove);
            }
        }
    }

    @FXML
    private void handleDeleteWordsButton() {
        if (this.wordView.getSelectionModel().getSelectedItems().size() == 0) {
            AlertBox.display("No words selected","Please select words to remove first.");
        } else {
            boolean delete = ConfirmBox.display("Confirm delete words","Delete all selected words?");
            if (delete) {
                ArrayList<Word> wordsToDelete = new ArrayList<Word>(this.wordView.getSelectionModel().getSelectedItems());
                this.selectedTopic.removeAll(wordsToDelete);
                this.wordView.getItems().removeAll(wordsToDelete);
                this.main.getAll().removeAll(wordsToDelete);
                this.main.getWords().removeAll(wordsToDelete);
            }
        }
    }

    @FXML
    private void handleAddSelectedWordsButton() {
        if (this.wordView.getSelectionModel().getSelectedItems().size() == 0) {
            AlertBox.display("No words selected","Please select words to add first.");
        } else {
            ArrayList<Topic> topicsWithoutAll = new ArrayList<>(this.topics);
            topicsWithoutAll.remove(this.main.getAll());
            List<Topic> listsToAddTo = TopicSelector.chooseTopics(topicsWithoutAll);
            for (Topic topic : listsToAddTo) {
                for (Word word : this.wordView.getSelectionModel().getSelectedItems())
                    if (!topic.contains(word))
                        topic.add(word);
            }
        }
    }
}
