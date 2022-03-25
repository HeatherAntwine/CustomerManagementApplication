/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Models.Database;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author hrant
 */
public class AddCustController implements Initializable {

    @FXML
    private TextField nameTF;
    @FXML
    private TextField add1TF;
    @FXML
    private TextField add2TF;
    @FXML
    private TextField postalTF;
    @FXML
    private TextField phNumTF;
    @FXML
    private TextField cityTF;
    @FXML
    private TextField countryTF;
    @FXML
    private Button saveButt;
    @FXML
    private Button cancelButt;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
// postalTF.getText().length()
    @FXML
    private void saveButtAction(ActionEvent event) throws IOException, SQLException {
        if (nameTF.getText().length() == 0
                || postalTF.getText().length() == 0
                || phNumTF.getText().length() == 0
                || add1TF.getText().length() == 0
                || add2TF.getText().length() == 0
                || cityTF.getText().length() == 0
                || countryTF.getText().length() == 0) {
            Stage errorWindow = new Stage();
            errorWindow.initModality(Modality.APPLICATION_MODAL);
            errorWindow.setTitle("ERROR ERROR");
            Label errorLabel = new Label();
            errorLabel.setText("Invalid Customer Data! Please try again.");
            Button OKButton = new Button("OK");
            OKButton.setOnAction(e -> errorWindow.close());
            VBox layout = new VBox(10);
            layout.getChildren().addAll(errorLabel, OKButton);
            layout.setAlignment(Pos.CENTER);
            Scene scene = new Scene(layout);
            errorWindow.setScene(scene);
            errorWindow.showAndWait();
        } else {
            String customerName = nameTF.getText();
            String postalCode = postalTF.getText();
            String phoneNumber = phNumTF.getText();
            String address = add1TF.getText();
            String address2 = add2TF.getText();
            String city = cityTF.getText();
            String country = countryTF.getText();
            int customerId = Database.getNextCustomerId();
            int addressId = Database.getNextAddressId();
            int cityId = Database.getNextCityId();
            int countryId = Database.getNextCountryId();

            Database.addNewCustomer(customerId, customerName, address, address2, city, postalCode, country, addressId);
            Database.addNewAddress(addressId, address, address2, cityId, postalCode, phoneNumber);
            Database.addNewCity(cityId, city, countryId);
            Database.addNewCountry(countryId, country);
            Parent root = FXMLLoader.load(getClass().getResource("/Views/cust.fxml"));
            Scene scene = new Scene(root);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
        }
            
        
    }

    @FXML
    private void cancelButtAction(ActionEvent event) throws IOException {
        Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION);
        exitAlert.initModality(Modality.NONE);
        exitAlert.setHeaderText("");
        exitAlert.setContentText("Cancel Action?");
        Optional<ButtonType> userConfirm = exitAlert.showAndWait();

        if (userConfirm.get() == ButtonType.OK) {
            Parent root = FXMLLoader.load(getClass().getResource("/Views/cust.fxml"));
            Scene scene = new Scene(root);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
        } else {
            System.out.println("");
        }
    }

}
