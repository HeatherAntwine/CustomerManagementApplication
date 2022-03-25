/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author hrant
 */
public class ReportDashboardController implements Initializable {

    @FXML
    private Button backButt;
    @FXML
    private Button apptButt;
    @FXML
    private Button consButt;
    @FXML
    private Button custButt;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void backButtAction(ActionEvent event)   throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Views/mainDashboard.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
    }

    @FXML
    private void apptButtAction(ActionEvent event)  throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Views/reportApptTypes.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
    }

    @FXML
    private void consButtAction(ActionEvent event)   throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Views/reportConsSch.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
    }

    @FXML
    private void custButtAction(ActionEvent event)   throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Views/reportCustCount.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
    }
    
}
