package VocabApp;

import VocabApp.model.Results;
import VocabApp.model.Topic;
import VocabApp.model.Word;
import VocabApp.util.ConfirmBox;
import VocabApp.util.Util;
import VocabApp.view.*;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Main extends Application {

    public static int fileNum = 0;

    public static final ArrayList<String> SUPPORTED_LANGUAGES = new ArrayList<String>(Arrays.asList(
            "dan", // Danish
            "deu", // German
            "eng", // English
            "fin", // Finnish
            "fra", // French
            "ita", // Italian
            "mri", // Maori
            "nld", // Dutch
            "por", // Portuguese
            "spa", // Spanish
            "swe" // Swedish
    ));

    public static final BiFunction<String,String,String> langNames = (baseLanguage, targetLanguage) -> {
        switch(baseLanguage) {
            case "dan": switch (targetLanguage) {
                case "dan": return "dansk";
                case "deu": return "tysk";
                case "eng": return "engelsk";
                case "fin": return "finsk";
                case "fra": return "fransk";
                case "ita": return "italiensk";
                case "mri": return "maori";
                case "nld": return "nederlansk";
                case "por": return "portugisisk";
                case "spa": return "spansk";
                case "swe": return "svensk";
            }
            case "deu": switch (targetLanguage) {
                case "dan": return "Dänisch";
                case "deu": return "Deutsch";
                case "eng": return "Englisch";
                case "fin": return "Finnisch";
                case "fra": return "Französisch";
                case "ita": return "Italienisch";
                case "mri": return "Maori";
                case "nld": return "Niederländisch";
                case "por": return "Portugiesisch";
                case "spa": return "Spanisch";
                case "swe": return "Schwedisch";
            }
            case "eng": switch (targetLanguage) {
                case "dan": return "Danish";
                case "deu": return "German";
                case "eng": return "English";
                case "fin": return "Finnish";
                case "fra": return "French";
                case "ita": return "Italian";
                case "mri": return "Maori";
                case "nld": return "Dutch";
                case "por": return "Portuguese";
                case "spa": return "Spanish";
                case "swe": return "Swedish";
                default: throw new NoSuchElementException();
            }
            case "fin": switch (targetLanguage) {
                case "dan": return "tanska";
                case "deu": return "saksa";
                case "eng": return "englanti";
                case "fin": return "suomi";
                case "fra": return "ranska";
                case "ita": return "italia";
                case "mri": return "maori";
                case "nld": return "hollanti";
                case "por": return "portugali";
                case "spa": return "espanja";
                case "swe": return "ruotsi";
            }
            case "fra": switch (targetLanguage) {
                case "dan": return "danois";
                case "deu": return "allemand";
                case "eng": return "anglais";
                case "fin": return "finnois";
                case "fra": return "français";
                case "ita": return "italien";
                case "mri": return "maori";
                case "nld": return "néerlandais";
                case "por": return "portugais";
                case "spa": return "espagnol";
                case "swe": return "suédois";
            }
            case "ita": switch (targetLanguage) {
                case "dan": return "danese";
                case "deu": return "tedesco";
                case "eng": return "inglese";
                case "fin": return "finlandese";
                case "fra": return "francese";
                case "ita": return "italiano";
                case "mri": return "māori";
                case "nld": return "nederlandese";
                case "por": return "portoghese";
                case "spa": return "spagnolo";
                case "swe": return "svedese";
            }
            case "mri": switch (targetLanguage) {
                case "dan": return "reo Teina";
                case "deu": return "reo Tiamana";
                case "eng": return "reo Pākeha";
                case "fin": return "reo Hinerangi";
                case "fra": return "reo Wīwī";
                case "ita": return "reo Itariana";
                case "mri": return "reo Māori";
                case "nld": return "reo Tatimana";
                case "por": return "reo Pōtikī";
                case "spa": return "reo Pāniora";
                case "swe": return "reo Huitene";
            }
            case "nld": switch (targetLanguage) {
                case "dan": return "Deens";
                case "deu": return "Duits";
                case "eng": return "Engels";
                case "fin": return "Fins";
                case "fra": return "Frans";
                case "ita": return "Italiaans";
                case "mri": return "Maori";
                case "nld": return "Nederlands";
                case "por": return "Portugees";
                case "spa": return "Spaans";
                case "swe": return "Zweeds";
            }
            case "por": switch (targetLanguage) {
                case "dan": return "dinamarquês";
                case "deu": return "alemão";
                case "eng": return "inglês";
                case "fin": return "finlandês";
                case "fra": return "francês";
                case "ita": return "italiano";
                case "mri": return "maori";
                case "nld": return "neerlandês";
                case "por": return "português";
                case "spa": return "espanhol";
                case "swe": return "sueco";
            }
            case "spa": switch (targetLanguage) {
                case "dan": return "danés";
                case "deu": return "alemán";
                case "eng": return "inglés";
                case "fin": return "finlandés";
                case "fra": return "francés";
                case "ita": return "italiano";
                case "mri": return "maori";
                case "nld": return "neerlandés";
                case "por": return "portugés";
                case "spa": return "español";
                case "swe": return "sueco";
            }
            case "swe": switch (targetLanguage) {
                case "dan": return "danska";
                case "deu": return "tyska";
                case "eng": return "engelska";
                case "fin": return "finska";
                case "fra": return "franska";
                case "ita": return "italienska";
                case "mri": return "maori";
                case "nld": return "nederländska";
                case "por": return "portugisiska";
                case "spa": return "spanska";
                case "swe": return "svenska";
            }
            default: throw new NoSuchElementException();
        }
    };

    public static final Function<String,String> flags = s -> "VocabApp/view/flags/" + s + ".png";

    public static final Function<String,String> backgroundImage = lang -> {
        File folder = new File("src/VocabApp/view/backgrounds/"+lang);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles == null || listOfFiles.length == 0) return null;
        int bound = listOfFiles.length;
        File file = listOfFiles[Main.random.nextInt(bound)];
        return file.getPath().substring(4);
    };

    public static final Random random = new Random();

    private Stage window;
    private static final AudioClip starEarnt = new AudioClip(new File("src/VocabApp/model/words/starEarnt.mp3").toURI().toString());
    private String baseLanguage;
    private String targetLanguage;
    private TreeSet<Word> words;
    private ArrayList<Topic> topics;
    private ObservableList<Word> selectedWords;
    private ObservableList<Word> selectedSpokenWords;
    private ObservableList<Topic> selectedTopics;
    private Topic all;
    private int learningMode;
    private int wordsPerGame;

    public Stage getWindow() {
        return window;
    }

    public String getBaseLanguage() {
        return baseLanguage;
    }

    public void setBaseLanguage(String baseLanguage) {
        this.baseLanguage = baseLanguage;
    }

    public String getTargetLanguage() {
        return targetLanguage;
    }

    public void setTargetLanguage(String targetLanguage) {
        this.targetLanguage = targetLanguage;
    }

    public TreeSet<Word> getWords() {
        return words;
    }

    public ArrayList<Topic> getTopics() {
        return topics;
    }

    public ObservableList<Word> getSelectedWords() {
        return selectedWords;
    }

    public void setSelectedWords(ObservableList<Word> selectedWords) {
        this.selectedWords = selectedWords;
    }

    public ObservableList<Word> getSelectedSpokenWords() {
        return selectedSpokenWords;
    }

    public void setSelectedSpokenWords(ObservableList<Word> selectedSpokenWords) {
        this.selectedSpokenWords = selectedSpokenWords;
    }

    public ObservableList<Topic> getSelectedTopics() {
        return selectedTopics;
    }

    public void setSelectedTopics(ObservableList<Topic> selectedTopics) {
        this.selectedTopics = selectedTopics;
    }

    public Topic getAll() {
        return all;
    }

    public void setAll(Topic all) {
        this.all = all;
    }

    public int getLearningMode() {
        return learningMode;
    }

    public void setLearningMode(int learningMode) {
        this.learningMode = learningMode;
    }

    public int getWordsPerGame() {
        return wordsPerGame;
    }

    public void setWordsPerGame(int wordsPerGame) {
        this.wordsPerGame = wordsPerGame;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //System.out.println("starting");
        loadWords();
        //System.out.println("words loaded");
        window = primaryStage;
        window.setTitle("Vocabulo");
        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });
        window.setResizable(false);
        showLauncher();
    }

    private void clearSelectedTopicsAndItems() {
        this.selectedTopics = null;
        this.selectedWords = null;
        this.all = null;
    }

    public void showLauncher() throws IOException {
        clearSelectedTopicsAndItems();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/Launcher.fxml"));
        Scene scene = new Scene(loader.load());
        LauncherController controller = loader.getController();
        controller.setMain(this);
        controller.setup();
        window.setScene(scene);
        window.show();
        window.centerOnScreen();
    }

    public void showSelectLanguagePlay() throws IOException {
        clearSelectedTopicsAndItems();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/SelectLanguagePlay.fxml"));
        Scene scene = new Scene(loader.load());
        SelectLanguagePlayController controller = loader.getController();
        controller.setMain(this);
        controller.setup();
        window.setScene(scene);
        window.centerOnScreen();
        window.show();
    }

    public void showPlayHome() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/PlayHome.fxml"));
        Scene scene = new Scene(loader.load(),1280,720);
        PlayHomeController controller = loader.getController();
        controller.setMain(this);
        controller.setup();
        window.setScene(scene);
        window.centerOnScreen();
        window.show();
    }

    public void showChooseWords() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/ChooseWords.fxml"));
        Scene scene = new Scene(loader.load(),1280,720);
        ChooseWordsController controller = loader.getController();
        controller.setMain(this);
        controller.setup();
        window.setScene(scene);
        window.centerOnScreen();
        window.show();
    }

    public void showSelectLearningMode() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/SelectLearningMode.fxml"));
        Scene scene = new Scene(loader.load(),1280,720);
        SelectLearningModeController controller = loader.getController();
        controller.setMain(this);
        controller.setup();
        window.setScene(scene);
        window.centerOnScreen();
        window.show();
    }

    public void showPlay() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/Play.fxml"));
        Scene scene = new Scene(loader.load(),1280,720);
        PlayController controller = loader.getController();
        controller.setMain(this);
        controller.setup();
        window.setScene(scene);
        window.centerOnScreen();
        window.show();
    }

    public void showResults(Results results) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/Results.fxml"));
        Scene scene = new Scene(loader.load(),1280,720);
        ResultsController controller = loader.getController();
        controller.setMain(this);
        controller.setup(results);
        window.setScene(scene);
        window.centerOnScreen();
        window.show();
    }

    public void showSelectLanguageEditor() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/SelectLanguageEditor.fxml"));
        Scene scene = new Scene(loader.load(),1280,720);
        SelectLanguageEditorController controller = loader.getController();
        controller.setMain(this);
        controller.setup();
        window.setScene(scene);
        window.centerOnScreen();
        window.show();
    }

    public void showEditor() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/Editor.fxml"));
        Scene scene = new Scene(loader.load(),1280,720);
        EditorController controller = loader.getController();
        controller.setMain(this);
        controller.setup();
        window.setScene(scene);
        window.centerOnScreen();
        window.show();
    }

    private void loadWords() throws IOException {
        topics = new ArrayList<>();
        words = new TreeSet<>(Word::compareTo);
        Scanner in = new Scanner(new File("src/VocabApp/model/words/wordsJSON.txt"));
        while (in.hasNext()) {
            String line = in.nextLine();
            if (line.charAt(0) == '\uFEFF') line = line.substring(1);
            ArrayList<String> topicNames = new ArrayList<String>();
            while (!line.substring(0, 1).equals("{")) {
                Scanner temp = new Scanner(line);
                topicNames.add(temp.next());
                line = temp.nextLine().substring(1);
            }
            Word word = new Word(line);
            this.words.add(word);
            for (String topicName : topicNames) {
                Topic topic = null;
                for (Topic t : this.topics) {
                    if (t.getName().equals(topicName) && t.baseLanguage.equals(word.getBaseLanguage()) && t.targetLanguage.equals(word.getTargetLanguage())) {
                        topic = t;
                        break;
                    }
                }
                if (topic == null) {
                    topic = new Topic(topicName, word.getBaseLanguage(), word.getTargetLanguage());
                    this.topics.add(topic);
                }
                topic.add(word);
            }
        }
        for (Word word : this.words) {
            word.updateStarsFromDate(Word.TARGET_TO_BASE_WRITTEN);
            word.updateStarsFromDate(Word.BASE_TO_TARGET_WRITTEN);
            word.updateStarsFromDate(Word.TARGET_TO_TARGET_SPOKEN);
            word.updateStarsFromDate(Word.TARGET_TO_BASE_SPOKEN);
        }
    }

    public void saveWords() throws IOException {
        PrintStream out = new PrintStream(new File("src/VocabApp/model/words/wordsJSON.txt"));
        for (Topic t : this.topics) {
            for (Word word : t) {
                if (words.contains(word)) {
                    for (Topic topic : this.topics) {
                        if (topic.contains(word))
                            out.print(topic.getName() + " ");
                    }
                    out.println(word.toStringJSON());
                    this.words.remove(word);
                }
            }
        }
        for (Word word : this.words) {
            for (Topic topic : this.topics) {
                if (topic.contains(word))
                    out.print(topic.getName() + " ");
            }
            out.println(word.toStringJSON());
        }
        out.close();
    }

    public void closeProgram() {
        boolean answer = ConfirmBox.display("Quit Vocabulo","Are you sure you want to exit?");
        if (answer) {
            try {
                for (Word word : this.words) {
                    word.updateStarsFromDate(Word.TARGET_TO_BASE_WRITTEN);
                    word.updateStarsFromDate(Word.BASE_TO_TARGET_WRITTEN);
                    word.updateStarsFromDate(Word.TARGET_TO_TARGET_SPOKEN);
                    word.updateStarsFromDate(Word.TARGET_TO_BASE_SPOKEN);
                }
                saveWords();
                window.close();
                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void playStarSound() {
        Main.starEarnt.play();
    }

    public static void main(String[] args) {
        //Test.main();
        launch(args);
    }
}
