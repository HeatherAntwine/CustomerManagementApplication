/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

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
import javafx.event.EventHandler;
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
public class AddApptController implements Initializable {

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
    private ChoiceBox locationCB;
    @FXML
    private TextField consTF;
    @FXML
    private Button saveButt;
    @FXML
    private Button cancelButt;
    @FXML
    private Label errorLabel;
    @FXML
    private TextField urlTF;
    private final ZoneId currentLocale = ZoneId.systemDefault();

    private final String[] descChoices = {"Initial Consult", "Consultation", "Project Kickoff", "Lunch", "Closing"};
    private final String[] locationChoices = {"Phoenix", "London", "New York"};
    private final String[] startHourChoices = {"08", "09", "10", "11", "12", "13", "14", "15"};
    private final String[] startMinuteChoices = {"00", "15", "30", "45"};
    private final String[] endHourChoices = {"08", "09", "10", "11", "12", "13", "14", "15"};
    private final String[] endMinuteChoices = {"00", "15", "30", "45"};
    private final String[] dateChoices = {"10", "11", "12", "19", "26", "30"};
//Define Business Hours and time offset for offices
    private final float openTime = 8.0f;
    private final float closeTime = 16.0f;
    private final int phxOffset = 2;
    private final int nyOffset = 1;
    private final int londOffset = 6;

    //Lambdas for scheduling and maintaining appointments
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
        saveButt.setOnAction((ActionEvent event) -> {
            try {
                saveAppt();
                Parent root = FXMLLoader.load(AddApptController.this.getClass().getResource("/Views/appts.fxml"));
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

//    @FXML
//    private void saveButtAction(ActionEvent event) throws SQLException, IOException {
//
//        saveAppt();
//        Parent root = FXMLLoader.load(getClass().getResource("/Views/appts.fxml"));
//        Scene scene = new Scene(root);
//        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        window.setScene(scene);
//    }
    private void saveAppt() throws SQLException, IOException {
        boolean outsideBusHours = false;
        boolean conflictingTime = false;

// custName, type, location, starthour, startmin, endhour, endmin, date
        if ((custNameTV.getSelectionModel().getSelectedItem() == null)
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
            errorLabel.setText("Invalid Appointment Information. Please Try Again.");
            Button OKButton = new Button("OK");
            OKButton.setOnAction(e -> errorWindow.close());
            VBox layout = new VBox(10);
            layout.getChildren().addAll(errorLabel, OKButton);
            layout.setAlignment(Pos.CENTER);
            Scene scene = new Scene(layout);
            errorWindow.setScene(scene);
            errorWindow.showAndWait();
        } else {
            // Construct the startTime and endTime for new Appt for checking conflicts and business hours
            String buildNewApptStartTime = startHourCB.getSelectionModel().getSelectedItem().toString() + "." + startMinCB.getSelectionModel().getSelectedItem().toString();
            String buildNewApptEndTime = endHourCB.getSelectionModel().getSelectedItem().toString() + "." + endMinCB.getSelectionModel().getSelectedItem().toString();
            Float apptNewStartTime = Float.valueOf(buildNewApptStartTime);
            Float apptNewEndTime = Float.valueOf(buildNewApptEndTime);
            System.out.println("NewStartTime= " + apptNewStartTime); //CHECK
            System.out.println("NewEndTime = " + apptNewEndTime); //CHECK
if(apptNewEndTime > apptNewStartTime){
            // Construct the startTime and endTime for the current Appt for checking conflicts and business hours
            for (int i = 0; i < LocaleData.getApptList().size(); i++) {
                String buildCurrApptStartTime = LocaleData.getApptList().get(i).getStartHour() + "." + LocaleData.getApptList().get(i).getStartMinute();
                String buildCurrApptEndTime = LocaleData.getApptList().get(i).getEndHour() + "." + LocaleData.getApptList().get(i).getEndMinute();
                // Convert to float in order to more easily assess if time is valid
                Float apptCurrStartTime = Float.valueOf(buildCurrApptStartTime);
                System.out.println("CurrStartTime = " + apptCurrStartTime); //CHECK
                Float apptCurrEndTime = Float.valueOf(buildCurrApptEndTime);
                System.out.println("CurrEndTime = " + apptCurrEndTime); //CHECK

                //Check for time conflicts while iterating through appointment list
            
                    if ((apptNewStartTime >= apptCurrStartTime && apptNewStartTime <= apptCurrEndTime)
                            || (apptNewEndTime >= apptCurrStartTime && apptNewEndTime <= apptCurrEndTime)
                            || apptNewStartTime <= apptCurrStartTime
                            && apptNewEndTime >= apptCurrEndTime) {
                        if ((LocaleData.getApptList().get(i).getDay()) == Integer.parseInt(dateCB.getSelectionModel().getSelectedItem().toString())) {
                            System.out.println("Date = " + dateCB.getSelectionModel().getSelectedItem().toString());
                            conflictingTime = true;
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
                            System.out.println("conflictingTime true");
                        }
                    } else if ((apptNewStartTime < openTime || apptNewStartTime > closeTime) || (apptNewEndTime < openTime || apptNewEndTime > closeTime)) {
                        outsideBusHours = true;
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
                        System.out.println("outsideBusHours true");
                    }
                
            }
            
            if (!conflictingTime && !outsideBusHours) {
                //startHourCB startMinCB endHourCB endMinCB dayTF titleTF typeCB custNameTV locationCB consCB urlTF
                int startHour = Integer.parseInt(startHourCB.getSelectionModel().getSelectedItem().toString());
//                startHourCB.getSelectionModel().selectedItemProperty().addListener((startHour) -> startHour);
                int startMin = Integer.parseInt(startMinCB.getSelectionModel().getSelectedItem().toString());
                int endHour = Integer.parseInt(endHourCB.getSelectionModel().getSelectedItem().toString());
                int endMin = Integer.parseInt(endMinCB.getSelectionModel().getSelectedItem().toString());
                int day = Integer.parseInt(dateCB.getSelectionModel().getSelectedItem().toString());
                String title = titleTF.getText();
                String description = typeCB.getSelectionModel().getSelectedItem().toString();
                String customer = custNameTV.getSelectionModel().getSelectedItem().getCustomerName();
                String location = locationCB.getSelectionModel().getSelectedItem().toString();
                String contact = consTF.getText();
                String url = urlTF.getText();
                Database.addNewAppt(title, customer, description, location, contact, url, day, offsetTime(startHour), startMin, offsetTime(endHour), endMin);

            }
            //insert here
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
        }

    }

    private int offsetTime(int time) {
        if (currentLocale.equals(ZoneId.of("America/New_York"))) {
            time -= nyOffset;
        } else if (currentLocale.equals(ZoneId.of("America/Phoenix"))) {
            time += phxOffset;
        } else if (currentLocale.equals(ZoneId.of("Europe/London"))) {
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
