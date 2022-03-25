/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Models.Customer;
import Models.Database;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.ChoiceBox;
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
public class ModCustController implements Initializable {

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
    private static Customer custInfo = null;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nameTF.setText(custInfo.getCustomerName());
        add1TF.setText(Database.getAddress(custInfo.getAddressId()));
        add2TF.setText(Database.getAddress2(custInfo.getAddressId()));
        postalTF.setText(Database.getPostalCode(custInfo.getAddressId()));
        phNumTF.setText(custInfo.getPhNum());
        cityTF.setText(Database.getCity(Database.getCityId(custInfo.getAddressId())));
        countryTF.setText(Database.getCountry(Database.getCountryId(Database.getCityId(custInfo.getAddressId()))));
    }

    public static void setCustomerToBeModified(Customer customer) {
        custInfo = customer;
    }

    @FXML
    private void saveButtAction(ActionEvent event) throws IOException {
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
            String modName = nameTF.getText();
            String modAdd1 = add1TF.getText();
            String modAdd2 = add2TF.getText();
            String modPostal = postalTF.getText();
            String modPhone = phNumTF.getText();
            String modCity = cityTF.getText();
            String modCountry = countryTF.getText();
            Database.modCustomer(custInfo.getCustomerId(), custInfo.getAddressId(), modName, modPhone, modAdd1, modAdd2, modCity, modPostal, modCountry);
            Parent root = FXMLLoader.load(getClass().getResource("/Views/cust.fxml"));
            Scene scene = new Scene(root);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
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
