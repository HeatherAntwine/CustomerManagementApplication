/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Models.Appointment;
import Models.Database;
import Models.LocaleData;
import Models.LoginTrackingFile;
import com.mysql.jdbc.Connection;
import java.io.IOException;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author hrant
 */
public class LoginController implements Initializable {

    Locale currLocale;
    @FXML
    private PasswordField passwordBox;
    @FXML
    public TextField nameBox;
    @FXML
    private Button loginButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TextArea informationBox;
    @FXML
    private Label loginLabel;
    @FXML
    private Label userNameLabel;
    @FXML
    private Label passwordLabel;
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String dbName = "U04Dtg";
    private static final String dbUsername = "U04Dtg";
    private static final String dbPassword = "53688208471";
    private static final String dbURL = "jdbc:mysql://52.206.157.109/" + dbName;
    private static final String defaultUsername = "admin";
    public static String currUserName;
    public static int currUserId;
    private boolean successfulLogin = false;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            Database.makeConnection();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(MainDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
        currLocale = Locale.getDefault();
        if (currLocale.getLanguage().equals("de")) {
            loginLabel.setText("Anmeldung");
            loginButton.setText("Anmeldung");
            cancelButton.setText("Stornieren");
            userNameLabel.setText("Nutzername");
            passwordLabel.setText("Passwort");
            informationBox.setText("Willkommen bei der Zeitplanungsanwendung für C195 von Heather Antwine");

        }
        if (currLocale.getLanguage().equals("en")) {
            loginLabel.setText("Login");
            loginButton.setText("Login");
            cancelButton.setText("Cancel");
            userNameLabel.setText("Username");
            passwordLabel.setText("Password");
            informationBox.setText("Welcome to the Scheduling Application for C195 by Heather Antwine");

        }

    }

