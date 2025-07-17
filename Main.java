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

     private void doView() {
        String id = txtID.getText().trim();
        if (id.isEmpty()) return;
        String sql = "SELECT * FROM Staff WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    txtLast .setText(rs.getString("lastName"));
                    txtFirst.setText(rs.getString("firstName"));
                    txtMI   .setText(rs.getString("mi"));
                    txtAddr .setText(rs.getString("address"));
                    txtCity .setText(rs.getString("city"));
                    txtState.setText(rs.getString("state"));
                    txtPhone.setText(rs.getString("telephone"));
                    txtEmail.setText(rs.getString("email"));
                } else {
                    showAlert("No record found for ID: " + id, Alert.AlertType.INFORMATION);
                }
            }
        } catch (SQLException ex) {
            showAlert("Database error: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void doInsert() {
        String sql =
            "INSERT INTO Staff(id,lastName,firstName,mi,address,city,state,telephone,email) " +
            "VALUES(?,?,?,?,?,?,?,?,?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            bindFields(ps, false);
            int rows = ps.executeUpdate();
            showAlert(rows + " record inserted.", Alert.AlertType.INFORMATION);
        } catch (SQLException ex) {
            showAlert("Database error: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }
     private void doUpdate() {
        String sql =
            "UPDATE Staff SET lastName=?, firstName=?, mi=?, address=?, " +
            "city=?, state=?, telephone=?, email=? WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            bindFields(ps, true);
            int rows = ps.executeUpdate();
            showAlert(rows + " record updated.", Alert.AlertType.INFORMATION);
        } catch (SQLException ex) {
            showAlert("Database error: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
     
    /** 
     * @param ps   the PreparedStatement 
     * @param shiftIdLast if true, bind ID as parameter 9 (for UPDATE),
     *                     otherwise bind ID first for INSERT
     */
    private void bindFields(PreparedStatement ps, boolean shiftIdLast) throws SQLException {
        int idx = 1;
        if (!shiftIdLast) {
            ps.setString(idx++, txtID.getText().trim());
        }
        ps.setString(idx++, txtLast.getText().trim());
        ps.setString(idx++, txtFirst.getText().trim());
        ps.setString(idx++, txtMI.getText().trim());
        ps.setString(idx++, txtAddr.getText().trim());
        ps.setString(idx++, txtCity.getText().trim());
        ps.setString(idx++, txtState.getText().trim());
        ps.setString(idx++, txtPhone.getText().trim());
        ps.setString(idx++, txtEmail.getText().trim());
        if (shiftIdLast) {
            ps.setString(idx, txtID.getText().trim());
        }
    }

   private void clearFields() {
        txtID.clear();
        txtLast.clear();
        txtFirst.clear();
        txtMI.clear();
        txtAddr.clear();
        txtCity.clear();
        txtState.clear();
        txtPhone.clear();
        txtEmail.clear();
    }

    private void showAlert(String msg, Alert.AlertType type) {
        Alert alert = new Alert(type, msg, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        // Optional: load the driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ignored) {}
        launch(args);
    }
}
