/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Models.Appointment;
import Models.Customer;
import Models.Database;
import Models.LocaleData;
import com.sun.media.sound.InvalidDataException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author hrant
 */
public class ModApptController implements Initializable {

    @FXML
//    private ChoiceBox custCB;
    private TableView<Customer> custNameTV;
    @FXML
    private TableColumn<Customer, String> custNameCol;
    @FXML
    private TextField titleTF;
    @FXML
    private ChoiceBox typeCB;
    @FXML
    private ChoiceBox locationCB;
    @FXML
    private TextField consTF;
    @FXML
    private ChoiceBox dateCB;
    @FXML
    private ChoiceBox endHourCB;
    @FXML
    private ChoiceBox endMinCB;
    @FXML
    private ChoiceBox startHourCB;
    @FXML
    private ChoiceBox startMinCB;
    @FXML
    private Button saveButt;
    @FXML
    private Button cancelButt;
    @FXML
    private TextField urlTF;
    @FXML
    private TextArea descTA;
    private final float openTime = 8.0f;
    private final float closeTime = 16.0f;
    private final ZoneId currLocale = ZoneId.systemDefault();
    private final int phxOffset = 2;
    private final int nyOffset = 1;
    private final int londOffset = 6;
    private static Appointment apptToMod = null;
    private final String[] descChoices = {"Initial Consult", "Consultation", "Project Kickoff", "Lunch", "Closing"};
    private final String[] locationChoices = {"Phoenix", "London", "New York"};
    private final String[] startHourChoices = {"08", "09", "10", "11", "12", "13", "14", "15"};
    private final String[] startMinuteChoices = {"00", "15", "30", "45"};
    private final String[] endHourChoices = {"08", "09", "10", "11", "12", "13", "14", "15"};
    private final String[] endMinuteChoices = {"00", "15", "30", "45"};
    private final String[] dateChoices = {"10", "11", "12", "19", "26", "30"};

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LocaleData.updateLocalCustomers();

        custNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        custNameTV.getItems().setAll(listCustomers());
        typeCB.setItems(getDescList());
        locationCB.setItems(getLocationList());
        startHourCB.setItems(getStartHours());
        startMinCB.setItems(getStartMinutes());
        endHourCB.setItems(getEndHours());
        endMinCB.setItems(getEndMinutes());
        dateCB.setItems(getDateList());
        titleTF.setText(apptToMod.getTitle());
        consTF.setText(apptToMod.getContact());
        urlTF.setText(apptToMod.getUrl());
// Fill in typeCB with Appt's type
        if (apptToMod.getDescription().equals(descChoices[0])) {
            typeCB.getSelectionModel().select(0);
        } else if (apptToMod.getDescription().equals(descChoices[1])) {
            typeCB.getSelectionModel().select(1);
        } else if (apptToMod.getDescription().equals(descChoices[2])) {
            typeCB.getSelectionModel().select(2);
        } else if (apptToMod.getDescription().equals(descChoices[3])) {
            typeCB.getSelectionModel().select(3);
        } else {
            typeCB.getSelectionModel().select(4);
        }
        //Fill in Location CB
        if (apptToMod.getLocation().equals(locationChoices[0])) {
            locationCB.getSelectionModel().select(0);
        } else if (apptToMod.getDescription().equals(locationChoices[1])) {
            locationCB.getSelectionModel().select(1);
        } else if (apptToMod.getDescription().equals(locationChoices[2])) {
            locationCB.getSelectionModel().select(2);
        } else {
            locationCB.getSelectionModel().select(3);
        }
        // Fill in Times and Date
        if (apptToMod.getStartHour().equals(startHourChoices[0])) {
            startHourCB.getSelectionModel().select(0);
        } else if (apptToMod.getStartHour().equals(startHourChoices[1])) {
            startHourCB.getSelectionModel().select(1);
        } else if (apptToMod.getStartHour().equals(startHourChoices[2])) {
            startHourCB.getSelectionModel().select(2);
        } else if (apptToMod.getStartHour().equals(startHourChoices[3])) {
            startHourCB.getSelectionModel().select(3);
        } else if (apptToMod.getStartHour().equals(startHourChoices[4])) {
            startHourCB.getSelectionModel().select(4);
        } else if (apptToMod.getStartHour().equals(startHourChoices[5])) {
            startHourCB.getSelectionModel().select(5);
        } else if (apptToMod.getStartHour().equals(startHourChoices[6])) {
            startHourCB.getSelectionModel().select(6);
        } else {
            startHourCB.getSelectionModel().select(7);
        }

