/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartstart.controllers;

import com.smartstart.entities.Contract;
import com.smartstart.entities.Opportunity;
import com.smartstart.services.ContractServiceImpl;
import com.smartstart.services.OpportunityService;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author diabl
 */
public class ContractController implements Initializable {

    @FXML
    private TableView<Contract> table;
    @FXML
    private TableColumn<Contract, Integer> colid;
    private ObservableList<Contract> data;
    @FXML
    private TableColumn<Contract, String> colPayment;
    @FXML
    private TableColumn<Contract, Date> colStart;
    @FXML
    private TableColumn<Contract, Date> colFinish;
    @FXML
    private TableColumn<Contract, Float> colSum;
    @FXML
    private TableColumn<String, String> colApplication;
    private Button CancelButton;
    @FXML
    private AnchorPane parent;
    @FXML
    private Label Nombre_Opp;
    @FXML
    private TextField txtField;
    @FXML
    private Button Delete;
    @FXML
    private Button Add;
    @FXML
    private Button display;
    @FXML
    private Button Show_My_Draft;
    @FXML
    private Button reload;
    @FXML
    private Button reload1;

    
    
    @FXML
    public void reload() throws SQLException {
        ContractServiceImpl cs = new ContractServiceImpl();

        int i = cs.CountContracts(1);

        Nombre_Opp.setText("" + i + "");

        data = cs.listContract(1);

        colid.setCellValueFactory(new PropertyValueFactory<>("id_contract"));
        colPayment.setCellValueFactory(new PropertyValueFactory<>("payment_method"));

        colStart.setCellValueFactory(new PropertyValueFactory<>("job_category"));

        colFinish.setCellValueFactory(new PropertyValueFactory<>("job_description"));

        colSum.setCellValueFactory(new PropertyValueFactory<>("Budget"));

        colApplication.setCellValueFactory(new PropertyValueFactory<>("job_Duration"));

        System.out.println(data);

        table.setItems(data);

    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        OpportunityService s = new OpportunityService();
        int i = s.CountOpportunities(1);

        Nombre_Opp.setText("" + i + "");

        try {
            reload();
        } catch (SQLException ex) {
            Logger.getLogger(ContractController.class.getName()).log(Level.SEVERE, null, ex);
        }
        initFilter();
        // TODO
    }  
    private void initFilter() {

        txtField.setPromptText("Filter");

        txtField.textProperty().addListener(new InvalidationListener() {

            @Override

            public void invalidated(Observable o) {

                if (txtField.textProperty().get().isEmpty()) {

                    table.setItems(data);

                    return;

                }

                ObservableList<Contract> tableItems = FXCollections.observableArrayList();

                ObservableList<TableColumn<Contract, ?>> cols = table.getColumns();

                for (int i = 0; i < data.size(); i++) {

                    for (int j = 0; j < cols.size(); j++) {

                        TableColumn col = cols.get(j);

                        String cellValue = col.getCellData(data.get(i)).toString();

                        cellValue = cellValue.toLowerCase();

                        if (cellValue.contains(txtField.textProperty().get().toLowerCase())) {

                            tableItems.add(data.get(i));

                            break;

                        }

                    }

                }

                table.setItems(tableItems);

            }

        });

    }

    private void alert1(String Message) {
        Alert a1 = new Alert(Alert.AlertType.ERROR);
        a1.setTitle("Alert");
        a1.setHeaderText("Champ Vide");
        a1.setContentText(Message);
        a1.showAndWait();
    }

    private void annuler(ActionEvent event) {
        Stage stage = (Stage) CancelButton.getScene().getWindow();
        stage.close();
    }
    public void AfficherDetails(Contract c )
    { 
        
        colStart.setText(c.getStart_date().toString());
       
        colFinish.setText(c.getFinish_date().toString());
        colSum.setText(""+c.getSum());
         //coljob_category.setText(O.getJob_category());
    }

    @FXML
    private void RemoveContract(ActionEvent event) {
        if (table.getSelectionModel().getSelectedItem() == null) {
            alert1("PLEASE SELECT THE CONTRACT THAT YOU WANT TO DELETE");
            return;
        } else {
            if (alert1Confirmation() == true) {
                int idCon = 0;
                ObservableList<Contract> AllCon = table.getItems();
                ObservableList<Contract> SingleCon = table.getSelectionModel().getSelectedItems();
                OpportunityService s = new OpportunityService();
                table.getSelectionModel().getSelectedItem();
                System.out.println("Value is in this row which" + table.getSelectionModel().getSelectedItem().getId_contract());

                s.delete_opporunity(table.getSelectionModel().getSelectedItem().getId_contract());
                SingleCon.forEach(AllCon::remove);
            } else {
                return;
            }

        }
    }

    @FXML
    private void AddContract(ActionEvent event) {
        try {
            FXMLLoader detail = new FXMLLoader(getClass().getResource("/com/smartstart/gui/AddContractGui.fxml"));
            Parent root2 = (Parent) detail.load();
            Stage stage1 = new Stage();
            stage1.setScene(new Scene(root2));
            stage1.show();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    

    @FXML
    private void displayDetails(ActionEvent event) {
        try {
            FXMLLoader detail = new FXMLLoader(getClass().getResource("/com/smartstart/gui/DetailOpportunityGui.fxml"));
            Parent root1 = (Parent) detail.load();
            Stage stage = new Stage();
            

            System.out.println(table.getSelectionModel().getSelectedItem());
            stage.setScene(new Scene(root1));
            stage.show();
           AfficherDetails(table.getSelectionModel().getSelectedItem());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @FXML
    private void Show_Draft(ActionEvent event) {
    }

    @FXML
    private void ExportToExcel(ActionEvent event) {
    }
    private boolean alert1Confirmation() {
        Alert a1 = new Alert(Alert.AlertType.CONFIRMATION);
        a1.setTitle("CONFIRMATION DIALOG");
        a1.setHeaderText("SUPPRESSION CONFIRMATION");
        a1.setContentText("ARE YOU SURE THAT YOU WANT TO DELETE THIS Contract?");
        Optional<ButtonType> result = a1.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }

    }
    
}