    public void login(String userName, String password) {
        String sqlQuery = "SELECT * FROM user WHERE userName = ? AND password = ?";
        try (Connection conn = (Connection) DriverManager.getConnection(dbURL, dbUsername, dbPassword)) {
            PreparedStatement stmt = conn.prepareStatement(sqlQuery);
            stmt.setString(1, userName);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Calendar calendar = Calendar.getInstance();
                int currDay = calendar.get(Calendar.DAY_OF_MONTH);
                int currMonth = calendar.get(Calendar.MONTH);
                int currHour = calendar.get(Calendar.HOUR);
                int currMin = calendar.get(Calendar.MINUTE);
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                LoginTrackingFile.logInformation("\r\n User " + userName + " logged in: " + timestamp + ".\r\n");
                successfulLogin = true;
                currUserId = Database.getUserIdByUsername(nameBox.getText());
                LocaleData.setCurrUser(LocaleData.getUserList().get(currUserId));
            }
        } catch (Exception e) {
        }
    }

    @FXML
    private void loginButtonAction(ActionEvent event) throws IOException, SQLException {
        String userName = nameBox.getText();
        String password = passwordBox.getText();
        login(nameBox.getText(), passwordBox.getText());
        if (successfulLogin == true) {
            setupAlert();
            Parent root = FXMLLoader.load(getClass().getResource("/Views/mainDashboard.fxml"));
            Scene scene = new Scene(root);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);

        } else {
            if (currLocale.getLanguage().equals("de")) {
                informationBox.setText("Ungültige Informationen - bitte versuchen Sie es erneut");
            } else {
                informationBox.setText("Invalid information - please try again.");
            }
        }

    }

    private void setupAlert() throws SQLException {
        ArrayList<Appointment> apptListReminder = Database.getApptList(1);
        Calendar calendar = Calendar.getInstance();
        int nowDay = calendar.get(Calendar.DAY_OF_MONTH);
        int nowHour = calendar.get(Calendar.HOUR_OF_DAY);
        int nowMin = calendar.get(Calendar.MINUTE);
        int nowTimeMin = (nowHour * 60) + nowMin;
        for (int i = 0; i < apptListReminder.size(); i++) {
            if (apptListReminder.get(i).getDay() == nowDay) {
                int apptHour = Integer.parseInt(apptListReminder.get(i).getStartHour());
                int apptMin = Integer.parseInt(apptListReminder.get(i).getStartMinute());
                int apptTimeMin = (apptHour * 60) + apptMin;
                if (apptTimeMin - nowTimeMin <= 15 && apptTimeMin - nowTimeMin > 0) {
                    Stage alertWindow = new Stage();
                    alertWindow.initModality(Modality.APPLICATION_MODAL);
                    alertWindow.setTitle("Upcoming Appointment");
                    Label reminderLabel = new Label();
                    reminderLabel.setText("You have an appointment in " + (apptTimeMin - nowTimeMin) + " minutes - please refer to your schedule.");
                    Button OKButton = new Button("OK");
                    OKButton.setOnAction(e -> alertWindow.close());
                    VBox layout = new VBox(10);
                    layout.getChildren().addAll(reminderLabel, OKButton);
                    layout.setAlignment(Pos.CENTER);
                    Scene scene = new Scene(layout);
                    alertWindow.setScene(scene);
                    alertWindow.showAndWait();
                }
            }
        }

    }



    @FXML
    private void cancelButtonAction(ActionEvent event) throws IOException {
        Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION);
        exitAlert.initModality(Modality.NONE);
        exitAlert.setHeaderText("");
        exitAlert.setContentText("Exit program?");
        Optional<ButtonType> userConfirm = exitAlert.showAndWait();

        if (userConfirm.get() == ButtonType.OK) {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        } else {
            System.out.println("");
        }
    }

}
//package Controllers;
//
//import Models.Appointment;
//import Models.Database;
//import Models.LocaleData;
//import Models.LoginTrackingFile;
//import java.io.IOException;
//import java.net.URL;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Locale;
//import java.util.ResourceBundle;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.fxml.Initializable;
//import javafx.scene.Node;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Alert.AlertType;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.TextArea;
//import javafx.scene.control.TextField;
//import javafx.stage.Stage;
//
//public class LoginController implements Initializable {
//
//    @FXML
//    private Button loginButton;
//    @FXML
//    private TextField nameBox;
//    @FXML
//    private TextField passwordBox;
//    @FXML
//    private TextArea informationBox;
//    @FXML
//    private Label loginLabel;
//    @FXML
//    private Button cancelButton;
//    @FXML
//    private Label userNameLabel;
//    @FXML
//    private Label passwordLabel;
//    Locale currLocale;
//
//    @Override
//    public void initialize(URL url, ResourceBundle rb) {
//        try {
//            Database.makeConnection();
//        } catch (ClassNotFoundException | SQLException ex) {
//            Logger.getLogger(MainDashboardController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        currLocale = Locale.getDefault();
//        if (currLocale.getLanguage().equals("de")) {
//            loginLabel.setText("Anmeldung");
//            loginButton.setText("Anmeldung");
//            cancelButton.setText("Stornieren");
//            userNameLabel.setText("Nutzername");
//            passwordLabel.setText("Passwort");
//            informationBox.setText("Willkommen bei der Zeitplanungsanwendung für C195 von Heather Antwine");
//
//        }
//        if (currLocale.getLanguage().equals("en")) {
//            loginLabel.setText("Login");
//            loginButton.setText("Login");
//            cancelButton.setText("Cancel");
//            userNameLabel.setText("Username");
//            passwordLabel.setText("Password");
//            informationBox.setText("Welcome to the Scheduling Application for C195 by Heather Antwine");
//
//        }
//       
//    }
//
//    @FXML
//    public void cancelButtonAction(ActionEvent event) throws SQLException {
//        Stage stage = (Stage) cancelButton.getScene().getWindow();
//        stage.close();
//        Database.closeConnection();
//        System.exit(0);
//    }
//
//    @FXML
//    public void loginButtonAction(ActionEvent event) throws IOException, SQLException {
//        String username = nameBox.getText();
//        String password = passwordBox.getText();
//        for (int i = 0; i < LocaleData.getUserList().size(); i++) {
//            if (LocaleData.getUserList().get(i).getUserName().equals(username) && LocaleData.getUserList().get(i).getPassword().equals(password)) {
//                LocaleData.setCurrUser(LocaleData.getUserList().get(i));
//                break;
//            }
//        }
//        if (LocaleData.getCurrUser() == null) {
//            if (currLocale.getLanguage().equals("de")) {
//                informationBox.setText("FEHLER: Ungültige Information");
//            }
//        } else {
//            LocaleData.updateLocalAppts();
//            showApptAlert();
//            Calendar cal = Calendar.getInstance();
//            int currDay = cal.get(Calendar.DAY_OF_MONTH);
//            int currMonth = cal.get(Calendar.MONTH);
//            int currHour = cal.get(Calendar.HOUR);
//            int currMin = cal.get(Calendar.MINUTE);
//            System.out.println("Login Successful");
//            LoginTrackingFile.logInformation("User logged in: " + LocaleData.getCurrUser().getUserId() + " " + currMonth + "-" + currDay + " " + currHour + ":" + currMin + ".");
//            Parent root = FXMLLoader.load(getClass().getResource("/Views/mainDashboard.fxml"));
//            Scene scene = new Scene(root);
//            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            window.setScene(scene);
//        }
//        
//    }
//
//
//    private void showApptAlert() {
//        ArrayList<Appointment> apptList = LocaleData.getApptList();
//        Calendar cal = Calendar.getInstance();
//
//        int currentDay = cal.get(Calendar.DAY_OF_MONTH);
//        int currentHour = cal.get(Calendar.HOUR_OF_DAY);
//        int currentMin = cal.get(Calendar.MINUTE);
//        int curentTimeInMins = (currentHour * 60) + currentMin;
//
//        for (int i = 0; i < apptList.size(); i++) {
//
//            if ((apptList.get(i).getDay()) == currentDay) {
//                int appointmentHour = Integer.parseInt(apptList.get(i).getStartHour());
//                int appointmentMins = Integer.parseInt(apptList.get(i).getStartMinute());
//                int appointmentStartTime = appointmentHour * 60 + appointmentMins;
//
//                System.out.println("currentTime:" + curentTimeInMins);
//                System.out.println("appointmentStartTime:" + appointmentStartTime);
//
//                if (appointmentStartTime - curentTimeInMins <= 15 && appointmentStartTime - curentTimeInMins > 0) {
//                    System.out.println("currentTime:" + curentTimeInMins);
//                    System.out.println("appointmentStartTime:" + appointmentStartTime);
//                    displayAlert(apptList.get(i), appointmentStartTime - curentTimeInMins);
//                }
//            }
//        }
//    }
//
//    public void displayAlert(Appointment upcomingAppointment, int minutesTill) {
//        Alert alert = new Alert(AlertType.INFORMATION);
//        alert.setTitle("You have an upcoming appointment!");
//        alert.setHeaderText("You have an upcoming appointment!");
//        alert.setContentText("Your \"" + upcomingAppointment.getTitle() + "\" appointment with " + upcomingAppointment.getCustomerName() + " is in " + minutesTill + " minutes.");
//        //Alert does not need to be translated because it is not an error control message.
//        alert.showAndWait();
//    }
//
//    public static void failedConnection() {
//        Alert alert = new Alert(AlertType.ERROR);
//        alert.setTitle("Unable to connect to database!");
//        alert.showAndWait();
//    }
//}
