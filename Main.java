import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;

public class Main extends Application {
    // TODO: adjust these for your environment
    private static final String DB_URL  = "jdbc:mysql://localhost:3306/yourDatabase";
    private static final String DB_USER = "yourUser";
    private static final String DB_PASS = "yourPass";

    // UI fields
    private TextField txtID    = new TextField();
    private TextField txtLast  = new TextField();
    private TextField txtFirst = new TextField();
    private TextField txtMI    = new TextField();
    private TextField txtAddr  = new TextField();
    private TextField txtCity  = new TextField();
    private TextField txtState = new TextField();
    private TextField txtPhone = new TextField();
    private TextField txtEmail = new TextField();
    
    @Override
    public void start(Stage primaryStage) {
        // Top: ID + View button
        HBox top = new HBox(8,
            new Label("Record ID:"), txtID,
            makeButton("View",  e -> doView())
        );
        top.setAlignment(Pos.CENTER_LEFT);
        top.setPadding(new Insets(10));

        // Center: form grid
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(8);
        form.setPadding(new Insets(0, 10, 10, 10));

        form.addRow(0, new Label("Last Name:"),   txtLast);
        form.addRow(1, new Label("First Name:"),  txtFirst);
        form.addRow(2, new Label("M.I.:"),        txtMI);
        form.addRow(3, new Label("Address:"),     txtAddr);
        form.addRow(4, new Label("City:"),        txtCity);
        form.addRow(5, new Label("State:"),       txtState);
        form.addRow(6, new Label("Telephone:"),   txtPhone);
        form.addRow(7, new Label("Email:"),       txtEmail);

        ColumnConstraints leftCol = new ColumnConstraints();
        leftCol.setMinWidth(Region.USE_PREF_SIZE);
        form.getColumnConstraints().addAll(leftCol, new ColumnConstraints(300));

        // Bottom: Insert, Update, Clear
        HBox bottom = new HBox(10,
            makeButton("Insert", e -> doInsert()),
            makeButton("Update", e -> doUpdate()),
            makeButton("Clear",  e -> clearFields())
        );
        bottom.setAlignment(Pos.CENTER);
        bottom.setPadding(new Insets(0, 0, 10, 0));

        // Assemble the scene
        BorderPane root = new BorderPane();
        root.setTop(top);
        root.setCenter(form);
        root.setBottom(bottom);

        Scene scene = new Scene(root);
        primaryStage.setTitle("Staff Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Button makeButton(String text, Runnable action) {
        Button btn = new Button(text);
        btn.setOnAction(e -> action.run());
        return btn;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }