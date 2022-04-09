package VocabApp.view;

import VocabApp.model.Topic;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;


public class TopicEditor {

    static String answer;

    public static String display(List<String> list, String initialString) {
        answer = null;
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("New Topic");
        window.setMinWidth(300);
        Label label = new Label("New Topic Name:");
        TextField textField = new TextField();
        if (initialString != null) {
            textField.setText(initialString);
        }
        Label warning = new Label();
        Button okButton = new Button("OK");
        Button cancelButton = new Button("Cancel");
        okButton.setOnAction(event -> {
            String validName = validTitle(textField,list);
            if (validName.equals("valid")) {
                answer =  textField.getText().trim();
                window.close();
            } else if (validName.equals("already exists")) {
                warning.setText("That topic name already exists!");
                warning.setVisible(true);
            } else if (validName.equals("empty")) {
                warning.setText("Please enter a name.");
                warning.setVisible(true);
            } else if (validName.equals("contains underscore")) {
                warning.setText("Name cannot contain '_'.");
                warning.setVisible(true);
            } else if (validName.equals("is all")) {
                warning.setText("Name cannot be \"All\".");
                warning.setVisible(true);
            }
        });
        cancelButton.setOnAction(event -> {
            answer = null;
            window.close();
        });
        textField.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (!oldValue.equals(newValue)) {
                warning.setVisible(false);
            }
        }));
        HBox hBox = new HBox();
        hBox.getChildren().add(okButton);
        hBox.getChildren().add(cancelButton);
        hBox.setSpacing(20);
        VBox vBox = new VBox();
        vBox.getChildren().add(label);
        vBox.getChildren().add(textField);
        vBox.getChildren().add(warning);
        vBox.getChildren().add(hBox);
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(20));
        window.setScene(new Scene(vBox));

        window.showAndWait();
        return answer;
    }

    public static String validTitle(TextField textField, List<String> list) {
        String answer = textField.getText().trim();
        if (answer.length() == 0) return "empty";
        else if (answer.equals("All")) return "is all";
        else if (answer.contains("_")) return "contains underscore";
        else for (String s : list)
            if (s.equals(answer))
                return "already exists";
        return "valid";
    }

}
