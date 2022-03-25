/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Models.Appointment;
import Models.Database;
import Models.LocaleData;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author hrant
 */
public class ApptsController implements Initializable {

    @FXML
    private TableView<Appointment> apptTV;
    @FXML
    private TableColumn<Appointment, String> dateCol;
    @FXML
    private TableColumn<Appointment, String> startCol;
    @FXML
    private TableColumn<Appointment, String> endCol;
    @FXML
    private TableColumn<Appointment, String> titleCol;
    @FXML
    private TableColumn<Appointment, String> typeCol;
    @FXML
    private TableColumn<Appointment, String> custCol;
    @FXML
    private RadioButton monthRB;
    @FXML
    private RadioButton weekRB;
    @FXML
    private Button addButt;
    @FXML
    private Button modButt;
    @FXML
    private Button delButt;
    @FXML
    private Button backButt;
    @FXML
    private ToggleGroup viewMode;
    private URL url;
    private ResourceBundle rb;
    private final ZoneId currLocale = ZoneId.systemDefault();
    private final int phxOffset = 2;
    private final int nyOffset = 1;
    private final int londOffset = 6;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // populate the tableView - dateCol, startCol, endCol, titleCol, typeCol, custCol
            LocaleData.updateLocalAppts();
        } catch (SQLException ex) {
            Logger.getLogger(ApptsController.class.getName()).log(Level.SEVERE, null, ex);
        }

//        dateCol.setCellValueFactory(new PropertyValueFactory<>("day"));
//        startCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
//        endCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
//        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
//        typeCol.setCellValueFactory(new PropertyValueFactory<>("description"));
//        custCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        custCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getCustomerName()));
        typeCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDescription()));
        titleCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getTitle()));
        endCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getEndTime()));
        startCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getStartTime()));
//dateCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDay()));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("day"));

        //Set up views - initially week - Toggle Group viewMode
        if (monthRB.isSelected()) {
            apptTV.getItems().setAll(showWeekly());
        } else {
            apptTV.getItems().setAll(showMonthly());
        }
    }

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
    public void changeViewMode(ActionEvent event) {
        initialize(url, rb);
    }

    private List<Appointment> showWeekly() {
        ArrayList<Appointment> apptWList = new ArrayList<>();
        //Use Calendar to determin which day of September it currently is and then show the week's view - https://www.tutorialspoint.com/java/util/java_util_calendar.htm
        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < LocaleData.getApptList().size(); i++) {
            int dayOfAppt;
            //Get the day's number from the appt's method getDay - 8,10 substring
            int dayApptString = LocaleData.getApptList().get(i).getDay();
            //check the substring's values for 0 and then show the weekly view
            if (dayApptString == 0) {
                // substring 1
                dayOfAppt = dayApptString;

            } else {
                dayOfAppt = (dayApptString);
            }
            if (dayOfAppt > (today - 7) && dayOfAppt < (today + 7)) {
                apptWList.add(LocaleData.getApptList().get(i));
            }
        }
        return apptWList;
    }

    private List<Appointment> showMonthly() {
        ArrayList<Appointment> apptMList = new ArrayList<>();
        for (int i = 0; i < LocaleData.getApptList().size(); i++) {
            apptMList.add(LocaleData.getApptList().get(i));
        }
        return apptMList;
    }

    //Don't use the actions - easier to not
    //TODO - clean up and delete these actions from FXML and Controller
//    @FXML
//    private void monthRBAction(ActionEvent event) {
//    }
//
//    @FXML
//    private void weekRBAction(ActionEvent event) {
//    }
    @FXML
    private void addButtAction(ActionEvent event) throws IOException {
        Alert infoAlert = new Alert(Alert.AlertType.CONFIRMATION);
        infoAlert.initModality(Modality.NONE);
        infoAlert.setHeaderText("");
        infoAlert.setContentText("You must have 15 minutes between appointments to help ensure quality customer services. \r\n If you attempt to schedule appointments back-to-back, an error WILL occur.");
        Optional<ButtonType> userConfirm = infoAlert.showAndWait();
        if (userConfirm.get() == ButtonType.OK) {
            Appointment apptMod = (apptTV.getSelectionModel().getSelectedItem());
            ModApptController.setModAppt(apptMod);
            Parent root = FXMLLoader.load(getClass().getResource("/Views/addAppt.fxml"));
            Scene scene = new Scene(root);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
        } else {
            System.out.println("Oops");
        }
//        Appointment apptMod = (apptTV.getSelectionModel().getSelectedItem());
//        ModApptController.setModAppt(apptMod);
//        Parent root = FXMLLoader.load(getClass().getResource("/Views/addAppt.fxml"));
//        Scene scene = new Scene(root);
//        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        window.setScene(scene);
    }

    @FXML
    private void modButtAction(ActionEvent event) throws IOException {
        if (apptTV.getSelectionModel().getSelectedItem() != null) {
            Appointment appointment = apptTV.getSelectionModel().getSelectedItem();
            ModApptController.setModAppt(appointment);
        }
        Parent root = FXMLLoader.load(getClass().getResource("/Views/modAppt.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
    }

    @FXML
    private void delButtAction(ActionEvent event) {
        Appointment appointment = apptTV.getSelectionModel().getSelectedItem();
        Database.delAppt(appointment.getApptId());
        initialize(url, rb);
    }

    @FXML
    private void backButtAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Views/mainDashboard.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
    }

}