        if (apptToMod.getEndHour().equals(endHourChoices[0])) {
            endHourCB.getSelectionModel().select(0);
        } else if (apptToMod.getEndHour().equals(endHourChoices[1])) {
            endHourCB.getSelectionModel().select(1);
        } else if (apptToMod.getEndHour().equals(endHourChoices[2])) {
            endHourCB.getSelectionModel().select(2);
        } else if (apptToMod.getEndHour().equals(endHourChoices[3])) {
            endHourCB.getSelectionModel().select(3);
        } else if (apptToMod.getEndHour().equals(endHourChoices[4])) {
            endHourCB.getSelectionModel().select(4);
        } else if (apptToMod.getEndHour().equals(endHourChoices[5])) {
            endHourCB.getSelectionModel().select(5);
        } else if (apptToMod.getEndHour().equals(endHourChoices[6])) {
            endHourCB.getSelectionModel().select(6);
        } else {
            endHourCB.getSelectionModel().select(7);
        }

        if (apptToMod.getStartMinute().equals(startMinuteChoices[0])) {
            startMinCB.getSelectionModel().select(0);
        } else if (apptToMod.getStartMinute().equals(startMinuteChoices[1])) {
            startMinCB.getSelectionModel().select(1);
        } else if (apptToMod.getStartMinute().equals(startMinuteChoices[2])) {
            startMinCB.getSelectionModel().select(2);
        } else {
            startMinCB.getSelectionModel().select(3);
        }

        if (apptToMod.getEndMinute().equals(endMinuteChoices[0])) {
            endMinCB.getSelectionModel().select(0);
        } else if (apptToMod.getEndMinute().equals(endMinuteChoices[1])) {
            endMinCB.getSelectionModel().select(1);
        } else if (apptToMod.getEndMinute().equals(endMinuteChoices[2])) {
            endMinCB.getSelectionModel().select(2);
        } else {
            endMinCB.getSelectionModel().select(3);
        }

        if (apptToMod.getDay() == Integer.parseInt(dateChoices[0])) {
            dateCB.getSelectionModel().select(0);
        } else if (apptToMod.getDay() == Integer.parseInt(dateChoices[1])) {
            dateCB.getSelectionModel().select(1);
        } else if (apptToMod.getDay() == Integer.parseInt(dateChoices[2])) {
            dateCB.getSelectionModel().select(2);
        } else if (apptToMod.getDay() == Integer.parseInt(dateChoices[3])) {
            dateCB.getSelectionModel().select(3);
        } else if (apptToMod.getDay() == Integer.parseInt(dateChoices[4])) {
            dateCB.getSelectionModel().select(4);
        } else {
            dateCB.getSelectionModel().select(5);
        }

