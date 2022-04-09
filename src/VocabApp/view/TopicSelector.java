package VocabApp.view;

import VocabApp.model.Topic;
import VocabApp.util.AlertBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class TopicSelector {

    public static List<Topic> chooseTopics(List<Topic> allTopics) {
        final List<Topic> output = new ArrayList<Topic>();

        Stage window = new Stage();
        window.setTitle("Choose Topics");
        window.initModality(Modality.APPLICATION_MODAL);

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefWidth(300);
        anchorPane.setPrefHeight(300);

        ListView<String> listView = new ListView<>();
        listView.setPrefWidth(250);
        listView.setPrefHeight(300);

        ObservableList<String> topicNames = FXCollections.observableArrayList();
        for (Topic topic : allTopics)
            if (!topic.nameAsString().equals("All"))
                topicNames.add(topic.nameAsString());
        listView.setItems(topicNames);
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        HBox hBox = new HBox();
        hBox.setSpacing(20);
        hBox.setPadding(new Insets(20));
        Button selectButton = new Button("Select");
        selectButton.setOnAction(event -> {
            if (listView.getSelectionModel().getSelectedItems().size() == 0) {
                AlertBox.display("No topic selected","Please select a topic.");
            } else {
                for (String topicName : listView.getSelectionModel().getSelectedItems()) {
                    for (Topic topic : allTopics) {
                        if (topic.nameAsString().equals(topicName))
                            output.add(topic);
                    }
                }
                window.close();
            }
        });
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(event -> {
            window.close();
        });
        hBox.getChildren().add(selectButton);
        hBox.getChildren().add(cancelButton);
        anchorPane.getChildren().add(listView);
        anchorPane.getChildren().add(hBox);

        AnchorPane.setTopAnchor(listView,25.0);
        AnchorPane.setLeftAnchor(listView, 25.0);
        AnchorPane.setBottomAnchor(hBox,0.0);
        AnchorPane.setRightAnchor(hBox,0.0);

        Scene scene = new Scene(anchorPane,300,400);
        window.setScene(scene);

        window.setOnCloseRequest(event -> {
            event.consume();
            cancelButton.getOnAction().handle(null);
        });

        window.showAndWait();
        return output;
    }

}
