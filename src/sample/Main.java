package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends Application {
    private TextField tx1;
    private Label tx2;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane fp = new GridPane();
        fp.setHgap(10);
        fp.setVgap(10);
        fp.setAlignment(Pos.CENTER);
        fp.setPadding(new Insets(10, 10, 10, 10));

        Label lb1 = new Label("Type your mathematical expression in the text field below...");
        lb1.setPrefSize(355, 20);
        lb1.setAlignment(Pos.CENTER);
        lb1.setTextAlignment(TextAlignment.CENTER);
        fp.add(lb1, 0, 0, 2, 1);

        tx1 = new TextField();
        tx1.setPrefSize(355, 20);
        tx1.setAlignment(Pos.CENTER);
        fp.add(tx1, 0, 1, 2, 1);

        Button bt1 = new Button("Calculate!");
        bt1.setTextAlignment(TextAlignment.CENTER);
        bt1.setPrefSize(167, 20);
        fp.add(bt1, 0, 2, 1, 1);

        tx2 = new Label("Result here");
        tx2.setPrefSize(167, 25);
        tx2.setFont(new Font("Tahoma Italic", 11));
        tx2.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(4), null)));
        tx2.setAlignment(Pos.CENTER);
        fp.add(tx2, 1, 2, 1, 1);

        bt1.setOnAction(this::calc);

        primaryStage.setScene(new Scene(fp, 368, 125));
        primaryStage.setTitle("Regex Calculator");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void calc(ActionEvent e) {
        String str = tx1.getText().replaceAll(" ", "");
        tx1.setText(str);
        tx2.setText("");
        String t;
        try {
            t = ext_cr(str);
            tx2.setText(t);
        } catch (NumberFormatException num) {
            tx2.setText("Illegal Number Format");
        } catch (StackOverflowError stl) {
            tx2.setText("OVER-FUCKING-FLOWN");
        }
    }

    private String ext_cr(String str) {
        // First, lets see if there is error in putting parentheses
        Pattern check = Pattern.compile("^[^\\(]+\\)");
        Matcher matchercheck = check.matcher(str);

        Pattern check2 = Pattern.compile("\\(");
        Matcher matchercheck2 = check2.matcher(str);
        int count2 = 0;
        while (matchercheck2.find()) count2++;

        Pattern check3 = Pattern.compile("\\)");
        Matcher matchercheck3 = check3.matcher(str);
        int count3 = 0;
        while (matchercheck3.find()) count3++;

        if (matchercheck.find() | count2 != count3) {
            return "PARENTHESES STUPIDITY";
        }

        // Remove most inner parentheses before attempting further calculation
        Pattern p = Pattern.compile("[x:\\-]\\(([^()]+)\\)");
        Matcher m = p.matcher(str);
        StringBuffer sb = new StringBuffer();

        boolean f = false;

        while (m.find()) {
            f = true;
            String t;
            if (m.group().contains(":") | m.group().contains("x")) {
                t = (m.group().contains(":") ? ":" : "x") + calculate_simple(m.group(1));
            } else {
                t = "-" + calculate_simple(m.group(1));
            }
            m.appendReplacement(sb, t);
        }

        if (f) {
            m.appendTail(sb);
            return ext_cr(sb.toString());
        } else {
            System.out.println(str);
            return recursive_cal(str);
        }
    }

    private String recursive_cal(String str) {
        // Solve all geometrical functions before the simpler expressions
        // No logarithm yet
        Pattern p = Pattern.compile("(sin|cos|tan)\\(([\\w\\-\\+\\.x:]+)\\)"); // removed a dup "+" after w
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

            m.appendReplacement(sb, String.valueOf(y));
        }

        if (found) {
            m.appendTail(sb);
            return recursive_cal(sb.toString());
        } else {
            return calculate_simple(str);
        }
    }

    private String calculate_simple(String str) {
        // Multipliers first
        Pattern p = Pattern.compile("(-?\\d+(?:\\.\\d+)?)[x:](-?\\d+(?:\\.\\d+)?)");
        Matcher m = p.matcher(str);
        boolean found = false;
        StringBuffer sb = new StringBuffer();

        double t = 0;
        while (m.find()) {
            found = true;
            if (m.group().contains(":")) {
                t = Double.valueOf(m.group(1)) / Double.valueOf(m.group(2));
            } else {
                t = Double.valueOf(m.group(1)) * Double.valueOf(m.group(2));
            }
            m.appendReplacement(sb, String.valueOf(t));
        }

        if (found) {
            m.appendTail(sb);
            return calculate_simple(sb.toString());
        } else {
            str = str.replaceAll("--", "+");
            p = Pattern.compile("(-?\\d+(?:\\.\\d+)?)");
            m = p.matcher(str);
            while (m.find()) {
                t = t + Double.valueOf(m.group());
            }
            return String.valueOf(t);
        }
    }
}