        // Satisfies Rubric Item C
        saveButt.setOnAction((ActionEvent event) -> {
            try {
                saveAppt();
                Parent root = FXMLLoader.load(getClass().getResource("/Views/appts.fxml"));
                Scene scene = new Scene(root);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
            } catch (SQLException | IOException ex) {
                Logger.getLogger(AddApptController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private List<Customer> listCustomers() {
        ArrayList<Customer> list = new ArrayList<>();
        for (int i = 0; i < LocaleData.getCustList().size(); i++) {
            list.add(LocaleData.getCustList().get(i));
        }
        return list;
    }

    private ObservableList<String> getDateList() {
        ObservableList<String> dateList = FXCollections.observableArrayList();
        dateList.addAll(Arrays.asList(dateChoices));
        return dateList;
    }

    private ObservableList<String> getStartHours() {
        ObservableList<String> startHourList = FXCollections.observableArrayList();
        startHourList.addAll(Arrays.asList(startHourChoices));
        return startHourList;
    }

    private ObservableList<String> getStartMinutes() {
        ObservableList<String> startMinList = FXCollections.observableArrayList();
        startMinList.addAll(Arrays.asList(startMinuteChoices));
        return startMinList;
    }

    private ObservableList<String> getEndHours() {
        ObservableList<String> endHourList = FXCollections.observableArrayList();
        endHourList.addAll(Arrays.asList(endHourChoices));
        return endHourList;
    }

    private ObservableList<String> getEndMinutes() {
        ObservableList<String> endMinList = FXCollections.observableArrayList();
        endMinList.addAll(Arrays.asList(endMinuteChoices));
        return endMinList;
    }

    private ObservableList<String> getDescList() {
        ObservableList<String> descList = FXCollections.observableArrayList();
        descList.addAll(Arrays.asList(descChoices));
        return descList;
    }

    private ObservableList getLocationList() {
        ObservableList<String> locationList = FXCollections.observableArrayList();
        locationList.addAll(Arrays.asList(locationChoices));
        return locationList;
    }

    public static void setModAppt(Appointment appointment) {
        apptToMod = appointment;
    }

    private void saveAppt() throws SQLException, IOException {

        boolean conflicting = false;
        boolean outsideBusHours = false;

        if (custNameTV.getSelectionModel().getSelectedItem() == null
                || typeCB.getSelectionModel().getSelectedItem() == null
                || locationCB.getSelectionModel().getSelectedItem() == null
                || startHourCB.getSelectionModel().getSelectedItem() == null
                || endHourCB.getSelectionModel().getSelectedItem() == null
                || startMinCB.getSelectionModel().getSelectedItem() == null
                || endMinCB.getSelectionModel().getSelectedItem() == null
                || dateCB.getSelectionModel().getSelectedItem() == null
                || titleTF.getText() == null
                || urlTF.getText() == null
                || consTF.getText() == null) {
            Stage errorWindow = new Stage();
            errorWindow.initModality(Modality.APPLICATION_MODAL);
            errorWindow.setTitle("ERROR ERROR");
            Label errorLabel = new Label();
            errorLabel.setText("Invalid Appointment Data! Please try again.");
            Button OKButton = new Button("OK");
            OKButton.setOnAction(e -> errorWindow.close());
            VBox layout = new VBox(10);
            layout.getChildren().addAll(errorLabel, OKButton);
            layout.setAlignment(Pos.CENTER);
            Scene scene = new Scene(layout);
            errorWindow.setScene(scene);
            errorWindow.showAndWait();
        } else {
            String buildNewApptStartTime = startHourCB.getSelectionModel().getSelectedItem().toString() + "." + startMinCB.getSelectionModel().getSelectedItem().toString();
            Float apptNewStartTime = Float.valueOf(buildNewApptStartTime);
            String buildNewApptEndTime = endHourCB.getSelectionModel().getSelectedItem().toString() + "." + endMinCB.getSelectionModel().getSelectedItem().toString();
            Float apptNewEndTime = Float.valueOf(buildNewApptEndTime);

            if ((apptNewStartTime < openTime || apptNewStartTime > closeTime) || apptNewEndTime > closeTime || apptNewStartTime < openTime) {
                outsideBusHours = true;
                System.out.println("Outside hours? " + outsideBusHours);
                Stage errorWindow = new Stage();
                errorWindow.initModality(Modality.APPLICATION_MODAL);
                errorWindow.setTitle("ERROR ERROR");
                Label errorLabel = new Label();
                errorLabel.setText("Appointment times outside business hours. Please enter new times.");
                Button OKButton = new Button("OK");
                OKButton.setOnAction(e -> errorWindow.close());
                VBox layout = new VBox(10);
                layout.getChildren().addAll(errorLabel, OKButton);
                layout.setAlignment(Pos.CENTER);
                Scene scene = new Scene(layout);
                errorWindow.setScene(scene);
                errorWindow.showAndWait();
            }
            if (apptNewEndTime > apptNewStartTime) {
                for (int i = 0; i < LocaleData.getApptList().size(); i++) {
                    //        
                    String buildCurrApptStartTime = LocaleData.getApptList().get(i).getStartHour() + "." + LocaleData.getApptList().get(i).getStartMinute();
                    Float apptCurrStartTime = Float.valueOf(buildCurrApptStartTime);
                    String buildCurrApptEndTime = LocaleData.getApptList().get(i).getEndHour() + "." + LocaleData.getApptList().get(i).getEndMinute();
                    Float apptCurrEndTime = Float.valueOf(buildCurrApptEndTime);

                    if (((apptNewStartTime >= apptCurrStartTime
                            && apptNewStartTime <= apptCurrEndTime)
                            || (apptNewEndTime >= apptCurrStartTime
                            && apptNewEndTime <= apptCurrEndTime)
                            || apptNewStartTime <= apptCurrStartTime
                            && apptNewEndTime >= apptCurrEndTime)) {

                        if (((LocaleData.getApptList().get(i).getDay()) == Integer.parseInt(dateCB.getSelectionModel().getSelectedItem().toString()))) {

                            conflicting = true;
                            Stage errorWindow = new Stage();
                            errorWindow.initModality(Modality.APPLICATION_MODAL);
                            errorWindow.setTitle("ERROR ERROR");
                            Label errorLabel = new Label();
                            errorLabel.setText("Conflicting Appointments. Please enter different times.");
                            Button OKButton = new Button("OK");
                            OKButton.setOnAction(e -> errorWindow.close());
                            VBox layout = new VBox(10);
                            layout.getChildren().addAll(errorLabel, OKButton);
                            layout.setAlignment(Pos.CENTER);
                            Scene scene = new Scene(layout);
                            errorWindow.setScene(scene);
                            errorWindow.showAndWait();
                            System.out.println("Conflicting Times?" + conflicting);
                        }

                    }

                }
            
            if (!conflicting && !outsideBusHours) {
                try {
                    int startHr = Integer.parseInt(startHourCB.getSelectionModel().getSelectedItem().toString());
                    int startMin = Integer.parseInt(startMinCB.getSelectionModel().getSelectedItem().toString());
                    int endHr = Integer.parseInt(endHourCB.getSelectionModel().getSelectedItem().toString());
                    int endMin = Integer.parseInt(endMinCB.getSelectionModel().getSelectedItem().toString());
                    int day = Integer.parseInt(dateCB.getSelectionModel().getSelectedItem().toString());
                    String title = titleTF.getText();
                    String description = typeCB.getSelectionModel().getSelectedItem().toString();
                    String customer = custNameTV.getSelectionModel().getSelectedItem().getCustomerName();
                    String location = locationCB.getSelectionModel().getSelectedItem().toString();
                    String contact = consTF.getText();
                    String url = urlTF.getText();

                    Database.modAppt(apptToMod.getApptId(), title, customer, description, location, contact, url, day, offsetTime(startHr), startMin, offsetTime(endHr), endMin);
                    System.out.println("Information to be updated:" + apptToMod.getApptId() + "," + title + "," + customer + "," + description + "," + location + "," + contact + "," + url + "," + day + "," + startHr + "," + startMin + "," + endHr + "," + endMin);
                } catch (NumberFormatException e) {
                    System.out.println("ERROR: " + e);
                }
            }
        }else {
                    Stage errorWindow = new Stage();
                    errorWindow.initModality(Modality.APPLICATION_MODAL);
                    errorWindow.setTitle("ERROR ERROR");
                    Label errorLabel = new Label();
                    errorLabel.setText("Appointment cannot end before it starts.");
                    Button OKButton = new Button("OK");
                    OKButton.setOnAction(e -> errorWindow.close());
                    VBox layout = new VBox(10);
                    layout.getChildren().addAll(errorLabel, OKButton);
                    layout.setAlignment(Pos.CENTER);
                    Scene scene = new Scene(layout);
                    errorWindow.setScene(scene);
                    errorWindow.showAndWait();
                }

    }}

    private int offsetTime(int time) {
        if (currLocale.equals(ZoneId.of("America/New_York"))) {
            time -= nyOffset;
        } else if (currLocale.equals(ZoneId.of("America/Phoenix"))) {
            time += phxOffset;
        } else if (currLocale.equals(ZoneId.of("Europe/London"))) {
            time -= londOffset;
        }
        return time;
    }

    @FXML
    private void cancelButtAction(ActionEvent event) throws IOException {
        Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION);
        exitAlert.initModality(Modality.NONE);
        exitAlert.setHeaderText("");
        exitAlert.setContentText("Cancel Action?");
        Optional<ButtonType> userConfirm = exitAlert.showAndWait();

        if (userConfirm.get() == ButtonType.OK) {
            Parent root = FXMLLoader.load(getClass().getResource("/Views/mainDashboard.fxml"));
            Scene scene = new Scene(root);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
        } else {
            System.out.println("");
        }
    }

}
