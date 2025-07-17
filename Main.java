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