/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Models.Database;
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
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author hrant
 */
public class ReportApptTypesController implements Initializable {

    @FXML
    private Label initialLabel;
    @FXML
    private Label consultLabel;
    @FXML
    private Label lunchLabel;
    @FXML
    private Label kickoffLabel;
    @FXML
    private Label closingLabel;
    @FXML
    private Button backButt;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try{
        initialLabel.setText(Database.getApptCountByType("Initial Consult"));
        consultLabel.setText(Database.getApptCountByType("Consultation"));
        lunchLabel.setText(Database.getApptCountByType("Lunch Meeting"));
        kickoffLabel.setText(Database.getApptCountByType("Project Kickoff"));
        closingLabel.setText(Database.getApptCountByType("Closing"));
        }catch(Exception e){
            System.out.println("ERROR: " + e);
        }
    }    

    @FXML
    private void backButtAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Views/reportDashboard.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
    }
    
}
