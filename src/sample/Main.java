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

import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends Application {
    TextField tx1;
    TextField tx2;
    static int count = 0;

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
        System.out.println("get text = :: " + str);
        tx2.clear();
        tx2.setText(recursive_cal(str));
    }

    String recursive_cal (String str) {

        Pattern p = Pattern.compile("(sin|cos|tan)\\(([\\w+\\-\\+\\.x:]+)\\)");
        Matcher m = p.matcher(str);
        StringBuffer sb = new StringBuffer();
        boolean found = false;

        while (m.find()) {
            found = true;
            System.out.println("Found: " + m.group(2));
            String t = calculate_simple(m.group(2));
            System.out.println("t = " + t);
            double y = 0;
            switch (m.group(1)) {
                case "sin":
                    y = Math.sin(Double.valueOf(t));
                    break;
                case "cos":
                    y = Math.cos(Double.valueOf(t));
                    break;
                case "tan":
                    y = Math.tan(Double.valueOf(t));
                    break;
            }

            m.appendReplacement(sb, String.format("%.2f",y));
        }

        if (found) {
            m.appendTail(sb);
            System.out.println(sb.toString());
            return recursive_cal(sb.toString());
        } else {
            return calculate_simple(str);
        }
    }

    String calculate_simple (String str) {
        Pattern p = Pattern.compile("(-?\\d+(?:\\.\\d+)?)[x:](-?\\d+(?:\\.\\d+)?)");
        Matcher m = p.matcher(str);
        boolean found = false;
        StringBuffer sb = new StringBuffer();

        double t = 0;
        while (m.find()) {
            found = true;
            if (m.group().contains(":")) {
                t = Double.valueOf(m.group(1))/Double.valueOf(m.group(2));
            } else {
                t = Double.valueOf(m.group(1))*Double.valueOf(m.group(2));
            }
            m.appendReplacement(sb, String.format("%.2f", t));
        }

        if (found) {
            m.appendTail(sb);
            return calculate_simple(sb.toString());
        } else {
            p = Pattern.compile("(-?\\d+(?:\\.\\d+)?)");
            m = p.matcher(str);

            while (m.find()) {
                t = t + Double.valueOf(m.group());
            }
            return String.valueOf(t);
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
