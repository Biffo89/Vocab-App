package VocabApp.view;

import VocabApp.Main;
import VocabApp.model.Results;
import VocabApp.model.Word;
import VocabApp.model.WordRun;
import VocabApp.util.ConfirmBox;
import VocabApp.util.Util;

import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmediaimpl.AudioClipProvider;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.CheckBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import javax.swing.Timer;

public class PlayController {

    private Function<String,String> soundCheckBoxText = baseLanguage -> {
        switch (baseLanguage) {
            case "dan": return "";
            case "deu": return "";
            case "eng": return "Sound"; // verified
            case "fin": return "";
            case "fra": return "Son";
            case "ita": return "";
            case "mri": return "";
            case "nld": return "";
            case "por": return "";
            case "spa": return "";
            case "swe": return "";
            default: throw new IllegalArgumentException();
        }
    };
    private Function<String,String> textFieldPromptText = baseLanguage -> {
        switch (baseLanguage) {
            case "dan": return "";
            case "deu": return "";
            case "eng": return "Enter word"; // verified
            case "fin": return "";
            case "fra": return "Entrez le mot";
            case "ita": return "";
            case "mri": return "";
            case "nld": return "";
            case "por": return "";
            case "spa": return "";
            case "swe": return "";
            default: throw new IllegalArgumentException();
        }
    };
    private Function<String,String> enterButtonText = baseLanguage -> {
        switch (baseLanguage) {
            case "dan": return "";
            case "deu": return "";
            case "eng": return "Enter"; // verified
            case "fin": return "";
            case "fra": return "Entrer";
            case "ita": return "";
            case "mri": return "";
            case "nld": return "";
            case "por": return "";
            case "spa": return "";
            case "swe": return "";
            default: throw new IllegalArgumentException();
        }
    };
    private Function<String,String> wrongAnswerLabelText = baseLanguage -> {
        switch (baseLanguage) {
            case "dan": return "";
            case "deu": return "";
            case "eng": return "Wrong answer"; // verified
            case "fin": return "";
            case "fra": return "Mauvaise réponse";
            case "ita": return "";
            case "mri": return "";
            case "nld": return "";
            case "por": return "";
            case "spa": return "";
            case "swe": return "";
            default: throw new IllegalArgumentException();
        }
    };
    private Function<String,String> questionLeftLabelText = baseLanguage -> {
        switch (baseLanguage) {
            case "dan": return "";
            case "deu": return "";
            case "eng": return "Question: "; // verified
            case "fin": return "";
            case "fra": return "Question : ";
            case "ita": return "";
            case "mri": return "";
            case "nld": return "";
            case "por": return "";
            case "spa": return "";
            case "swe": return "";
            default: throw new IllegalArgumentException();
        }
    };
    private Function<String,String> correctAnswerLeftLabelText = baseLanguage -> {
        switch (baseLanguage) {
            case "dan": return "";
            case "deu": return "";
            case "eng": return "Correct answer: "; // verified
            case "fin": return "";
            case "fra": return "Bonne réponse : ";
            case "ita": return "";
            case "mri": return "";
            case "nld": return "";
            case "por": return "";
            case "spa": return "";
            case "swe": return "";
            default: throw new IllegalArgumentException();
        }
    };
    private Function<String,String> yourAnswerLeftLabelText = baseLanguage -> {
        switch (baseLanguage) {
            case "dan": return "";
            case "deu": return "";
            case "eng": return "Your answer: "; // verified
            case "fin": return "";
            case "fra": return "Votre réponse : ";
            case "ita": return "";
            case "mri": return "";
            case "nld": return "";
            case "por": return "";
            case "spa": return "";
            case "swe": return "";
            default: throw new IllegalArgumentException();
        }
    };
    private Function<String,String> checkYourSpellingLabelText = baseLanguage -> {
        switch (baseLanguage) {
            case "dan": return "";
            case "deu": return "";
            case "eng": return "Check your spelling!"; // verified
            case "fin": return "";
            case "fra": return "Inspectez votre orthographe !";
            case "ita": return "";
            case "mri": return "";
            case "nld": return "";
            case "por": return "";
            case "spa": return "";
            case "swe": return "";
            default: throw new IllegalArgumentException();
        }
    };
    private Function<String,String> wrongAnswerWasRightButtonText = baseLanguage -> {
        switch (baseLanguage) {
            case "dan": return "";
            case "deu": return "";
            case "eng": return "I was right!"; // verified
            case "fin": return "";
            case "fra": return "J'avais raison !";
            case "ita": return "";
            case "mri": return "";
            case "nld": return "";
            case "por": return "";
            case "spa": return "";
            case "swe": return "";
            default: throw new IllegalArgumentException();
        }
    };
    private Function<String,String> wrongAnswerContinueButtonText = baseLanguage -> {
        switch (baseLanguage) {
            case "dan": return "";
            case "deu": return "";
            case "eng": return "Continue"; // verified
            case "fin": return "";
            case "fra": return "Continuer";
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

    private SimpleIntegerProperty timeRemaining;
    private long endTime;
    private Timer timer;
    private int roundsDone;
    private ObservableList<Word> playList;
    private WordRun run;
    private AudioClip audioClip;
    private Word word;
    private String answer;
    private Results results;

    @FXML private Button quitButton;
    @FXML private Button enterButton;
    @FXML private Label displayWord;
    @FXML private TextField textField;
    @FXML private ProgressBar progressBar;
    @FXML private ProgressIndicator timeWheel;
    @FXML private Label progressLabel;
    @FXML private CheckBox soundCheckBox;
    @FXML private AnchorPane darkOverlay;
    @FXML private BorderPane wrongAnswerMenu;
    @FXML private Label wrongAnswerLabel;
    @FXML private Label questionLeftLabel;
    @FXML private Label questionRightLabel;
    @FXML private Label correctAnswerLeftLabel;
    @FXML private Label correctAnswerRightLabel;
    @FXML private Label yourAnswerLeftLabel;
    @FXML private Label yourAnswerRightLabel;
    @FXML private Label checkSpellingLabel;
    @FXML private Button wrongAnswerWasRightButton;
    @FXML private Button wrongAnswerContinueButton;

    public void setMain(Main main) {
        this.main = main;
    }

    public void setup() throws IOException {
        initialiseUI();
        // temporarily change close operation to handle early quitting
        this.main.getWindow().setOnCloseRequest(event -> {
            boolean answer = ConfirmBox.display("Quit Vocabulo","Are you sure you want to exit?");
            if (answer) {
                quitEarly();
                try {
                    for (Word word : this.main.getWords()) {
                        word.updateStarsFromDate(Word.TARGET_TO_BASE_WRITTEN);
                        word.updateStarsFromDate(Word.BASE_TO_TARGET_WRITTEN);
                        word.updateStarsFromDate(Word.TARGET_TO_TARGET_SPOKEN);
                        word.updateStarsFromDate(Word.TARGET_TO_BASE_SPOKEN);
                    }
                    this.main.saveWords();
                    this.main.getWindow().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }});
        // select list of words to be used
        switch (this.main.getLearningMode()) {
            case Word.TARGET_TO_BASE_WRITTEN: {this.playList = this.main.getSelectedWords(); break;}
            case Word.BASE_TO_TARGET_WRITTEN: {this.playList = this.main.getSelectedWords(); break;}
            case Word.TARGET_TO_TARGET_SPOKEN: {this.playList = this.main.getSelectedSpokenWords(); break;}
            case Word.TARGET_TO_BASE_SPOKEN: {this.playList = this.main.getSelectedSpokenWords(); break;}
            default: throw new AssertionError();
        }
        // setup wordrun
        this.run = new WordRun();
        while (!this.run.isFull() && Util.containsLearnableWordNotInRun(this.playList,this.run,this.main.getLearningMode())) {
            Word word = Util.getRandomLearnableNotInRun(this.playList,this.run,this.main.getLearningMode());
            this.run.add(word);
        }
        // setup results
        this.results = new Results();
        // make sure wrong answer overlay is not visible
        setVisibleWrongAnswerScreen(false);
        // setup time
        ActionListener step = e -> this.timeRemaining.setValue((this.endTime - System.currentTimeMillis())/1000);
        this.timer = new Timer(1000,step);
        this.timeRemaining = new SimpleIntegerProperty();
        this.timeRemaining.addListener((observable,oldValue,newValue) -> {
            this.timeWheel.setProgress(((double) (int) newValue)/30);
            if ((int) newValue == 0) enterAnswer();
        });
        this.timeWheel.setProgress(1);
        // rounds
        this.roundsDone = 0;
        // start game
        runRound();
    }

    private void initialiseUI() {
        this.soundCheckBox.setText(this.soundCheckBoxText.apply(this.main.getBaseLanguage()));
        this.textField.setPromptText(this.textFieldPromptText.apply(this.main.getBaseLanguage()));
        this.enterButton.setText(this.enterButtonText.apply(this.main.getBaseLanguage()));
        this.wrongAnswerLabel.setText(this.wrongAnswerLabelText.apply(this.main.getBaseLanguage()));
        this.questionLeftLabel.setText(this.questionLeftLabelText.apply(this.main.getBaseLanguage()));
        this.correctAnswerLeftLabel.setText(this.correctAnswerLeftLabelText.apply(this.main.getBaseLanguage()));
        this.yourAnswerLeftLabel.setText(this.yourAnswerLeftLabelText.apply(this.main.getBaseLanguage()));
        this.checkSpellingLabel.setText(this.checkYourSpellingLabelText.apply(this.main.getBaseLanguage()));
        this.wrongAnswerWasRightButton.setText(this.wrongAnswerWasRightButtonText.apply(this.main.getBaseLanguage()));
        this.wrongAnswerContinueButton.setText(this.wrongAnswerContinueButtonText.apply(this.main.getBaseLanguage()));
    }

    private void runRound() throws IOException {
        if (this.roundsDone == this.main.getWordsPerGame()) {quit(); return;}

        setVisibleWrongAnswerScreen(false);
        this.word = this.run.get();
        if (this.word == null) {this.word = (Word) Util.getRandom(this.playList);}
        // set label
        switch (this.main.getLearningMode()) {
            case Word.TARGET_TO_BASE_WRITTEN: {this.displayWord.setText(this.word.getTargetLanguageDisplayWord()); break;}
            case Word.BASE_TO_TARGET_WRITTEN: {this.displayWord.setText(this.word.getBaseLanguageDisplayWord()); break;}
            default: this.displayWord.setText("");
        }
        // make check spelling label invisible
        this.checkSpellingLabel.setVisible(false);
        // clear text field and set focus
        this.textField.setText("");
        this.textField.requestFocus();
        // set progress bar and label
        this.progressBar.setProgress(((double) this.roundsDone)/((double) this.main.getWordsPerGame()));
        this.progressLabel.setText(this.roundsDone + "/" + this.main.getWordsPerGame());
        // set time
        this.endTime = System.currentTimeMillis() + 30000;
        this.timer.start();
        // text field listener
        this.textField.textProperty().addListener((observable, oldValue, newValue) -> {this.enterButton.disableProperty().setValue((newValue.equals("")));});
        // disable enter button until text field non empty
        this.enterButton.disableProperty().setValue(true);
        // setup enter word protocol
        this.wrongAnswerContinueButton.setDefaultButton(false);
        this.enterButton.setDefaultButton(true);
        this.enterButton.setOnAction(event -> {
            this.textField.onKeyPressedProperty().set(e -> {});
            this.enterButton.setOnAction(e -> {});
            this.enterButton.setDefaultButton(false);
            this.enterAnswer();
        });
        this.textField.onKeyPressedProperty().set(event -> {
            if (!this.textField.getText().equals("") && event.getCode().equals(KeyCode.ENTER)) {
                this.textField.onKeyPressedProperty().set(e -> {});
                this.enterButton.setOnAction(e -> {});
                this.enterButton.setDefaultButton(false);
                this.enterAnswer();
            }
        });
        // setup audio
        this.main.fileNum++;
        //this.main.fileNum%=5;
        if (this.audioClip != null) this.audioClip.stop();
        this.audioClip = null;
        Util.actionWithDelay(() -> {},100);
        if (this.word.getAudioData() != null) {
            //Util.makeTempFile("wordAudio.mp3",this.word.getAudioData());
            Util.makeTempFile("wordAudio" + this.main.fileNum + ".mp3",this.word.getAudioData());
            //this.audioClip = new AudioClip(new File("wordAudio.mp3").toURI().toString());
            this.audioClip = new AudioClip(new File("wordAudio" + this.main.fileNum + ".mp3").toURI().toString());
        }
        requestPlaySound();
        this.roundsDone++;
    }

    private void requestPlaySound() {
        if (this.soundCheckBox.isSelected() && (this.main.getLearningMode() == Word.TARGET_TO_BASE_WRITTEN || this.main.getLearningMode() == Word.TARGET_TO_TARGET_SPOKEN || this.main.getLearningMode() == Word.TARGET_TO_BASE_SPOKEN) && this.audioClip != null) {
            this.audioClip.play();
        }
    }

    private void enterAnswer() {
        this.timer.stop();
        if (this.audioClip != null) this.audioClip.stop();
        if (this.audioClip != null && this.main.getLearningMode() == Word.BASE_TO_TARGET_WRITTEN) this.audioClip.play();
        this.answer = this.textField.getText().trim();
        if (this.word.isCorrectAnswer(this.main.getLearningMode(),answer)) {
            this.success();
        } else if (this.word.isMisspeltAnswer(this.main.getLearningMode(),answer)) {
            System.out.println("misspelt");
            this.checkSpellingLabel.setVisible(true);
            EventHandler e = event -> {
                answer = this.textField.getText().trim();
                if (this.word.isCorrectAnswer(this.main.getLearningMode(),answer))
                    this.success();
                else failure();
            };
            Util.actionWithDelay(() -> {
                this.textField.onKeyPressedProperty().set(event -> {if (event.getCode().equals(KeyCode.ENTER)) e.handle(event);});
                this.enterButton.setOnAction(e);
            }, 200);
            this.checkSpellingLabel.setVisible(true);
        } else {
            System.out.println("wrong answer");
            this.failure();
        }
    }

    private void success() {
        this.word.success(this.main.getLearningMode());
        this.results.getRight(this.word);
        if (this.run.contains(this.word)) {
            boolean stillLearnable = false;
            if (this.main.getLearningMode() == 4) {
                if (this.word.isLearnable(Word.TARGET_TO_BASE_WRITTEN)) stillLearnable = true;
                if (this.word.isLearnable(Word.BASE_TO_TARGET_WRITTEN)) stillLearnable = true;
                if (this.word.isLearnable(Word.TARGET_TO_TARGET_SPOKEN)) stillLearnable = true;
                if (this.word.isLearnable(Word.TARGET_TO_BASE_SPOKEN)) stillLearnable = true;
            } else {
                if (this.word.isLearnable(this.main.getLearningMode())) stillLearnable = true;
            }
            if (stillLearnable)
                this.run.sendToBack(this.word);
            else {
                this.run.remove(this.word);
            }
        }
        while (!this.run.isFull() && Util.containsLearnableWordNotInRun(this.playList,this.run,this.main.getLearningMode())) {
            Word newWord = Util.getRandomLearnableNotInRun(this.playList, this.run, this.main.getLearningMode());
            this.run.add(newWord);
        }
        try {
            this.runRound();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void failure() {
        this.word.failure(this.main.getLearningMode());
        this.results.getWrong(this.word);
        if (this.run.contains(word))
            this.run.sendToBack(this.word);
        switch (this.main.getLearningMode()) {
            case Word.TARGET_TO_BASE_WRITTEN: {
                this.questionRightLabel.setText(this.word.getTargetLanguageDisplayWord());
                this.correctAnswerRightLabel.setText(this.word.getBaseLanguageDisplayWord());
                break;
            }
            case Word.BASE_TO_TARGET_WRITTEN: {
                this.questionRightLabel.setText(this.word.getBaseLanguageDisplayWord());
                this.correctAnswerRightLabel.setText(this.word.getTargetLanguageDisplayWord());
                break;
            }
            case Word.TARGET_TO_TARGET_SPOKEN: {
                this.questionRightLabel.setText("");
                this.correctAnswerRightLabel.setText(this.word.getSpokenAnswers().toString());
                break;
            }
            case Word.TARGET_TO_BASE_SPOKEN: {
                this.questionRightLabel.setText("");
                this.correctAnswerRightLabel.setText(this.word.getBaseLanguageDisplayWord());
                break;
            }
        }
        this.yourAnswerRightLabel.setText(this.answer);
        this.setVisibleWrongAnswerScreen(true);
        this.enterButton.setDefaultButton(false);
        this.wrongAnswerContinueButton.setOnAction(event -> {
            try {
                this.runRound();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Util.actionWithDelay(() -> this.wrongAnswerContinueButton.setDefaultButton(true),200);
        this.wrongAnswerContinueButton.requestFocus();
    }

    @FXML
    private void quitButton() throws IOException {
        quitEarly();
        quit();
    }

    private void quitEarly() {
        if (!this.run.isEmpty() && this.roundsDone != 1) {
            this.run.resetTimeRight();
        }
    }

    private void quit() throws IOException {
        this.timer.stop();
        this.main.getWindow().setOnCloseRequest(event -> {
            event.consume();
            this.main.closeProgram();
        });
        if (this.roundsDone != 1) this.main.showResults(this.results);
        else this.main.showSelectLearningMode();
    }

    private void setVisibleWrongAnswerScreen(boolean visible) {
        this.darkOverlay.setVisible(visible);
        this.darkOverlay.setMouseTransparent(!visible);
        this.wrongAnswerMenu.setVisible(visible);
        this.wrongAnswerMenu.setMouseTransparent(!visible);
    }

}
