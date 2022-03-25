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
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author hrant
 */
public class ReportConsSchController implements Initializable {

    private URL url;
    private ResourceBundle rb;
    private final String[] usernameOptions = {"test"};

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
    private ChoiceBox consCB;
    @FXML
    private Button backButt;
    @FXML
    private Button goButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        consCB.setItems(getUserChoices());

        this.url = url;
        this.rb = rb;
        LocaleData.getApptList();
    }

    @FXML
    public void backButtAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Views/reportDashboard.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
    }

    @FXML
    public void goButtonAction(ActionEvent event) throws IOException, SQLException {
        String userName = consCB.getSelectionModel().getSelectedItem().toString();
        updateReportList(userName);
    }

    private void updateReportList(String userName) throws SQLException {
        dateCol.setCellValueFactory(new PropertyValueFactory<>("day"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        custCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));

        apptTV.getItems().setAll(Database.getApptList(Database.getUserIdByUsername(userName)));
    }

    private ObservableList<String> getUserChoices() {
        ObservableList<String> usernameOptionsObservableList = FXCollections.observableArrayList();
        usernameOptionsObservableList.addAll(Arrays.asList(usernameOptions));
        return usernameOptionsObservableList;
    }

}
