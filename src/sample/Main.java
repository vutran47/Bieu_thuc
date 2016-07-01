package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends Application {
    TextField tx1;
    TextField tx2;

    @Override
    public void start(Stage primaryStage) throws Exception{
        GridPane fp = new GridPane( );
        fp.setHgap(20);
        fp.setVgap(10);
        //fp.setGridLinesVisible(true);
        fp.setAlignment(Pos.CENTER);
        fp.setPadding(new Insets(10,10,10,10));

        Label lb1 = new Label("Type your mathematical expression in the text field below...");
        lb1.setPrefSize(300,20);
        lb1.setAlignment(Pos.CENTER);
        lb1.setTextAlignment(TextAlignment.CENTER);
        fp.add(lb1,0,0,2,1);

        tx1 = new TextField();
        tx1.setPrefSize(300,20);
        tx1.setAlignment(Pos.CENTER);
        fp.add(tx1,0,1,2,1);

        Button bt1 = new Button("Calculate!");
        bt1.setTextAlignment(TextAlignment.CENTER);
        bt1.setPrefSize(150,20);
        fp.add(bt1,0,2,1,1);

        tx2 = new TextField("Result here");
        tx2.setPrefSize(150,20);
        tx2.setFont(new Font("Tahoma Italic", 10));
        tx2.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(4), null)));
        tx2.setAlignment(Pos.CENTER);
        fp.add(tx2,1,2,1,1);

        bt1.setOnAction(this::calc);

        primaryStage.setScene(new Scene(fp, 320, 125));
        primaryStage.setTitle("Regex Calculator");
        primaryStage.show();
    }


    void calc(ActionEvent e) {
        String str = tx1.getText();
        recursive_cal(str);
    }

    double recursive_cal (String str) {
        double result = 0;
        Pattern p = Pattern.compile("(sin|cos|tan)\\([\\w+-]+?\\)");
        Matcher m = p.matcher(str);
        boolean found = false;

        while (m.find()) {
            StringBuffer sb = new StringBuffer();
            System.out.println("> "+m.group());
            found = true;

            Pattern p2 = Pattern.compile("\\d+(.+)?(?=\\)+$)");
            Matcher m2 = p2.matcher(m.group());
            boolean found2 = false;
            while (m2.find()) {
                System.out.println(">> " + m2.group());
                switch (m.group().substring(4,m.group().length()-1)) {
                    case "sin":
                        m2.appendReplacement(sb, String.valueOf(recursive_cal(m2.group())));
                        m2.appendTail(sb);
                        System.out.println(">>> "+ sb.toString());
                        break;

                    case "cos":

                        break;

                    case "tan":

                        break;
                }
            }

        }

        if (!found) {
            System.out.println(str);
        }

        return 0;
    }




    public static void main(String[] args) {
        launch(args);
    }
}
