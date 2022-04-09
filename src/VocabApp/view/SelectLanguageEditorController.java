package VocabApp.view;

import VocabApp.Main;
import VocabApp.util.Util;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class SelectLanguageEditorController {

    @FXML
    AnchorPane anchor;

    Main main;

    public void setMain(Main main) {
        this.main = main;
    }

    public void setup() {
        GridPane grid = new GridPane();
        anchor.getChildren().clear();
        anchor.getChildren().add(grid);
        ArrayList<Button> buttons = new ArrayList<Button>();
        for (String lang : Main.SUPPORTED_LANGUAGES) {
            if (lang.equals(main.getBaseLanguage())) continue;
            Button button = new Button(Main.langNames.apply(main.getBaseLanguage(), lang));
            button.setPrefSize(100, 100);
            //button.setPadding(new Insets(10));
            Image flag = Util.scaleHeight(new Image(Main.flags.apply(lang)),100,true);
            Color fontColor = flag.getPixelReader().getColor((int)flag.getWidth()/2, (int)flag.getHeight()/2).invert();
            button.setTextFill(fontColor);
            BackgroundImage backgroundImage = new BackgroundImage(flag,null,null,BackgroundPosition.CENTER,null);
            button.setBackground(new Background(backgroundImage));
            button.setFont(new Font("System",14));
            button.setOnAction(e -> {
                main.setTargetLanguage(lang);
                try {
                    main.getWindow().hide();
                    main.showEditor();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });
            buttons.add(button);
        }
        buttons.sort(new Comparator<Button>() {
            @Override
            public int compare(Button o1, Button o2) {
                String s1 = o1.getText();
                String s2 = o2.getText();
                return compare(s1,s2);
            }

            public int compare(String s1, String s2) {
                if (s1 == s2 || s1.equalsIgnoreCase(s2)) return 0;
                if (s1 == null || s1.length() == 0) return -1;
                if (s2 == null || s2.length() == 0) return 1;
                if ((""+s1.charAt(0)).equalsIgnoreCase(""+s2.charAt(0))) return compare(s1.substring(1),s2.substring(1));
                else return (""+s1.charAt(0)).toLowerCase().compareTo((""+s2.charAt(0)).toLowerCase());
            }
        });
        int gridHorizSize = (int) Math.ceil(Math.sqrt(buttons.size()));
        int gridVertSize = (int) Math.ceil(buttons.size()/((double)gridHorizSize));
        for (int i = 0; i < buttons.size(); i++) {
            Button button = buttons.get(i);
            grid.add(button,i%gridHorizSize,i/gridHorizSize);
            GridPane.setValignment(button, VPos.CENTER);
            GridPane.setHalignment(button, HPos.CENTER);
            GridPane.setMargin(button, new Insets(10));
        }
        main.getWindow().setWidth(122*gridHorizSize);
        main.getWindow().setHeight(130*gridVertSize);
        this.main.getWindow().setResizable(false);
    }

    @FXML
    private void initialize() {
    }

}
