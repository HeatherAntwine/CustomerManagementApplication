/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Models.Customer;
import Models.Database;
import Models.LocaleData;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author hrant
 */
public class CustController implements Initializable {

    @FXML
    private TableView<Customer> custTV;
    @FXML
    private TableColumn<Customer, String> custNameCol;
    @FXML
    private TableColumn<Customer,String> infoCol;
    @FXML
    private Button addButt;
    @FXML
    private Button modButt;
    @FXML
    private Button delButt;
    @FXML
    private Button backButt;
    private URL url;
    private ResourceBundle rb;

    /**
     * Initializes the controller class.
     *
   
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        LocaleData.updateLocalCustomers();
        custNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        infoCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        custTV.getItems().setAll(listCustomers());
    }

    private List<Customer> listCustomers() {
        ArrayList<Customer> list = new ArrayList<>();
        for (int i = 0; i < LocaleData.getCustList().size(); i++) {
            list.add(LocaleData.getCustList().get(i));
        }
        return list;
    }

    @FXML
    private void addButtAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Views/addCust.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
    }

    @FXML
    private void modButtAction(ActionEvent event) throws IOException {
        if(custTV.getSelectionModel().getSelectedItem() != null){
            Customer customer = custTV.getSelectionModel().getSelectedItem();
            ModCustController.setCustomerToBeModified(customer);
            
            
        Parent root = FXMLLoader.load(getClass().getResource("/Views/modCust.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        }else{
            Alert errAlert = new Alert(Alert.AlertType.CONFIRMATION);
            errAlert.initModality(Modality.NONE);
            errAlert.setContentText("Press ok to return to list - CHOOSE A CUSTOMER");
            Optional<ButtonType> userCOnfirm = errAlert.showAndWait();
        }
    }

    @FXML
    private void delButtAction(ActionEvent event) {
        Customer customer = custTV.getSelectionModel().getSelectedItem();
        Database.delCustomer(customer);
        initialize(url,rb);
    }

    @FXML
    private void backButtAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Views/mainDashboard.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
    }

}
